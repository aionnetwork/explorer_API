package com.aion.dashboard.services.mysql;

import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repositories.BlockJpaRepository;
import com.aion.dashboard.repositories.ParserStateJpaRepository;
import com.aion.dashboard.services.AccountService;
import com.aion.dashboard.services.BlockService;
import com.aion.dashboard.services.TransactionService;
import com.aion.dashboard.specification.BlockSpec;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.RewardsCalculator;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.aion.dashboard.utility.Logging.traceLogStartAndEnd;

@Component
public class BlockImpMysql implements BlockService {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private BlockJpaRepository blockJpaRepository;

	private static  final String CONTENT="content";
	private static final String TRANSACTION_HASH="transactionHashes";

	private Sort sortDesc(){
		return new Sort( Sort.Direction.DESC,"blockTimestamp");
	}

	@Override
	public String getBlockList(long start,
							   long end,
							   int pageNumber,
							   int pageSize) throws Exception {

		JSONArray blkArray = new JSONArray();
		JSONObject blkObject = new JSONObject();

        var zdtStart = Instant.ofEpochSecond(start).atZone( ZoneId.of("UTC"));
        var zdtEnd = Instant.ofEpochSecond(end).atZone( ZoneId.of("UTC"));



		Utility.validateBlkListPeriod(zdtStart, zdtEnd);

		Page<Block> blksPage = blockJpaRepository.findAll(BlockSpec.checkTime(zdtStart, zdtEnd), PageRequest.of(pageNumber,pageSize, sortDesc()));


		List<Block> blksList = blksPage.getContent();
		if(!blksList.isEmpty()) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			for(Block blk: blksList) {
				JSONObject result = new JSONObject(ow.writeValueAsString(blk));
				blkArray.put(result);
			}

			JSONObject pageObject = new JSONObject();
			pageObject.put("totalElements", blksPage.getTotalElements());
			pageObject.put("totalPages", blksPage.getTotalPages());
			pageObject.put("number", pageNumber);
			pageObject.put("size", pageSize);
            pageObject.put("start", start);
            pageObject.put("end", end);
            blkObject.put(CONTENT, blkArray);
			blkObject.put("page", pageObject);
		}

		// If the ResultSet is Null
		if (blkArray.length() == 0) {
			blkArray = new JSONArray();
			blkObject.put(CONTENT, blkArray);
		}

