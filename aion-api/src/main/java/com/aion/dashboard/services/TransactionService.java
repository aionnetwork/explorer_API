package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Event;
import com.aion.dashboard.entities.InternalTransfer;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenTransfers;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repositories.EventJpaRepository;
import com.aion.dashboard.repositories.InternalTransferJpaRepository;
import com.aion.dashboard.repositories.TokenJpaRepository;
import com.aion.dashboard.repositories.TokenTransfersJpaRepository;
import com.aion.dashboard.repositories.TransactionJpaRepository;
import com.aion.dashboard.specification.TransactionSpec;
import com.aion.dashboard.specification.TransferSpec;
import com.aion.dashboard.utility.Logging;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@SuppressWarnings("Duplicates")
@Component
public class TransactionService {

	@Autowired
	private EventJpaRepository evtRepo;

	@Autowired
	private TokenJpaRepository tknRepo;

	@Autowired
	private TransactionJpaRepository txnRepo;

	@Autowired
	private TokenTransfersJpaRepository tknTxfRepo;

	@Autowired
	private InternalTransferJpaRepository internalTransferJpaRepository;

	@Autowired
	private BlockService blockService;


	private static final String VALUE="value";
	private static final String CONTENT="content";
	private static final String TOTAL_ELEMENTS="totalElements";
	private static final String TOTAL_PAGES="totalPages";
	private static final String NUMBER="number";
	private static final String START="start";
	private static final String SIZE="size";
	private static final String END="end";
	private static final String PAGE="page";

	private final Sort sortAsc() {
		return new Sort(Sort.Direction.ASC,"blockTimestamp" );
	}
	private Sort sortDesc1() {
		return new Sort(Sort.Direction.DESC,"blockTimestamp" );
	}
	private Sort sortDesc2() {
		return new Sort(Sort.Direction.DESC,"transferTimestamp" );
	}

	public String getTransactionDetailsByTransactionHash(String searchParam,
														 int pageNumber,
														 int pageSize) throws Exception{
		if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		JSONArray txnArr = new JSONArray();

		if(Utility.validHex(searchParam)) {
			Transaction txn = txnRepo.findByTransactionHash(searchParam);
			if(txn != null) {
				JSONObject result = new JSONObject(ow.writeValueAsString(txn));

				// Converting Value from Wei to Aion
				result.put(VALUE, Utility.toAion(txn.getValue()));

				// Gathering the Events of the Transaction
				result.put("events", new JSONObject(getTransactionEventsByTransactionHash(txn.getTransactionHash(), pageNumber, pageSize)));

				// Correcting the Transaction Details with TokenTransfers information if related to a TokenTransfers
				if(!txn.getTransactionLog().equals("[]")) {
					List<TokenTransfers> tknTxfsList = tknTxfRepo.findByTransactionHash(txn.getTransactionHash());
					if (!tknTxfsList.isEmpty()) {
						Token tkn = tknRepo.findByContractAddr(tknTxfsList.stream().findAny().map(t->t.getContractAddr()).orElse(""));
						JSONArray transfersArray = new JSONArray();
                        if (tkn != null) {
                            for (TokenTransfers tknTxf : tknTxfsList) {
                                JSONObject object = new JSONObject();
                                object.put("tokenName", tkn.getName());
                                object.put("tokenSymbol", tkn.getSymbol());
                                object.put(VALUE, tknTxf.getRawValue());
                                object.put("to", tknTxf.getToAddr());
                                object.put("from", tknTxf.getFromAddr());
                                transfersArray.put(object);
                            }
						}
						result.put("tokenTransfers", transfersArray);

					}
				}

				txnArr.put(result);
				return new JSONObject().put(CONTENT, txnArr).toString();
			}
		}

		throw new Exception();
	}

