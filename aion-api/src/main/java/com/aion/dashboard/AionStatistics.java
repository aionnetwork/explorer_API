package com.aion.dashboard;

import com.aion.dashboard.controller.Dashboard;
import com.aion.dashboard.entities.*;
import com.aion.dashboard.repository.BlockJpaRepository;
import com.aion.dashboard.repository.ParserStateJpaRepository;
import com.aion.dashboard.repository.TransactionJpaRepository;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.RewardsCalculator;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AionStatistics {

	AtomicLong rtLastBlockRead = new AtomicLong(0);

	static final long INTERVAL_STATS_MS_DAILY = 2000;
	static final long INTERVAL_STATS_MS_RT = 1000;

	static final int RT_BLK_COUNT_RETRIEVE = 128;
	static final int RT_TXN_COUNT_RETRIEVE = 10;

	static final int RT_BLK_COUNT_DISPLAY = 5;
	static final int RT_TXN_COUNT_DISPLAY = 10;

	@Autowired
	private BlockJpaRepository blockJpaRepository;
	
	@Autowired
	private TransactionJpaRepository transactionJpaRepository;
	
	@Autowired
	private ParserStateJpaRepository parserStateJpaRepository;

	@Autowired
	private Dashboard dashboard;

	@Autowired
	private WebSocketMessageBrokerStats webSocketMessageBrokerStats;

//	@Timed(value = "aion.statistics.rt")
	@Scheduled(fixedDelay = INTERVAL_STATS_MS_RT)
	public void threadRTStatistics() {
		Optional<ParserState> lastBlockRead = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
		if(lastBlockRead.isPresent()) {
			Long blockNumber = lastBlockRead.get().getBlockNumber();
			// deliberate use of equality and not gt or lt to deal with reorgs
			if(rtLastBlockRead.get() != blockNumber) {
				rtLastBlockRead.set(blockNumber);
				computeDashboardRTStatistics();
				dashboard.newBlockDashboard();
			}
		} else System.out.println("ERR: lastBlockReadJpaRepository.findById(1) is NULL");
	}

//	@Timed(value = "aion.statistics.daily")
	@Scheduled(fixedDelay = INTERVAL_STATS_MS_DAILY)
	public void threadDailyStatistics() {
		Optional<ParserState> lastBlockRead = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
		if(lastBlockRead.isPresent()) computeDailyStatistics(lastBlockRead.get().getBlockNumber());
		else System.out.println("ERR: lastBlockReadJpaRepository.findById(1) is NULL");
	}

	// blocks and transactions to look at for computing account statistics
    static final long ACC_STATS_BLK_COUNT = 12000;
    static final long ACC_STATS_TXN_COUNT = 100000;

    static final long MAX_BLK_BATCH = 3000;

	@SuppressWarnings("Duplicates")
	public void computeDailyStatistics(long endBlock) {
		long currTime = System.currentTimeMillis() / 1000;
		// time to sync 24 hours
		long startTime = currTime - 86400;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		// calculate max and sum of transactions in last 24 hours
		try {
			long sumTransactions = 0;
			long maxTransactions = 0;
			boolean rangeFound = false;

			// find in block master
			long startBlock = Math.max((endBlock - MAX_BLK_BATCH),0);
			long interimEndBlock = endBlock;

			while(!rangeFound) {

				List<Object> blockList = blockJpaRepository.getTimestampBetween(startBlock,interimEndBlock);
				if(blockList!=null && blockList.size()>0) {
					int masterSize = blockList.size();
					interimEndBlock = Long.parseLong((ow.writeValueAsString(blockList.get(0))).replace("[", "").replace("]", "").split(",")[0].trim());
					// verify if search time > last timestamp
					if(interimEndBlock == endBlock) {
						long endTime = Long.parseLong((ow.writeValueAsString(blockList.get(0))).replace("[", "").replace("]", "").split(",")[1].trim());
						if(startTime > endTime) {
							// no blocks found in last 24 hours
							startBlock = 0;
							endBlock = 0;
							rangeFound = true;
							break;
						}
					}

					// get timestamp of last index
					String lastBlock = (ow.writeValueAsString(blockList.get(masterSize-1))).replace("[", "").replace("]", "");
					String[] spl = lastBlock.split(",");
					long timestampVal = Long.parseLong(spl[1].trim());
					startBlock  = Long.parseLong(spl[0].trim());

					// found block range
					if(timestampVal <= startTime) {
						for(int i=masterSize-1;i>=0;i--) {
							lastBlock = (ow.writeValueAsString(blockList.get(i))).replace("[", "").replace("]", "");
							spl = lastBlock.split(",");
							startBlock = Long.parseLong(spl[0].trim());
							timestampVal = Long.parseLong(spl[1].trim());
							if(timestampVal >= startTime) {
								rangeFound = true;
								break;
							}
						}
					}
					else if(startBlock==0) {
						startBlock = 0;
						rangeFound = true;
						break;
					}
					// else parse more blocks by updating end block and start block and continue
					else {
						interimEndBlock = startBlock - 1;
						startBlock -= MAX_BLK_BATCH;
					}

				}else {
					break;
				}
			}

			// save max and sum transactions in master block range
			startBlock = startBlock > 0 ? startBlock : 0;
			List<Object> resultSet = blockJpaRepository.getSumAndMaxTransactionsInBlockRange(startBlock, endBlock);
			if(resultSet != null && resultSet.size() > 0) {
				String[] result = (ow.writeValueAsString(resultSet.get(0))).replace("[", "").replace("]", "").split(",");
				if(Utility.validInt(result[0].trim())) {
					maxTransactions = Long.parseLong(result[1].trim());
				}
				if(Utility.validInt(result[1].trim()))
					sumTransactions += Long.parseLong(result[0].trim());
			}

			// save unique account information for blocks mined and transactions 12,000 blocks and 100,000 transactions
			JSONArray inboundTransactions = new JSONArray();
			JSONArray outboundTransactions = new JSONArray();
			JSONArray blocksMined = new JSONArray();

			// blocks mined
			long start = Math.max(0,endBlock-12000);
			long blockRange = endBlock-start+1;
			List<Object> blocksMinedList = blockJpaRepository.getBlocksMinedByAccountByBlockNumberBetween(endBlock-ACC_STATS_BLK_COUNT, endBlock);
			if(blocksMinedList!=null && blocksMinedList.size()>0){
				for(int i=0;i<blocksMinedList.size();i++) {
					String[] result = (ow.writeValueAsString(blocksMinedList.get(i))).replace("[", "").replace("]", "").split(",");
					JSONArray json = new JSONArray();
					json.put(result[0].replace("\"", "").trim());
					json.put(result[1].trim());
					json.put(Integer.parseInt(result[2].trim())*100/blockRange);
					blocksMined.put(json);
				}
			}

			// outbound & inbound transactions
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
			if(parserState.isPresent()) {
			    long transactionId = parserState.get().getTransactionId();

				List<Object> outboundTransactionList = transactionJpaRepository.getFromTransactionByAccountByIdBetween(transactionId-ACC_STATS_TXN_COUNT, transactionId);
				if(outboundTransactionList!=null && outboundTransactionList.size()>0){
					for(int i=0;i<outboundTransactionList.size();i++) {
						String[] result = (ow.writeValueAsString(outboundTransactionList.get(i))).replace("[", "").replace("]", "").split(",");
						JSONArray json = new JSONArray();
						json.put(result[0].replace("\"", "").trim());
						json.put(result[1].trim());
						json.put(result[2].trim());
						json.put(result[3].trim());
						outboundTransactions.put(json);
					}
				}

				List<Object> inboundTransactionList = transactionJpaRepository.getToTransactionByAccountByIdBetween(transactionId-ACC_STATS_TXN_COUNT, transactionId);
				if(inboundTransactionList!=null && inboundTransactionList.size()>0){
					for(int i=0;i<inboundTransactionList.size();i++) {
						String[] result = (ow.writeValueAsString(inboundTransactionList.get(i))).replace("[", "").replace("]", "").split(",");
						String addr = result[0].replace("\"", "").trim();
						if (addr.isEmpty())
							continue;

						JSONArray json = new JSONArray();
						json.put(addr);
						json.put(result[1].trim());
						json.put(result[2].trim());
						json.put(result[3].trim());
						inboundTransactions.put(json);
					}
				}
			}

			DailyStatistics.getInstance().setStatistics(maxTransactions, sumTransactions, inboundTransactions, outboundTransactions, blocksMined);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception in calculating daily statistics");
		}

	}

	public void computeDashboardRTStatistics() {
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			Sort blockSort = new Sort(Sort.Direction.DESC, "blockNumber");
			Sort transactionSort = new Sort(Sort.Direction.DESC, "id");

			// populate recent blocks
			Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
			long transactionId = parserState.get().getTransactionId();
			long blockNumber = parserState.get().getBlockNumber();

			Page<Block> blockPage = blockJpaRepository.findByBlockNumberBetween(blockNumber-RT_BLK_COUNT_RETRIEVE, blockNumber, new PageRequest(0, 32, blockSort));
			List<Block> blockList = blockPage.getContent();
			JSONArray blockArray = new JSONArray();
			if(blockList != null && blockList.size()>0) {
				for(int i = 0; i < Math.min(blockList.size(), RT_BLK_COUNT_DISPLAY); i++) {
					Block block = blockList.get(i);
					JSONObject result = new JSONObject(ow.writeValueAsString(block));
					result.put("blockReward", RewardsCalculator.calculateReward(block.getBlockNumber()));
					result.remove("transactionList");
					blockArray.put(result);
				}
			}

			Page<Transaction> transactionPage = transactionJpaRepository.findByIdBetween(transactionId-RT_TXN_COUNT_RETRIEVE, transactionId, new PageRequest(0, 10, transactionSort));
			List<Transaction> transactionList = transactionPage.getContent();
			JSONArray transactionArray = new JSONArray();
			if(transactionList!=null && transactionList.size()>0) {
				for(int i = 0;i < Math.min(transactionList.size(), RT_TXN_COUNT_DISPLAY); i++) {
					Transaction transaction = transactionList.get(i);
					transactionArray.put(new JSONObject(ow.writeValueAsString(transaction)));
				}
			}

			long currentBlockchainHead = 0;
			Optional<ParserState> lastBlockRead = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCKCHAIN.getId());
			if(lastBlockRead.isPresent())
				currentBlockchainHead = lastBlockRead.get().getBlockNumber();

			// kpi's for recent block averaging window
			BigInteger difficultyAccumulator = BigInteger.ZERO;
			BigInteger nrgConsumedAccumulator = BigInteger.ZERO;
			BigInteger nrgLimitAccumulator = BigInteger.ZERO;
			BigInteger lastBlockReward = BigInteger.ZERO;

			long txnCount = 0L;
			long blkTimeAccumulator = 0;
			double averageDifficulty = 0 ;
			double averageBlockTime = 0;
			double hashRate = 0;
			double avgNrgConsumedPerBlock = 0;
			double avgNrgLimitPerBlock = 0;
			double transactionPerSecond = 0;
			long startBlock = 0;
			long endBlock = 0;
			long startTimestamp = 0;
			long endTimestamp = 0;
			int size = 0;

			if(blockList != null && blockList.size() > 1) {
				size = blockList.size();

				for (int i = 0; i < size; i++) {
					Block block = blockList.get(i);

					difficultyAccumulator = difficultyAccumulator.add(new BigInteger(block.getDifficulty(),16));
					txnCount += block.getNumTransactions();
					if(block.getBlockNumber() > 1) blkTimeAccumulator += (block.getBlockTime());

					nrgConsumedAccumulator = nrgConsumedAccumulator.add(BigInteger.valueOf(block.getNrgConsumed()));
					nrgLimitAccumulator = nrgLimitAccumulator.add(BigInteger.valueOf(block.getNrgLimit()));
				}

				averageDifficulty = difficultyAccumulator.doubleValue() / size;
				averageBlockTime = blkTimeAccumulator / (double)size;
				hashRate = new BigInteger(blockList.get(0).getDifficulty(),16).doubleValue() / averageBlockTime;
				avgNrgConsumedPerBlock = nrgConsumedAccumulator.doubleValue() / size;
				avgNrgLimitPerBlock = nrgLimitAccumulator.doubleValue() / size;

				long totalTime = (blockList.get(0).getBlockTimestamp() - blockList.get(size-1).getBlockTimestamp());
				if (totalTime > 0) transactionPerSecond = txnCount / totalTime;

				endBlock = blockList.get(0).getBlockNumber();
				startBlock = blockList.get(size-1).getBlockNumber();
				endTimestamp = blockList.get(0).getBlockTimestamp();
				startTimestamp = blockList.get(size-1).getBlockTimestamp();
				lastBlockReward = RewardsCalculator.calculateReward(blockList.get(0).getBlockNumber());
			}

			JSONObject metrics = new JSONObject();
			metrics.put("blockWindow",           size                 );
			metrics.put("hashRate",              hashRate             );
			metrics.put("endBlock",              endBlock             );
			metrics.put("targetBlockTime",       10                   );
			metrics.put("startBlock",            startBlock           );
			metrics.put("endTimestamp",          endTimestamp         );
			metrics.put("startTimestamp",        startTimestamp       );
			metrics.put("lastBlockReward",       lastBlockReward      );
			metrics.put("averageBlockTime",      averageBlockTime     );
			metrics.put("averageDifficulty",     averageDifficulty    );
			metrics.put("transactionPerSecond",  transactionPerSecond );
			metrics.put("currentBlockchainHead", currentBlockchainHead);

			metrics.put("peakTransactionsPerBlockInLast24hours", DailyStatistics.getInstance().getPeakTransaction() );
			metrics.put("totalTransactionsInLast24hours",        DailyStatistics.getInstance().getTotalTransaction());
			metrics.put("averageNrgConsumedPerBlock",            avgNrgConsumedPerBlock                             );
			metrics.put("averageNrgLimitPerBlock",               avgNrgLimitPerBlock                                );
			
			Statistics dashboardStatistics = Statistics.getInstance();
			dashboardStatistics.setTransactions(transactionArray);
			dashboardStatistics.setBlocks(blockArray);
			dashboardStatistics.setSBMetrics(metrics);
			
		} catch(Exception e) { e.printStackTrace(); }
	}

    @Value("${app.host}")
    private String host;
    AtomicInteger totalWs             = new AtomicInteger(0);
    AtomicInteger transportError      = new AtomicInteger(0);
    AtomicInteger sendLimitError      = new AtomicInteger(0);
	AtomicInteger closedAbnormally    = new AtomicInteger(0);
	AtomicInteger connectFailureError = new AtomicInteger(0);

    @Scheduled(fixedDelay = INTERVAL_STATS_MS_DAILY)
	public void webSocketDailyStatistics() {
    	//5 current WS(0)-HttpStream(5)-HttpPoll(0), 5 total, 0 closed abnormally (0 connect failure, 0 send limit, 0 transport error)
		totalWs.set(Integer.parseInt(webSocketMessageBrokerStats.getWebSocketSessionStatsInfo().split(",")[1].replace("total","").trim()));
		sendLimitError.set(Integer.parseInt(webSocketMessageBrokerStats.getWebSocketSessionStatsInfo().split(",")[3].replace("send limit","").trim()));
		transportError.set(Integer.parseInt(webSocketMessageBrokerStats.getWebSocketSessionStatsInfo().split(",")[4].replace("transport error)","").trim()));
		connectFailureError.set(Integer.parseInt(webSocketMessageBrokerStats.getWebSocketSessionStatsInfo().split(",")[2].split("\\(")[0].replace("closed abnormally","").trim()));
		closedAbnormally.set(connectFailureError.intValue()+sendLimitError.intValue()+transportError.intValue());

		Metrics.gauge("aion.websocket.totalws",             Collections.singletonList(Tag.of("host", host)), totalWs);
		Metrics.gauge("aion.websocket.transporterror",      Collections.singletonList(Tag.of("host", host)), transportError);
		Metrics.gauge("aion.websocket.sendlimiterror",      Collections.singletonList(Tag.of("host", host)), sendLimitError);
		Metrics.gauge("aion.websocket.closedAbnormally",    Collections.singletonList(Tag.of("host", host)), closedAbnormally);
		Metrics.gauge("aion.websocket.connectfailureerror", Collections.singletonList(Tag.of("host", host)), connectFailureError);
    }
}