		return blkObject.toString();
	}

	@Override
	public String getBlocksMinedByAddress(long start,
										  long end,
										  int pageNumber,
										  int pageSize,
										  String accountAddress) throws Exception {
		if(accountAddress.startsWith("0x"))
			accountAddress = accountAddress.replace("0x", "");


		if(Utility.isValidAddress(accountAddress)) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject blockObject = new JSONObject();
            JSONArray blockArray = new JSONArray();

            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortDesc());
            ZonedDateTime startZDT = Instant.ofEpochSecond(start).atZone(ZoneId.of("UTC"));
            ZonedDateTime endZDT = Instant.ofEpochSecond(end).atZone(ZoneId.of("UTC"));
            

            traceLogStartAndEnd(startZDT, startZDT, "Call to getBlocksMinedByAddress");

            // blocks mined
            Account account = accountService.findByAddress(accountAddress);
            if (account != null) {

                Page<Block> blksPage;
                blksPage = blockJpaRepository.findAll(BlockSpec.checkTime(startZDT, endZDT).and(BlockSpec.isMiner(accountAddress)), pageRequest);
                List<Block> blksList = blksPage.getContent();
                for (Block aBlksList : blksList) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(aBlksList));
                    blockArray.put(result);
                }

                // If the ResultSet is Null
                if (blockArray.length() == 0) {
                    blockObject.put(CONTENT, blockArray);
                } else {
                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", blksPage.getTotalElements());
                    pageObject.put("totalPages", blksPage.getTotalPages());
                    pageObject.put("number", pageNumber);
                    pageObject.put("size", pageSize);

                    pageObject.put("start", start);
                    pageObject.put("end", end);
                    blockObject.put(CONTENT, blockArray);
                    blockObject.put("page", pageObject);
                }
                return blockObject.toString();
            }
        }

		throw new Exception();
	}

	@Override
	public String getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(String searchParam) throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		JSONArray blockArray = new JSONArray();

		// find in block hash
		if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");

		if(Utility.validHex(searchParam)) {
			// block master
			Block block = blockJpaRepository.findByBlockHash(searchParam);
			if(block != null) {
				JSONObject result = new JSONObject(ow.writeValueAsString(block));

				JSONArray txnHashes = new JSONArray(result.getString(TRANSACTION_HASH));
				JSONArray txnList = new JSONArray();
				for(int i = 0; i < txnHashes.length(); i++) {
					Transaction txn = transactionService.findByTransactionHash(txnHashes.getString(i));
					String txnString = "[" +
							txn.getTransactionHash() + "," +
							txn.getFromAddr() + "," +
							txn.getToAddr() + "," +
							Utility.toAion(txn.getValue()) + "," +
							txn.getBlockTimestamp() + "," +
							txn.getBlockNumber() +
							"]";
					JSONArray txnArray = new JSONArray(txnString);
					txnList.put(txnArray);
				}
				result.put("blockReward", RewardsCalculator.calculateReward(block.getBlockNumber()));
				result.put("transactionList", txnList);
				result.remove(TRANSACTION_HASH);

				blockArray.put(result);
			}

			// If the ResultSet is Null
			if(blockArray.length() == 0) {
				blockArray = new JSONArray();
			}

			return new JSONObject().put(CONTENT, blockArray).toString();
		}

		// find by block number
		else if(Utility.validLong(searchParam)) {
			// block master
			Block block = blockJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
			blockArray = new JSONArray();
			if(block != null) {
				JSONObject result = new JSONObject(ow.writeValueAsString(block));

				JSONArray txnHashes = new JSONArray(result.getString(TRANSACTION_HASH));
				JSONArray txnList = new JSONArray();
				for(int i = 0; i < txnHashes.length(); i++) {
					Transaction txn = transactionService.findByTransactionHash(txnHashes.getString(i));
					String txnString = "[" +
							txn.getTransactionHash() + "," +
							txn.getFromAddr() + "," +
							txn.getToAddr() + "," +
							Utility.toAion(txn.getValue()) + "," +
							txn.getBlockTimestamp() + "," +
							txn.getBlockNumber() +
							"]";
					JSONArray txnArray = new JSONArray(txnString);
					txnList.put(txnArray);
				}
				result.put("blockReward", RewardsCalculator.calculateReward(block.getBlockNumber()));
				result.put("transactionList", txnList);
				result.remove(TRANSACTION_HASH);

				blockArray.put(result);
			}

			// If the ResultSet is Null
			if(blockArray.length() == 0) {
				blockArray = new JSONArray();
			}

			return new JSONObject().put(CONTENT, blockArray).toString();
		}

		throw new Exception();
	}

	@Autowired
    private	ParserStateJpaRepository psRepo;

	@Override
	public String getBlockNumber() {

		JSONObject output = new JSONObject();
		final String errorKey = "error";
		final String dataKey = "data";
		try {
			var state = psRepo.findById(ParserStateType.HEAD_BLOCKCHAIN.getId());


			if (state.isPresent()) {
				output.put(dataKey, state.get().getBlockNumber());
				output.put(errorKey, false);
			} else {
				output.put(errorKey, true);
			}
			return output.toString();
		}catch (Exception e){
			return 	output.put(errorKey, true).toString();

		}
	}

	@Override
	public Page<Block> findByDayAndMonthAndYear(int day, int month, int year, Pageable pageable) {
		return blockJpaRepository.findByDayAndMonthAndYear(day,month,year, pageable);
	}

	@Override
	public Block findByBlockNumber(Long blockNumber) {
		return blockJpaRepository.findByBlockNumber(blockNumber);
	}

	@Override
	public Block findByBlockHash(String blockHash) {
		return blockJpaRepository.findByBlockHash(blockHash);
	}

	@Override
	public long countByBlockTimestampBetween(long start, long end) {
		return blockJpaRepository.countByBlockTimestampBetween(start,end);
	}

	@Override
	public List<Object> findAvgAndCountForAddressBetweenTimestamp(long start, long end) {
		return blockJpaRepository.findAvgAndCountForAddressBetweenTimestamp(start,end);
	}


}