	public String getTransactionList(int pageNumber,
									 int pageSize,
									 long startTime,
									 long endTime) throws Exception {
		var ldtStart = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("UTC"));
		var ldtEnd = Instant.ofEpochSecond(endTime).atZone(ZoneId.of("UTC"));
		Logging.traceLogStartAndEnd(ldtStart,ldtEnd, "Call to getTransactionsByAddressForList");
        Utility.validateTxListPeriod(ldtStart, ldtEnd);
        Page<Transaction> page =txnRepo.findAll(TransactionSpec.checkTime(ldtStart, ldtEnd), PageRequest.of(pageNumber, pageSize, sortDesc1()));
		List<Transaction> txnsList = page.getContent();
		JSONArray txnArray = new JSONArray();
		JSONObject txnObject = new JSONObject();
		if(txnsList != null && !txnsList.isEmpty()) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			for(Transaction txn: txnsList) {
				JSONObject result = new JSONObject(ow.writeValueAsString(txn));

				// Converting Value from Wei to Aion
				result.put(VALUE, Utility.toAion(txn.getValue()));
				txnArray.put(result);
			}


			JSONObject pageObject = new JSONObject();
			pageObject.put(TOTAL_ELEMENTS, page.getTotalElements());
			pageObject.put(TOTAL_PAGES, page.getTotalPages());
			pageObject.put(NUMBER, pageNumber);
			pageObject.put(SIZE, pageSize);
			pageObject.put(START, startTime);
			pageObject.put(END, endTime);

