package com.aion.dashboard.controllers;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Statistics;
import com.aion.dashboard.repositories.CirculatingSupplyJPARepository;
import com.aion.dashboard.services.AccountService;
import com.aion.dashboard.services.BlockService;
import com.aion.dashboard.services.ContractService;
import com.aion.dashboard.services.GraphingService;
import com.aion.dashboard.services.TokenService;
import com.aion.dashboard.services.TransactionService;
import com.aion.dashboard.utility.BackwardsCompactibilityUtil;
import com.aion.dashboard.utility.Logging;
import com.aion.dashboard.utility.Utility;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.Cacheable;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This controller is used to maintain legacy support for dashboard v1.0
 */
@RestController
@RequestMapping("/dashboard")
@Timed
public class Dashboard {

    private static final String MESSAGE = "message";
    private static final String INVALID_REQUEST = "Error: Invalid Request";
    private static final String MISSING_SEARCH_PARAM = "Missing Search Param";
    private static final String RESULT = "result";
    private static final String SEARCH_TYPE = "searchType";
    private BlockService blockService;
    private TokenService tokenService;
    private AccountService accountService;
    private GraphingService graphingService;
    private ContractService contractService;
    private TransactionService transactionService;
    private SimpMessagingTemplate brokerMessagingTemplate;
    @Autowired
    CirculatingSupplyJPARepository circulatingSupplyJPARepository;
    @Autowired
    Dashboard(BlockService blockService,
              TokenService tokenService,
              AccountService accountService,
              GraphingService graphingService,
              ContractService contractService,
              TransactionService transactionService,
              SimpMessagingTemplate brokerMessagingTemplate) {
        this.blockService = blockService;
        this.tokenService = tokenService;
        this.accountService = accountService;
        this.graphingService = graphingService;
        this.contractService = contractService;
         this.transactionService = transactionService;
        this.brokerMessagingTemplate = brokerMessagingTemplate;
    }

