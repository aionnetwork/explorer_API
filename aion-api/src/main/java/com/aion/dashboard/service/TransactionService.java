package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Event;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.entities.Transfer;
import com.aion.dashboard.repository.EventJpaRepository;
import com.aion.dashboard.repository.ParserStateJpaRepository;
import com.aion.dashboard.repository.TransactionJpaRepository;
import com.aion.dashboard.repository.TransferJpaRepository;
import com.aion.dashboard.types.ParserStateType;
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

@SuppressWarnings("Duplicates")
@Component
public class TransactionService {

	@Autowired
	private EventJpaRepository eventJpaRepository;

	@Autowired
	private TransferJpaRepository transferJpaRepository;

	@Autowired
	private ParserStateJpaRepository parserStateJpaRepository;

	@Autowired
	private TransactionJpaRepository transactionJpaRepository;

	public static final Long TRANSACTION_LIMIT = 250L;

    @Cacheable(CacheConfig.TRANSACTION_LIST)
    public String getTransactionList(int pageNumber, int pageSize) throws Exception {
        if(pageSize>300)
            return "";

        try {
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
            JSONArray transactionArray = new JSONArray();
            JSONObject transactionObject = new JSONObject();
            Sort sort = new Sort(Sort.Direction.DESC, "id");

            if(parserState.isPresent()) {
                pageSize = (int) Math.min(pageSize, TRANSACTION_LIMIT);
                long transactionId = parserState.get().getTransactionId();
				Page<Transaction> transactionPage = transactionJpaRepository.findByIdBetween(transactionId-pageSize*(1 + pageNumber), transactionId-pageSize*pageNumber, new PageRequest(0, pageSize));

				//Page<Transaction> transactionPage = transactionJpaRepository.findByIdBetween(transactionId - TRANSACTION_LIMIT, transactionId, new PageRequest(pageNumber, pageSize, sort));
                //Page<Transaction> transactionPage = transactionJpaRepository.findAll(new PageRequest(pageNumber, pageSize, sort));
                List<Transaction> transactionList = transactionPage.getContent();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                if(transactionList != null && transactionList.size() > 0) {
                    for(int i = 0; i < transactionList.size(); i++) {
                        Transaction transaction = transactionList.get(i);
                        JSONObject result = new JSONObject(ow.writeValueAsString(transaction));
                        transactionArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", parserState.get().getTransactionId());
                    //pageObject.put("totalPages", transactionPage.getTotalPages());
                    pageObject.put("totalPages", parserState.get().getTransactionId()/pageSize);
                    pageObject.put("number", pageNumber);
                    pageObject.put("size", pageSize);

                    transactionObject.put("content", transactionArray);
                    transactionObject.put("page", pageObject);
                }
            }

            // If the ResultSet is Null
            if (transactionArray.length() == 0) {
                transactionArray = new JSONArray();
                transactionObject.put("content", transactionArray);
            }

            return transactionObject.toString();
        } catch(Exception e) { throw e; }
    }


    @Cacheable(CacheConfig.TRANSACTION_DETAIL_FROM_TRANSACTION_HASH)
	public String getTransactionDetailsByTransactionHash(String searchParam, int pageNumber, int pageSize) throws Exception{
		try {
			if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			JSONArray transactionArray = new JSONArray();

			// find in block hash
			if(Utility.validHex(searchParam)) {
				// transaction cache
				pageSize = (int) Math.min(pageSize, 1000L);
				Transaction transaction = transactionJpaRepository.getTransactionByTransactionHash(searchParam);
				if(transaction != null) {
					JSONObject result = new JSONObject(ow.writeValueAsString(transaction));

					// Gathering the Events of the Transaction
					result.put("events", new JSONObject(getTransactionEventsByTransactionID(transaction.getId(), pageNumber, pageSize)));

					transactionArray.put(result);
					return new JSONObject().put("content", transactionArray).toString();
				}
			}

			throw new Exception();
		} catch(Exception e) { throw e; }
	}

	@Cacheable(CacheConfig.TRANSACTIONS_BY_ADDRESS)
	public String getTransactionsByAddress(String accountAddress, String tokenAddress, int transactionPageNumber, int transactionPageSize) throws Exception {
		transactionPageSize = (int) Math.min(transactionPageSize, TRANSACTION_LIMIT);
		if(accountAddress.startsWith("0x")) accountAddress = accountAddress.replace("0x", "");
		if(tokenAddress != null && !tokenAddress.trim().isEmpty()) {
			if(tokenAddress.startsWith("0x")) tokenAddress = tokenAddress.replace("0x", "");
			return getTransactionsByAddressForToken(accountAddress, tokenAddress, transactionPageNumber, transactionPageSize);
		}
		else return getTransactionsByAddressForAion(accountAddress, transactionPageNumber, transactionPageSize);
	}


	// Internal Methods
	@Cacheable(CacheConfig.TRANSACTION_EVENTS_BY_TRANSACTION_ID)
	private String getTransactionEventsByTransactionID(Long txnId, int pageNumber, int pageSize) throws Exception {
		try {
			// Gathering the Events of the Transaction
			pageSize = (int) Math.min(pageSize, TRANSACTION_LIMIT);
			Page<Event> eventPage = eventJpaRepository.findByTransactionId(txnId, new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "eventTimestamp")));
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			JSONObject eventObject = new JSONObject();
			JSONArray eventArray = new JSONArray();
			if(eventPage != null) {
				List<Event> eventList = eventPage.getContent();
				if(eventList != null && eventList.size() > 0) {
					for(Event event: eventList) {
						eventArray.put(new JSONObject(ow.writeValueAsString(event)));
					}

					JSONObject pageObject = new JSONObject();
					pageObject.put("totalElements", eventPage.getTotalElements());
					pageObject.put("totalPages", eventPage.getTotalPages());
					pageObject.put("number", pageNumber);
					pageObject.put("size", pageSize);

					eventObject.put("content", eventArray);
					eventObject.put("page", pageObject);
				}
			}
			if(eventArray.length() == 0) return new JSONObject().put("rel", JSONObject.NULL).toString();  // If there are no Events

			eventObject.put("content", eventArray);
			return eventObject.toString();
		} catch(Exception e) {
			throw new Exception();
		}
	}

	@Cacheable(CacheConfig.TRANSACTIONS_BY_ADDRESS_FOR_AION)
	private String getTransactionsByAddressForAion(String accountAddress, int transactionPageNumber, int transactionPageSize) throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		JSONObject transactionObject = new JSONObject();
		JSONArray transactionArray = new JSONArray();

		// find by block hash
		if(Utility.validHex(accountAddress)) {

			transactionPageSize = (int) Math.min(transactionPageSize, TRANSACTION_LIMIT);

			// transactions
			Page<Transaction> transactionPage = transactionJpaRepository.findTransactionsByAddress(accountAddress, new PageRequest(transactionPageNumber, transactionPageSize));
			if (transactionPage != null) {
				List<Transaction> transactionList = transactionPage.getContent();
				if (transactionList != null && transactionList.size() > 0) {
					for (int i = 0; i < transactionList.size(); i++) {
						JSONObject result = new JSONObject(ow.writeValueAsString(transactionList.get(i)));
						transactionArray.put(result);
					}

					JSONObject pageObject = new JSONObject();
					pageObject.put("totalElements", transactionPage.getTotalElements());
					pageObject.put("totalPages", transactionPage.getTotalPages());
					pageObject.put("number", transactionPageNumber);
					pageObject.put("size", transactionPageSize);

					transactionObject.put("content", transactionArray);
					transactionObject.put("page", pageObject);

					return transactionObject.toString();
				} else return new JSONObject().put("rel", JSONObject.NULL).toString();
			}
		}

		throw new Exception();
	}

	@Cacheable(CacheConfig.TRANSACTIONS_BY_ADDRESS_FOR_TOKEN)
	private String getTransactionsByAddressForToken(String accountAddress, String tokenAddress, int transactionPageNumber, int transactionPageSize) throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		JSONObject transactionObject = new JSONObject();
		JSONArray transactionArray = new JSONArray();

		// find by block hash
		if (Utility.validHex(accountAddress)) {

			transactionPageSize = (int) Math.min(transactionPageSize, TRANSACTION_LIMIT);

			// transactions
			Page<Transfer> transferPage = transferJpaRepository.findTransfersByContractAndHolder(tokenAddress, accountAddress, new PageRequest(transactionPageNumber, transactionPageSize));
			if (transferPage != null) {
				List<Transfer> transferList = transferPage.getContent();
				if (transferList != null && transferList.size() > 0) {
					for (Transfer transfer : transferList) {
						JSONObject result = new JSONObject(ow.writeValueAsString(transfer));

						// Getting the Transaction Hash of each Transfer
						Optional<Transaction> transaction = transactionJpaRepository.findById(transfer.getTransactionId());
						transaction.ifPresent(txn -> result.put("transactionHash", txn.getTransactionHash()));

						transactionArray.put(result);
					}

					JSONObject pageObject = new JSONObject();
					pageObject.put("totalElements", transferPage.getTotalElements());
					pageObject.put("totalPages", transferPage.getTotalPages());
					pageObject.put("number", transactionPageNumber);
					pageObject.put("size", transactionPageSize);

					transactionObject.put("content", transactionArray);
					transactionObject.put("page", pageObject);

					return transactionObject.toString();
				} else return new JSONObject().put("rel", JSONObject.NULL).toString();
			}
		}

		throw new Exception();
	}
}