			txnObject.put(CONTENT, txnArray);
			txnObject.put(PAGE, pageObject);
		}

		// If the ResultSet is Null
		if (txnArray.length() == 0) {
			txnArray = new JSONArray();
			txnObject.put(CONTENT, txnArray);
		}

		return txnObject.toString();



	}


	public String getTransactionsByAddressForToken(int pageNumber,
												   int pageSize,
												   long timeStampStart,
												   long timeStampEnd,
												   String address,
												   String tokenAddress) throws Exception {
		if(address.startsWith("0x")) address = address.replace("0x", "");
		if(tokenAddress.startsWith("0x")) tokenAddress = tokenAddress.replace("0x", "");

		if(Utility.isValidAddress(address) && Utility.isValidAddress(tokenAddress)) {
            ZonedDateTime dateStart = Instant.ofEpochSecond(timeStampStart).atZone(ZoneId.of("UTC"));
            ZonedDateTime dateEnd = Instant.ofEpochSecond(timeStampEnd).atZone(ZoneId.of("UTC"));
            Logging.traceLogStartAndEnd(dateStart, dateEnd, "Call to getTransactionsByAddressForToken");

            boolean sameYear = dateStart.getYear() == dateEnd.getYear();
            PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDesc2());

            var page = sameYear ?
                    tknTxfRepo.findAll(TransferSpec.hasAddr(address).and(TransferSpec.checkTime(dateStart.getYear(), dateStart.getMonthValue(), dateEnd.getMonthValue(), timeStampStart, timeStampEnd)).and(TransferSpec.tokenIs(tokenAddress)), pageable)
                    : tknTxfRepo.findAll(TransferSpec.hasAddr(address).and(TransferSpec.checkTime(dateStart.getYear(), dateEnd.getYear(), timeStampStart, timeStampEnd)).and(TransferSpec.tokenIs(tokenAddress)), pageable);

            List<TokenTransfers> txnsList = page.getContent();
            JSONArray txnArray = new JSONArray();
            JSONObject txnObject = new JSONObject();
            if (txnsList != null && !txnsList.isEmpty()) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                for (TokenTransfers txn : txnsList) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(txn));
                    txnArray.put(result);
                }

                JSONObject pageObject = new JSONObject();
                pageObject.put(TOTAL_ELEMENTS, page.getTotalElements());
                pageObject.put(TOTAL_PAGES, page.getTotalPages());
                pageObject.put(NUMBER, pageNumber);
                pageObject.put(SIZE, pageSize);
                pageObject.put(START, timeStampStart);
                pageObject.put(END, timeStampEnd);

                txnObject.put(CONTENT, txnArray);
                txnObject.put(PAGE, pageObject);
            }

            // If the ResultSet is Null
            if (txnArray.length() == 0) {
                txnArray = new JSONArray();
                txnObject.put(CONTENT, txnArray);
            }

            return txnObject.toString();
        }

		throw new Exception();
	}

	public String getTransactionsByAddressForNative(int pageNumber,
													int pageSize,
													long timeStampStart,
													long timeStampEnd,
													String address) throws Exception {
		if(address.startsWith("0x")) address = address.replace("0x", "");

		if(Utility.isValidAddress(address)) {
			var dateStart = Instant.ofEpochSecond(timeStampStart).atZone(ZoneId.of("UTC"));
			var dateEnd = Instant.ofEpochSecond(timeStampEnd).atZone(ZoneId.of("UTC"));

            Logging.traceLogStartAndEnd(dateStart,dateEnd, "Call to getTransactionsByAddressForNative");

			var page = txnRepo.findAll(TransactionSpec.hasAddr(address).and(TransactionSpec.checkTime(dateStart, dateEnd)), PageRequest.of(pageNumber, pageSize, sortDesc1()));
			List<Transaction> txnsList = page.getContent();
			JSONArray txnArray = new JSONArray();
			JSONObject txnObject = new JSONObject();
			if (txnsList != null && !txnsList.isEmpty()) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				for (Transaction txn : txnsList) {
					JSONObject result = new JSONObject(ow.writeValueAsString(txn));

					// Converting Value from Wei to Aion
					result.put(VALUE, Utility.toAion(txn.getValue()));
					txnArray.put(result);
				}

				JSONObject pageObject = new JSONObject();
				pageObject.put(TOTAL_ELEMENTS, page.getTotalElements());
				pageObject.put(TOTAL_PAGES, page.getTotalPages());
				pageObject.put(NUMBER, pageNumber);
				pageObject.put(SIZE, pageSize);
				pageObject.put(START, timeStampStart);
				pageObject.put(END, timeStampEnd);


				txnObject.put(CONTENT, txnArray);
				txnObject.put(PAGE, pageObject);
			}

			// If the ResultSet is Null
			if (txnArray.length() == 0) {
				txnArray = new JSONArray();
				txnObject.put(CONTENT, txnArray);
			}

			return txnObject.toString();
		}

		throw new Exception();
	}


	public String getInternalTransfersByAddress(int pageNumber,
												int pageSize,
												long timeStampStart,
												long timeStampEnd,
												String address) throws Exception {
		if (address.startsWith("0x")){
			address = address.replace("0x", "");
		}

		if(Utility.isValidAddress(address)) {
			var pageable = PageRequest.of(pageNumber, pageSize, sortDesc1());

			var page = internalTransferJpaRepository.findAllByToAddrOrFromAddr(address, address, pageable);

			List<InternalTransfer> txnsList = page.getContent();
			JSONArray txnArray = new JSONArray();
			JSONObject txnObject = new JSONObject();
			if (txnsList != null && !txnsList.isEmpty()) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				for (var txn : txnsList) {
					JSONObject result = new JSONObject(ow.writeValueAsString(txn));

					// Converting Value from Wei to Aion
					result.put(VALUE, Utility.toAion(txn.getValueTransferred()));
					txnArray.put(result);
				}

				JSONObject pageObject = new JSONObject();
				pageObject.put(TOTAL_ELEMENTS, page.getTotalElements());
				pageObject.put(TOTAL_PAGES, page.getTotalPages());
				pageObject.put(NUMBER, pageNumber);
				pageObject.put(SIZE, pageSize);
				pageObject.put(START, timeStampStart);
				pageObject.put(END, timeStampEnd);


				txnObject.put(CONTENT, txnArray);
				txnObject.put(PAGE, pageObject);
			}

			// If the ResultSet is Null
			if (txnArray.length() == 0) {
				txnArray = new JSONArray();
				txnObject.put(CONTENT, txnArray);
			}

			return txnObject.toString();
		}else throw new Exception();
	}

    public String getInternalTransfersByTransactionHash(int pageNumber,
														int pageSize,
														String transactionHash) throws Exception {
        if (transactionHash.startsWith("0x")){
            transactionHash = transactionHash.replace("0x", "");
        }

        if(Utility.validHex(transactionHash)) {

            var pageable = PageRequest.of(pageNumber, pageSize, sortDesc1());

            var page = internalTransferJpaRepository.findAllByTransactionHash(transactionHash, pageable);

            List<InternalTransfer> txnsList = page.getContent();
            JSONArray txnArray = new JSONArray();
            JSONObject txnObject = new JSONObject();
            if (txnsList != null && !txnsList.isEmpty()) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                for (var txn : txnsList) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(txn));

                    // Converting Value from Wei to Aion
                    result.put("valueTransferred", Utility.toAion(txn.getValueTransferred()));
                    txnArray.put(result);
                }

                JSONObject pageObject = new JSONObject();
                pageObject.put(TOTAL_ELEMENTS, page.getTotalElements());
                pageObject.put(TOTAL_PAGES, page.getTotalPages());
                pageObject.put(NUMBER, pageNumber);
                pageObject.put(SIZE, pageSize);


                txnObject.put(CONTENT, txnArray);
                txnObject.put(PAGE, pageObject);
            }

            // If the ResultSet is Null
            if (txnArray.length() == 0) {
                txnArray = new JSONArray();
                txnObject.put(CONTENT, txnArray);
            }

            return txnObject.toString();
        }else throw new Exception();
    }


	// Internal Methods
	private String getTransactionEventsByTransactionHash(String txnHash,
														 int pageNumber,
														 int pageSize) throws Exception {
		// Gathering the Events of the Transaction
		Page<Event> eventPage = evtRepo.findByTransactionHash(txnHash, PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "eventTimestamp")));
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		JSONObject eventObject = new JSONObject();
		JSONArray eventArray = new JSONArray();
		if(eventPage != null) {
			List<Event> eventList = eventPage.getContent();
			if(eventList != null && !eventList.isEmpty()) {
				for(Event event: eventList) {
					eventArray.put(new JSONObject(ow.writeValueAsString(event)));
				}

				JSONObject pageObject = new JSONObject();
				pageObject.put(TOTAL_ELEMENTS, eventPage.getTotalElements());
				pageObject.put(TOTAL_PAGES, eventPage.getTotalPages());
				pageObject.put(NUMBER, pageNumber);
				pageObject.put(SIZE, pageSize);

				eventObject.put(CONTENT, eventArray);
				eventObject.put(PAGE, pageObject);
			}
		}

		// If the ResultSet is Null
		if (eventArray.length() == 0) {
			eventArray = new JSONArray();
			eventObject.put(CONTENT, eventArray);
		}

		return eventObject.toString();
	}

	@Cacheable(CacheConfig.TRANSACTIONS)
	public Page<Transaction> findByBlockNumber(long blockNumber, int page,int size){

		return txnRepo.findByBlockNumber(blockNumber,PageRequest.of(page,size));

	}

	@Cacheable(CacheConfig.TRANSACTIONS)
	public Page<Transaction> findByBlockHash(String blockHash, int page,int size){

		return txnRepo.findByBlockHash(Utility.sanitizeHex(blockHash),PageRequest.of(page,size));

	}

    /**
     * Return all transactions in a specified time range.
     * @param startTime used to construct the time ranged
     * @param endTime used to construct the time range
     * @param pageSize the
     * @param pageNumber
     * @return List of peggable transaction
     */
	@Cacheable(CacheConfig.TRANSACTIONS)
	public Page<Transaction>  findByTime(int pageNumber,
									 int pageSize,
									 long startTime,
									 long endTime) {
		var ldtStart = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("UTC"));
		var ldtEnd = Instant.ofEpochSecond(endTime).atZone(ZoneId.of("UTC"));
		Logging.traceLogStartAndEnd(ldtStart,ldtEnd, "Call to getTransactionByTime");
		try{Utility.validateTxListPeriod(ldtStart, ldtEnd);}catch (Exception e){e.printStackTrace();}
		return txnRepo.findAll(TransactionSpec.checkTime(ldtStart, ldtEnd), PageRequest.of(pageNumber, pageSize, sortDesc1()));


	}

	@Cacheable(CacheConfig.TRANSACTIONS_2)
	public Page<Transaction> findAll(int page, int size){
		return txnRepo.findAll(PageRequest.of(page, size, sortDesc1()));
	}
}