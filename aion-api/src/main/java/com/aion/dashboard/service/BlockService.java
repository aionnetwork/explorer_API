package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Balance;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.repository.BalanceJpaRepository;
import com.aion.dashboard.repository.BlockJpaRepository;
import com.aion.dashboard.repository.ParserStateJpaRepository;
import com.aion.dashboard.types.ParserStateType;
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

import java.util.List;
import java.util.Optional;

@Component
public class BlockService {

	@Autowired
	private ParserStateJpaRepository parserStateJpaRepository;

	@Autowired
    private BalanceJpaRepository balanceJpaRepository;

	@Autowired
	private BlockJpaRepository blockJpaRepository;

	public static final Long BLOCK_LIMIT = 250L;

	@Cacheable(CacheConfig.BLOCK_LIST)
	public String getBlockList(int pageNumber, int pageSize) throws Exception {
		if(pageSize>300)
			return "";
		try {
			Optional<ParserState> blockParserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
			JSONArray blockArray = new JSONArray();
			JSONObject blockObject = new JSONObject();
			Sort sort = new Sort(Sort.Direction.DESC, "blockNumber");

			pageSize = (int) Math.min(pageSize, BLOCK_LIMIT);

			if(blockParserState.isPresent()) {
				long blockNumber = blockParserState.get().getBlockNumber();
				Page<Block> blockPage = blockJpaRepository.findByBlockNumberBetween(blockNumber-pageSize*(1+pageNumber), blockNumber-pageSize*pageNumber, new PageRequest(0, pageSize, sort));

				//Page<Block> blockPage = blockJpaRepository.findByBlockNumberBetween(blockNumber - BLOCK_LIMIT, blockNumber, new PageRequest(pageNumber, pageSize, sort));
				//Page<Block> blockPage = blockJpaRepository.findAll( new PageRequest(pageNumber, pageSize, sort));
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				List<Block> blockList = blockPage.getContent();

				if(blockList != null && blockList.size() > 0) {
					for (Block block : blockList) {
						JSONObject result = new JSONObject(ow.writeValueAsString(block));
						result.remove("transactionList");
						result.put("blockReward", RewardsCalculator.calculateReward(block.getBlockNumber()));
						blockArray.put(result);
					}

					JSONObject pageObject = new JSONObject();
					pageObject.put("totalElements", blockParserState.get().getBlockNumber());
					//pageObject.put("totalPages", blockPage.getTotalPages());
					pageObject.put("totalPages", blockParserState.get().getBlockNumber()/pageSize);

					pageObject.put("number", pageNumber);
					pageObject.put("size", pageSize);

					blockObject.put("content", blockArray);
					blockObject.put("page", pageObject);
				}

				// If the ResultSet is Null
				if(blockArray.length() == 0) {
					blockArray = new JSONArray();
					blockObject.put("content", blockArray);
				}

				return blockObject.toString();
			}

			throw new Exception();
		} catch(Exception e) { throw e; }
	}



	@Cacheable(CacheConfig.BLOCKS_MINED_BY_ADDRESS)
	public String getBlocksMinedByAddress(String searchParam, int blockPageNumber, int blockPageSize) throws Exception {
	    try {
			if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject blockObject = new JSONObject();
            JSONArray blockArray = new JSONArray();

            // find by block hash
            if (Utility.validHex(searchParam)) {

                // blocks mined
                Balance balance = balanceJpaRepository.findByAddress(searchParam);
                if(balance != null) {
                    long blockNumber = balance.getLastBlockNumber();
					blockPageSize = (int) Math.min(blockPageSize, BLOCK_LIMIT);
					Page<Block> blockPageList = blockJpaRepository.findByMinerAddress(searchParam, new PageRequest(blockPageNumber, blockPageSize));
                    if (blockPageList != null) {
                        List<Block> blockList = blockPageList.getContent();
                        if (blockList != null && blockList.size() > 0) {
                            for (int i = 0; i < blockList.size(); i++) {
                                JSONObject result = new JSONObject(ow.writeValueAsString(blockList.get(i)));
                                result.remove("transactionList");
                                blockArray.put(result);
                            }

                            JSONObject pageObject = new JSONObject();
                            pageObject.put("totalElements", blockPageList.getTotalElements());
                            pageObject.put("totalPages", blockPageList.getTotalPages());
                            pageObject.put("number", blockPageNumber);
                            pageObject.put("size", blockPageSize);

                            blockObject.put("content", blockArray);
                            blockObject.put("page", pageObject);

                            return blockObject.toString();
						} else return new JSONObject().put("rel", JSONObject.NULL).toString();
                    }
                }
            }

            throw new Exception();
        } catch(Exception e) { throw e; }
	}

	@Cacheable(CacheConfig.BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER)
	public String getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(String searchParam) throws Exception {
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			JSONArray blockArray = new JSONArray();

			// find in block hash
			if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");

			if(Utility.validHex(searchParam)) {
				// block master
				Block block = blockJpaRepository.findByBlockHash(searchParam);
				if(block != null) {
					JSONObject result = new JSONObject(ow.writeValueAsString(block));
					result.put("blockReward", RewardsCalculator.calculateReward(block.getBlockNumber()));
					JSONArray transactionList = new JSONArray(result.get("transactionList").toString());
					result.put("transactionList", transactionList);
					blockArray.put(result);
					return new JSONObject().put("content", blockArray).toString();
				} else return new JSONObject().put("rel", JSONObject.NULL).toString();
			}

			// find by block number
			else if(Utility.validLong(searchParam)) {
				// block master
				Block block = blockJpaRepository.findByBlockNumber(Long.parseLong(searchParam));
				blockArray = new JSONArray();
				if(block != null) {
					JSONObject result = new JSONObject(ow.writeValueAsString(block));
					result.put("blockReward", RewardsCalculator.calculateReward(block.getBlockNumber()));
					JSONArray transactionList = new JSONArray(result.get("transactionList").toString());
					result.put("transactionList", transactionList);
					blockArray.put(result);
					return new JSONObject().put("content", blockArray).toString();
				} else return new JSONObject().put("rel", JSONObject.NULL).toString();
			}

            throw new Exception();
		} catch(Exception e) { throw e; }
	}
}
