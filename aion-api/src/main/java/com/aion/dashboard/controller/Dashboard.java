package com.aion.dashboard.controller;

import com.aion.dashboard.entities.Statistics;
import com.aion.dashboard.service.*;
import com.aion.dashboard.utility.BackwardsCompactibilityUtil;
import com.aion.dashboard.utility.Utility;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/dashboard")
public class Dashboard {


    @Autowired
	private BlockService blockService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private GraphingService graphingService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ExporterService exporterService;

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
	private TransactionService transactionService;

    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;

    // Tokens
    @RequestMapping(value = "/getTokenList", method = RequestMethod.GET)
    public String getTokenList(@RequestParam(value = "page", required = false) String page,
                               @RequestParam(value = "size", required = false) String size) {
        try {
            int pageNumber = 0;
            int pageSize = 25;
            if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
            if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
            return tokenService.getTokenList(pageNumber, pageSize);
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getTokenListByTokenNameOrTokenSymbol", method = RequestMethod.GET)
    public String getTokenListByTokenNameOrTokenSymbol(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                   @RequestParam(value = "page", required = false) String page,
                                                   @RequestParam(value = "size", required = false) String size) {

        try {
            if(searchParam != null && !searchParam.trim().isEmpty()) {
                int pageNumber = 0;
                int pageSize = 25;
                if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
                if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
                return tokenService.getTokenListByTokenNameOrTokenSymbol(searchParam, pageNumber, pageSize);
            }
            return new JSONObject().put("result", "Missing Search Param").toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getTokenDetailsTransfersAndHoldersByContractAddress", method = RequestMethod.GET)
    public String getTokenDetailsTransfersAndHoldersByContractAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                                      @RequestParam(value = "holdersPage", required = false) String holdersPage,
                                                                      @RequestParam(value = "holdersSize", required = false) String holdersSize,
                                                                      @RequestParam(value = "transfersPage", required = false) String transfersPage,
                                                                      @RequestParam(value = "transfersSize", required = false) String transfersSize) {
        try {
            if(searchParam != null && !searchParam.trim().isEmpty()) {
                int holderPageNumber = 0;
                int holdersPageSize = 1000;
                int transferPageNumber = 0;
                int transferPageSize = 100000;
                if(holdersPage != null && Utility.validInt(holdersPage)) holderPageNumber = Integer.parseInt(holdersPage);
                if(holdersSize != null && Utility.validInt(holdersSize)) holdersPageSize = Integer.parseInt(holdersSize);
                if(transfersPage != null && Utility.validInt(transfersPage)) transferPageNumber = Integer.parseInt(transfersPage);
                if(transfersSize != null && Utility.validInt(transfersSize)) transferPageSize = Integer.parseInt(transfersSize);
                return tokenService.getTokenDetailsTransfersAndHoldersByContractAddress(searchParam, holderPageNumber, holdersPageSize, transferPageNumber, transferPageSize);
            }
            return new JSONObject().put("result", "Missing Search Param").toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }


    // Contracts
    @RequestMapping(value = "/getContractList", method = RequestMethod.GET)
    public String getContractList(@RequestParam(value = "page", required = false) String page,
                                  @RequestParam(value = "size", required = false) String size) {
        try {
            int pageNumber = 0;
            int pageSize = 25;
            if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
            if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
            return contractService.getContractList(pageNumber, pageSize);
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getContractDetailsByContractAddress", method = RequestMethod.GET)
    public String getContractDetailsByContractAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                      @RequestParam(value = "eventsPage", required = false) String eventsPage,
                                                      @RequestParam(value = "eventsSize", required = false) String eventsSize,
                                                      @RequestParam(value = "transactionsPage", required = false) String transactionsPage,
                                                      @RequestParam(value = "transactionsSize", required = false) String transactionsSize) {
        try {
            if(searchParam != null && !searchParam.trim().isEmpty()) {
                int transactionsPageNumber = 0;
                int transactionsPageSize = 25;
                int eventsPageNumber = 0;
                int eventsPageSize = 25;
                if(transactionsPage != null && Utility.validInt(transactionsPage)) transactionsPageNumber = Integer.parseInt(transactionsPage);
                if(transactionsSize != null && Utility.validInt(transactionsSize)) transactionsPageSize = Integer.parseInt(transactionsSize);
                if(eventsPage != null && Utility.validInt(eventsPage)) eventsPageNumber = Integer.parseInt(eventsPage);
                if(eventsSize != null && Utility.validInt(eventsSize)) eventsPageSize = Integer.parseInt(eventsSize);
                return contractService.getContractDetailsByContractAddress(searchParam, eventsPageNumber, eventsPageSize, transactionsPageNumber, transactionsPageSize);
            }
            return new JSONObject().put("result", "Missing Search Param").toString();
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }


    // Blocks
    @RequestMapping(value = "/getBlockList", method = RequestMethod.GET)
	public String getBlockList(@RequestParam(value = "page", required = false) String page,
                               @RequestParam(value = "size", required = false) String size) {
		try {
            int pageNumber = 0;
            int pageSize = 25;
            if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
            if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
			return blockService.getBlockList(pageNumber, pageSize);
		} catch(Exception e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("message", "Error: Invalid Request");
			return result.toString();
		}
	}

    @RequestMapping(value = "/getBlocksMinedByAddress", method = RequestMethod.GET)
    public String getBlocksMinedByAddress(@RequestParam(value = "searchParam", required = false) String searchParam,
                                          @RequestParam(value = "page", required = false) String page,
                                          @RequestParam(value = "size", required = false) String size) {
        try {
            if(searchParam == null) return new JSONObject().put("result", "Missing Search Param").toString();
            int pageNumber = 0;
            int pageSize = 25;
            if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
            if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
            return blockService.getBlocksMinedByAddress(searchParam, pageNumber, pageSize);
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }

    }

    @RequestMapping(value = "/getBlockAndTransactionDetailsFromBlockNumberOrBlockHash", method = RequestMethod.GET)
    public String getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(@RequestParam(value = "searchParam", required = false) String searchParam) {
        try {
            if(searchParam != null && !searchParam.trim().isEmpty()) return blockService.getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(searchParam);
            return new JSONObject().put("result", "Missing Search Param").toString();
        }catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }


    // Transactions
    @RequestMapping(value = "/getTransactionList", method = RequestMethod.GET)
    public String getTransactionList(@RequestParam(value = "page", required = false) String page,
                                     @RequestParam(value = "size", required = false) String size) {
        try {
            int pageNumber = 0;
            int pageSize = 25;
            if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
            if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
            return transactionService.getTransactionList(pageNumber, pageSize);
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getTransactionsByAddress", method = RequestMethod.GET)
    public String getTransactionsByAddress(@RequestParam(value = "accountAddress", required = false) String accountAddress,
                                           @RequestParam(value = "tokenAddress", required = false) String tokenAddress,
                                           @RequestParam(value = "page", required = false) String page,
                                           @RequestParam(value = "size", required = false) String size) {
        try {
            if(accountAddress == null || accountAddress.trim().isEmpty()) return new JSONObject().put("result", "Missing Search Param").toString();
            int pageNumber = 0;
            int pageSize = 25;
            if(page != null && Utility.validInt(page)) pageNumber = Integer.parseInt(page);
            if(size != null && Utility.validInt(size)) pageSize = Integer.parseInt(size);
            return transactionService.getTransactionsByAddress(accountAddress, tokenAddress, pageNumber, pageSize);
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getTransactionDetailsByTransactionHash", method = RequestMethod.GET)
    public String getTransactionDetailsByTransactionHash(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                        @RequestParam(value = "page", required = false) String page,
                                                        @RequestParam(value = "size", required = false) String size) {


        try {
            if(searchParam != null && !searchParam.trim().isEmpty()) {
                int pageNumber = 0;
                int pageSize = 25;
                if(page != null && Utility.validInt(page)) { pageNumber = Integer.parseInt(page); }
                if(size != null && Utility.validInt(size)) { pageSize = Integer.parseInt(size); }
                return transactionService.getTransactionDetailsByTransactionHash(searchParam, pageNumber, pageSize);
            }
            return new JSONObject().put("result", "Missing Search Param").toString();
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }


    // Accounts
    @RequestMapping(value = "/getRichList", method = RequestMethod.GET)
    public String getRichList() {
        try { return balanceService.getAccountRichList(); }
        catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getAccountDetails", method = RequestMethod.GET)
    public String getAccountDetails(@RequestParam(value = "accountAddress", required = false) String accountAddress,
                                    @RequestParam(value = "tokenAddress", required = false) String tokenAddress) {
        try {
            if(accountAddress != null && !accountAddress.trim().isEmpty()) return balanceService.getAccountDetails(accountAddress, tokenAddress);
            return new JSONObject().put("result", "Missing Search Param").toString();
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

	@RequestMapping(value = "/getDailyAccountStatistics", method = RequestMethod.GET)
	public String getDailyAccountStatistics() {
		try { return balanceService.getDailyAccountStatistics(); }
		catch(Exception e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("message", "Error: Invalid Request");
			return result.toString();
		}
	}


	// Home
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "searchParam", required = false) String searchParam) {
        if (searchParam != null && !searchParam.trim().isEmpty()) {

            String result = "";

            // Checking if searchParam is an Account's address
            if (searchParam.startsWith("a0") || searchParam.startsWith("0xa0") || searchParam.startsWith("0x0") || searchParam.startsWith("0")) try {
                result = balanceService.getAccountDetails(searchParam, null);
                if (result != null && !result.equals("{\"rel\":null}")) {
                    result = new StringBuilder(result).insert(1, "\"searchType\":\"account\",").toString();
                    return result;
                }
            } catch (Exception ignored) { }

            // Checking if searchParam is a Contract's address
            if (searchParam.startsWith("a0") || searchParam.startsWith("0xa0") || searchParam.startsWith("0x0") || searchParam.startsWith("0")) try {
                result = contractService.getContractDetailsByContractAddress(searchParam, 0, 25, 0, 25);
                if (result != null && !result.equals("{\"rel\":null}")) {
                    result = new StringBuilder(result).insert(1, "\"searchType\":\"contract\",").toString();
                    return result;
                }
            } catch (Exception ignored) { }

            // Checking if searchParam is a Token's name or symbol
            try { result = tokenService.getTokenListByTokenNameOrTokenSymbol(searchParam, 0, 25);
                if (result != null && !result.equals("{\"rel\":null}")) {
                    result = new StringBuilder(result).insert(1, "\"searchType\":\"token\",").toString();
                    return result;
                }
            } catch (Exception ignored) { }

            // Checking if searchParam is a Block's number or hash
            try { result = blockService.getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(searchParam);
                if (result != null && !result.equals("{\"rel\":null}")) {
                    result = new StringBuilder(result).insert(1, "\"searchType\":\"block\",").toString();
                    return result;
                }
            } catch (Exception ignored) { }

            // Checking if searchParam is a Transaction's hash
            try { result = transactionService.getTransactionDetailsByTransactionHash(searchParam, 0, 25);
                if (result != null && !result.equals("{\"rel\":null}")) {
                    result = new StringBuilder(result).insert(1, "\"searchType\":\"transaction\",").toString();
                    return result;
                }
            } catch (Exception ignored) { }

            JSONObject error = new JSONObject();
            error.put("message", "Error: Invalid Request");
            return error.toString();
        }

        return new JSONObject().put("result", "Missing Search Param").toString();
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewDashboard() {
        try {
            return Statistics.getInstance().getDashboardJSON();
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

	@RequestMapping(value = "/newBlockReceived", method = RequestMethod.POST)
	public void newBlockDashboard() {
        try {
            this.brokerMessagingTemplate.convertAndSend("/dashboard/view", Statistics.getInstance().getDashboardJSON());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


	// Third Party APIs
    @RequestMapping(value = "/getCirculatingSupply", method = RequestMethod.GET)
    public String getCirculatingSupply() {
        try { return thirdPartyService.getCirculatingSupply(); }
        catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

//    @RequestMapping(value = "/countOperations", method = RequestMethod.GET)
//    public String countOperations(@RequestParam(value = "startTime") String start,
//                                  @RequestParam(value = "endTime") String end) {
//        try {
//            long startTimestamp = 0L;
//            long endTimestamp = ZonedDateTime.now().toInstant().getEpochSecond();
//            if(start != null && Utility.validLong(start)) startTimestamp = parseLong(start);
//            if(end != null && Utility.validLong(end)) endTimestamp = parseLong(end);
//            return thirdPartyService.countOperations(startTimestamp, endTimestamp);
//        } catch (Exception e) {
//            e.printStackTrace();
//            JSONObject result = new JSONObject();
//            result.put("message", "Error: Invalid Request");
//            return result.toString();
//        }
//    }


    // Miscellaneous
    @RequestMapping(value = "/getRTMetrics", method = RequestMethod.GET)
    public BackwardsCompactibilityUtil.oldMetrics getRTMetrics() {
        try {
            return BackwardsCompactibilityUtil.oldMetrics.toObject(Statistics.getInstance().getSBMetrics());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/getGraphingInfo", method = RequestMethod.GET)
    public String getGraphingInfo(@RequestParam(value = "type", required = false) String type) {
        try {
            if(type != null && !type.trim().isEmpty() && Utility.validInt(type)) return graphingService.getGraphingInfo(Integer.parseInt(type));
            return new JSONObject().put("result", "Missing Type Param").toString();
        } catch(Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }

    @RequestMapping(value = "/getLatestGraphingTimestamp", method = RequestMethod.GET)
    public String getLatestGraphingTimestamp() { return graphingService.getLatestTimestamp(); }

    @RequestMapping(value = "/exportToCsv", method = RequestMethod.GET)
    public ResponseEntity<String> exportToCsv(@RequestParam(value = "searchParam1", required = false) String searchParam1,
                                              @RequestParam(value = "searchParam2", required = false) String searchParam2,
                                              @RequestParam(value = "entityType", required = false) String entityType,
                                              @RequestParam(value = "rangeMin1", required = false) String rangeMin,
                                              @RequestParam(value = "rangeMax1", required = false) String rangeMax,
                                              @RequestParam(value = "g-recaptcha-response") String recaptchaResponse,
                                              HttpServletResponse response,
                                              HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String captchaVerifyMessage = recaptchaService.verifyRecaptcha(ip, recaptchaResponse);
        if(StringUtils.isNotEmpty(captchaVerifyMessage)) return new ResponseEntity<> (captchaVerifyMessage, HttpStatus.BAD_REQUEST);

        try {
            if(entityType == null || entityType.trim().isEmpty()) return new ResponseEntity<> (new JSONObject().put("result", "Missing Entity Type").toString(), HttpStatus.BAD_REQUEST);
            int min = -1;
            int max = -1;
            if(rangeMin != null && Utility.validInt(rangeMin)) min = Integer.parseInt(rangeMin);
            if(rangeMax != null && Utility.validInt(rangeMax)) max = Integer.parseInt(rangeMax);
            return new ResponseEntity<> (exporterService.exportToCSV(searchParam1, searchParam2, entityType, min, max, min, max, response), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<> (new JSONObject().put("message", "Error: Invalid Request").toString(), HttpStatus.BAD_REQUEST);
        }
    }
}