    // Tokens
    @Cacheable(CacheConfig.TOKENS)
    @GetMapping(value = "/getTokenList")
    public String getTokenList(@RequestParam(value = "page", required = false) String page,
                               @RequestParam(value = "size", required = false) String size,
                               @RequestParam(value = "timestampStart", required = false) String timestampStart,
                               @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            long start = Utility.parseDefaultStartYear(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);

            return tokenService.getTokenList(start, end, pageNumber, pageSize);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);

        }
    }

    @Cacheable(CacheConfig.TOKEN_LIST_BY_NAME)
    @GetMapping(value = "/getTokenListByTokenNameOrTokenSymbol")
    public String getTokenListByTokenNameOrTokenSymbol(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                       @RequestParam(value = "page", required = false) String page,
                                                       @RequestParam(value = "size", required = false) String size) {

        try {
            if (searchParam != null && !searchParam.trim().isEmpty()) {
                int pageNumber = Utility.parseRequestedPage(page);
                int pageSize = Utility.parseRequestedSize(size);

                String result = tokenService.getTokenListByTokenName(searchParam, pageNumber, pageSize);
                if (result.equals("{\"content\":[]}"))
                    return tokenService.getTokenListByTokenSymbol(searchParam, pageNumber, pageSize);
                else return result;
            }

            return getJsonString(RESULT, MISSING_SEARCH_PARAM);

        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }

    @Cacheable(CacheConfig.TOKEN_TRANSFERS)
    @GetMapping(value = "/getTokenDetailsTransfersAndHoldersByContractAddress")
    public String getTokenDetailsTransfersAndHoldersByContractAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                                      @RequestParam(value = "contractAddress", required = false) String contractAddress,
                                                                      @RequestParam(value = "holdersPage", required = false) String holdersPage,
                                                                      @RequestParam(value = "holdersSize", required = false) String holdersSize,
                                                                      @RequestParam(value = "transfersPage", required = false) String transfersPage,
                                                                      @RequestParam(value = "transfersSize", required = false) String transfersSize,
                                                                      @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                                                      @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            if (contractAddress == null && searchParam != null && !searchParam.trim().isEmpty())
                contractAddress = searchParam;

            if (contractAddress != null && !contractAddress.trim().isEmpty()) {
                int holdersPageNumber = Utility.parseRequestedPage(holdersPage);
                int holdersPageSize = Utility.parseRequestedSize(holdersSize);

                int transfersPageNumber = Utility.parseRequestedPage(transfersPage);
                int transfersPageSize = Utility.parseRequestedSize(transfersSize);

                long start = Utility.parseDefaultStartYear(timestampStart);
                long end = Utility.parseDefaultEnd(timestampEnd);

                return tokenService.getTokenDetailsTransfersAndHoldersByContractAddress(start, end, holdersPageNumber, holdersPageSize, transfersPageNumber, transfersPageSize, contractAddress);
            }
            return getJsonString(RESULT, MISSING_SEARCH_PARAM);

        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }


    // Contracts
    @Cacheable(CacheConfig.CONTRACT_LIST)
    @GetMapping(value = "/getContractList")
    public String getContractList(@RequestParam(value = "page", required = false) String page,
                                  @RequestParam(value = "size", required = false) String size,
                                  @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                  @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            long start = Utility.parseDefaultStartYear(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);

            return contractService.getContractList(start, end, pageNumber, pageSize);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }

    @Cacheable(CacheConfig.CONTRACT_DETAILS)
    @GetMapping(value = "/getContractDetailsByContractAddress")
    public String getContractDetailsByContractAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                      @RequestParam(value = "contractAddress", required = false) String contractAddress,
                                                      @RequestParam(value = "eventsPage", required = false) String eventsPage,
                                                      @RequestParam(value = "eventsSize", required = false) String eventsSize,
                                                      @RequestParam(value = "transactionsPage", required = false) String transactionsPage,
                                                      @RequestParam(value = "transactionsSize", required = false) String transactionsSize) {
        try {
            if (contractAddress == null && searchParam != null && !searchParam.trim().isEmpty())
                contractAddress = searchParam;

            if (contractAddress != null && !contractAddress.trim().isEmpty()) {
                int transactionsPageNumber = Utility.parseRequestedPage(transactionsPage);
                int transactionsPageSize = Utility.parseRequestedSize(transactionsSize);

                int eventsPageNumber = Utility.parseRequestedPage(eventsPage);
                int eventsPageSize = Utility.parseRequestedSize(eventsSize);

                return contractService.getContractDetailsByContractAddress(contractAddress, eventsPageNumber, eventsPageSize, transactionsPageNumber, transactionsPageSize);
            }
            return getJsonString(RESULT, MISSING_SEARCH_PARAM);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }


    // blocks
    @Cacheable(CacheConfig.BLOCK_LIST)
    @GetMapping(value = "/getBlockList")
    public String getBlockList(@RequestParam(value = "page", required = false) String page,
                               @RequestParam(value = "size", required = false) String size,
                               @RequestParam(value = "timestampStart", required = false) String timestampStart,
                               @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            long start = Utility.optionalStartBlks(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);
            return blockService.getBlockList(start, end, pageNumber, pageSize);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }

    @Cacheable(CacheConfig.BLOCKS_MINED)
    @GetMapping(value = "/getBlocksMinedByAddress")
    public String getBlocksMinedByAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                          @RequestParam(value = "accountAddress", required = false) String accountAddress,
                                          @RequestParam(value = "page", required = false) String page,
                                          @RequestParam(value = "size", required = false) String size,
                                          @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                          @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            if (accountAddress == null && searchParam != null && !searchParam.trim().isEmpty())
                accountAddress = searchParam;

            if (accountAddress == null) return new JSONObject().put(RESULT, MISSING_SEARCH_PARAM).toString();
            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            long start = Utility.parseDefaultStartMonth(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);

            return blockService.getBlocksMinedByAddress(start, end, pageNumber, pageSize, accountAddress);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }

    }

    @Cacheable(CacheConfig.BLOCKS_AND_TRANSACTION)
    @GetMapping(value = "/getBlockAndTransactionDetailsFromBlockNumberOrBlockHash")
    public String getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(@RequestParam(value = "searchParam", required = false) String searchParam) {
        try {
            if (searchParam != null && !searchParam.trim().isEmpty())
                return blockService.getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(searchParam);
            return getJsonString(RESULT, MISSING_SEARCH_PARAM);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }


    // transactions
    @Cacheable(CacheConfig.TRANSACTION_LIST)
    @GetMapping(value = "/getTransactionList")
    public String getTransactionList(@RequestParam(value = "page", required = false) String page,
                                     @RequestParam(value = "size", required = false) String size,
                                     @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                     @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            long start = Utility.optionalStartTx(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);

            return transactionService.getTransactionList(pageNumber, pageSize, start, end);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }


    }

    @Cacheable(CacheConfig.TRANSACTION_BY_ADDRESS)
    @GetMapping(value = "/getTransactionsByAddress")
    public String getTransactionsByAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                           @RequestParam(value = "page", required = false) String page,
                                           @RequestParam(value = "size", required = false) String size,
                                           @RequestParam(value = "accountAddress", required = false) String accountAddress,
                                           @RequestParam(value = "tokenAddress", required = false) String tokenAddress,
                                           @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                           @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {
        try {
            if (accountAddress == null && searchParam != null && !searchParam.trim().isEmpty())
                accountAddress = searchParam;

            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            long end = Utility.parseDefaultEnd(timestampEnd);

            if (accountAddress == null || accountAddress.trim().isEmpty()) {
                return getJsonString(RESULT, MISSING_SEARCH_PARAM);
            } else if (tokenAddress != null) {
                long start = Utility.parseDefaultStartYear(timestampStart);
                return transactionService.getTransactionsByAddressForToken(pageNumber, pageSize, start, end, accountAddress, tokenAddress);
            } else {
                long start = Utility.parseDefaultStartMonth(timestampStart);
                return transactionService.getTransactionsByAddressForNative(pageNumber, pageSize, start, end, accountAddress);
            }

        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }

    }

    @GetMapping(value = "/getInternalTransfersByAddress")
    @Deprecated
    public String getInternalTransfersByAddress(@RequestParam(value = "accountAddress", required = false) String accountAddress,
                                                @RequestParam(value = "page", required = false) String page,
                                                @RequestParam(value = "size", required = false) String size,
                                                @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                                @RequestParam(value = "timestampEnd", required = false) String timestampEnd) {

        try {
            long start = Utility.parseDefaultStartYear(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);

            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);
            if (accountAddress == null || accountAddress.trim().isEmpty()) {
                return getJsonString(RESULT, MISSING_SEARCH_PARAM);
            } else {
                return transactionService.getInternalTransfersByAddress(pageNumber, pageSize, start, end, accountAddress);
            }
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }

    }

    @GetMapping(value = "/getInternalTransfersByTransactionHash")
    @Deprecated
    public String getInternalTransfersByTransactionHash(@RequestParam(value = "transactionHash", required = false) String transactionHash,
                                                        @RequestParam(value = "page", required = false) String page,
                                                        @RequestParam(value = "size", required = false) String size) {
        try {

            int pageNumber = Utility.parseRequestedPage(page);
            int pageSize = Utility.parseRequestedSize(size);

            if (transactionHash == null || transactionHash.trim().isEmpty()) {
                return getJsonString(RESULT, MISSING_SEARCH_PARAM);
            } else {
                return transactionService.getInternalTransfersByTransactionHash(pageNumber, pageSize, transactionHash);
            }
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }

    }

    @Cacheable(CacheConfig.TRANSACTION)
    @GetMapping(value = "/getTransactionDetailsByTransactionHash")
    public String getTransactionDetailsByTransactionHash(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                         @RequestParam(value = "transactionHash", required = false) String transactionHash,
                                                         @RequestParam(value = "page", required = false) String page,
                                                         @RequestParam(value = "size", required = false) String size) {
        try {
            if (transactionHash == null && searchParam != null && !searchParam.trim().isEmpty())
                transactionHash = searchParam;

            if (transactionHash != null && !transactionHash.trim().isEmpty()) {
                int pageNumber = Utility.parseRequestedPage(page);
                int pageSize = Utility.parseRequestedSize(size);

                return transactionService.getTransactionDetailsByTransactionHash(transactionHash, pageNumber, pageSize);
            }
            return getJsonString(RESULT, MISSING_SEARCH_PARAM);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }


    // Accounts
    @Cacheable(CacheConfig.STATISTICS_ACCOUNT_STATS)
    @GetMapping(value = "/getDailyAccountStatistics")
    public String getDailyAccountStatistics() {
        try {
            return Statistics.getInstance().getDailyAccountStatistics();
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }

    @Cacheable(CacheConfig.RICH_LIST)
    @GetMapping(value = "/getRichList")
    public String getRichList() {
        try {
            return accountService.getAccountRichList();
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }

    @Cacheable(CacheConfig.ACCOUNT_DETAILS)
    @GetMapping(value = "/getAccountDetails")
    public String getAccountDetails(@RequestParam(value = "accountAddress", required = false) String accountAddress,
                                    @RequestParam(value = "tokenAddress", required = false) String tokenAddress) {
        try {
            if (accountAddress != null && !accountAddress.trim().isEmpty())
                return accountService.getAccountDetails(accountAddress, tokenAddress);
            return getJsonString(RESULT, MISSING_SEARCH_PARAM);
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }

    // Home
    @GetMapping(value = "/search")
    @Deprecated
    @ApiOperation(value = "DEPRECATED")
    public String search(@RequestParam(value = "searchParam", required = false) String searchParam) {
        if (searchParam != null && !searchParam.trim().isEmpty()) {

            String result = "";

            // Checking if searchParam is an Account's or Contract's address
            if (Utility.isValidAddress(searchParam)) {
                try {
                    result = accountService.getAccountDetails(searchParam, null);
                    if (result != null && !result.equals("{\"content\":[]}")) {

                        return getJsonString(SEARCH_TYPE, "account");

                    }
                } catch (Exception ignored) {
                    //It is not an account
                }
            }

            // Checking if searchParam is a Token's name or symbol
            try {
                result = tokenService.getTokenListByTokenName(searchParam, 0, 25);
                // Gets the Token first by Name and if it fails that, attempts to find by Symbol
                if (result != null && result.equals("{\"content\":[]}")) result = tokenService.getTokenListByTokenSymbol(searchParam, 0, 25);
                if (result != null && !result.equals("{\"content\":[]}")) {
                    result = new StringBuilder(result).insert(1, "\"searchType\":\"token\",").toString();
                    return result;
                }
            } catch (Exception ignored) {
                // Search param is not a token
            }

            // Checking if searchParam is a Block's number or hash
            try {
                result = blockService.getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(searchParam);
                if (result != null && !result.equals("{\"content\":[]}")) {

                    return getJsonString(SEARCH_TYPE, "block");

                }
            } catch (Exception ignored) {
                //Search param is not a block
            }

            // Checking if searchParam is a Transaction's hash
            try {
                result = transactionService.getTransactionDetailsByTransactionHash(searchParam, 0, 25);
                if (result != null && !result.equals("{\"content\":[]}")) {

                    return getJsonString(SEARCH_TYPE, "transaction");

                }
            } catch (Exception ignored) {
                // Search param is not a transaction
            }

            return getJsonString(MESSAGE,INVALID_REQUEST);

        }

        return getJsonString(RESULT,MISSING_SEARCH_PARAM);
    }

    @Cacheable(CacheConfig.VIEW_V1)
    @GetMapping(value = "/view")
    public String viewDashboard() {
        try {
            return Statistics.getInstance().getDashboardJSON();
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);

        }
    }
    @Cacheable(CacheConfig.CIRCULATING_SUPPLY)
    @GetMapping(value = "/getCirculatingSupply")
    public String getCirculatingSupply() {
        try {
             return circulatingSupplyJPARepository
                     .findByOrderByTimestampDesc(PageRequest.of(0, 1)).getContent().get(0).
                             getSupply().setScale(0, RoundingMode.HALF_EVEN).toString();
        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);
        }
    }


    // Miscellaneous
    @GetMapping(value = "/getRTMetrics")
    @Deprecated
    @ApiOperation(value = "DEPRECATED")
    public BackwardsCompactibilityUtil.oldMetrics getRTMetrics() {
        try {
            return BackwardsCompactibilityUtil.oldMetrics.toObject(Statistics.getInstance().getRTMetrics());
        } catch (Exception e) {
            Logging.traceException(e);
            return null;
        }
    }

    @GetMapping(value = "/getGraphingInfo")
    public String getGraphingInfo(@RequestParam(value = "type", required = false) String type) {
        try {
            if (type != null && !type.trim().isEmpty() && Utility.validInt(type))
                return graphingService.getGraphingInfo(Integer.parseInt(type));
            else
                return getJsonString(RESULT, MISSING_SEARCH_PARAM);

        } catch (Exception e) {
            Logging.traceException(e);
            return getJsonString(MESSAGE, INVALID_REQUEST);

        }
    }


    @Cacheable(CacheConfig.BLOCK_NUMBER)
    @GetMapping(value = "/getBlockNumber")
    public String getBlockNumber() {
        return blockService.getBlockNumber();
    }


    private String getJsonString(String key,String message) {

        JSONObject result = new JSONObject();
        result.put(key, message);
        return result.toString();
    }
}