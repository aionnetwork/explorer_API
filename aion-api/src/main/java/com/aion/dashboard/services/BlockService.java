package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repositories.AccountJpaRepository;
import com.aion.dashboard.repositories.BlockJpaRepository;
import com.aion.dashboard.repositories.TransactionJpaRepository;
import com.aion.dashboard.specification.BlockSpec;
import com.aion.dashboard.utility.RewardsCalculator;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.aion.dashboard.utility.Logging.traceLogStartAndEnd;

@Component
public class BlockService {

	@Autowired
	private TransactionJpaRepository txnRepo;

	@Autowired
	private AccountJpaRepository acctRepo;

	@Autowired
	private BlockJpaRepository blkRepo;

	private static  final String CONTENT="content";
	private static final String TRANSACTION_HASH="transactionHashes";

	private Sort sortDesc(){
		return new Sort( Sort.Direction.DESC,"blockTimestamp");
	}
	@Cacheable(CacheConfig.BLOCK_LIST)
	public String getBlockList(long start,
							   long end,
							   int pageNumber,
							   int pageSize) throws Exception {

		JSONArray blkArray = new JSONArray();
		JSONObject blkObject = new JSONObject();

        var zdtStart = Instant.ofEpochSecond(start).atZone( ZoneId.of("UTC"));
        var zdtEnd = Instant.ofEpochSecond(end).atZone( ZoneId.of("UTC"));



		Utility.validateBlkListPeriod(zdtStart, zdtEnd);

		Page<Block> blksPage = blkRepo.findAll(BlockSpec.checkTime(zdtStart, zdtEnd), PageRequest.of(pageNumber,pageSize, sortDesc()));


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

	@Cacheable(CacheConfig.BLOCKS_MINED_BY_ADDRESS)
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
            Account account = acctRepo.findByAddress(accountAddress);
            if (account != null) {

                Page<Block> blksPage;
                blksPage = blkRepo.findAll(BlockSpec.checkTime(startZDT, endZDT).and(BlockSpec.isMiner(accountAddress)), pageRequest);
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

	@Cacheable(CacheConfig.BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER)
	public String getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(String searchParam) throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		JSONArray blockArray = new JSONArray();

		// find in block hash
		if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");

		if(Utility.validHex(searchParam)) {
			// block master
			Block block = blkRepo.findByBlockHash(searchParam);
			if(block != null) {
				JSONObject result = new JSONObject(ow.writeValueAsString(block));

				JSONArray txnHashes = new JSONArray(result.getString(TRANSACTION_HASH));
				JSONArray txnList = new JSONArray();
				for(int i = 0; i < txnHashes.length(); i++) {
					Transaction txn = txnRepo.findByTransactionHash(txnHashes.getString(i));
					String txnString = "[" +
							txn.getTransactionHash() + "," +
							txn.getFromAddr() + "," +
							txn.getToAddr() + "," +
							Utility.toAion(txn.getValue()).doubleValue() + "," +
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
			Block block = blkRepo.findByBlockNumber(Long.parseLong(searchParam));
			blockArray = new JSONArray();
			if(block != null) {
				JSONObject result = new JSONObject(ow.writeValueAsString(block));

				JSONArray txnHashes = new JSONArray(result.getString(TRANSACTION_HASH));
				JSONArray txnList = new JSONArray();
				for(int i = 0; i < txnHashes.length(); i++) {
					Transaction txn = txnRepo.findByTransactionHash(txnHashes.getString(i));
					String txnString = "[" +
							txn.getTransactionHash() + "," +
							txn.getFromAddr() + "," +
							txn.getToAddr() + "," +
							Utility.toAion(txn.getValue()).doubleValue() + "," +
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
}