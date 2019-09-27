package com.aion.dashboard.controllers;


import static org.springframework.http.HttpStatus.OK;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.controllers.mapper.*;
import com.aion.dashboard.datatransferobject.*;
import com.aion.dashboard.exception.EntityNotFoundException;
import com.aion.dashboard.exception.IncorrectArgumentException;
import com.aion.dashboard.exception.MissingArgumentException;
import com.aion.dashboard.services.AccountService;
import com.aion.dashboard.services.BlockService;
import com.aion.dashboard.services.InternalTransactionService;
import com.aion.dashboard.services.SearchService;
import com.aion.dashboard.services.StatisticsService;
import com.aion.dashboard.services.ThirdPartyService;
import com.aion.dashboard.services.TransactionService;
import com.aion.dashboard.services.TxLogService;
import com.aion.dashboard.utility.Validators;
import com.aion.dashboard.view.Result;
import com.aion.dashboard.view.SearchResult;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.annotations.Cacheable;


/**
 * This class will be used to develop a fully compliant rest API
 * @see <a href="https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design">Rest API Guidelines</>
 * <br/>
 * All times specified need to conform to:
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"> ISO local date time</>.
 * note that the api uses UTC as its timezone.
 * All interfaces must return a media type of application/json with the exception of any third party api's developed.
 */
@RestController
@RequestMapping("/v2/dashboard")
public class Dashboardv2 {

    private SearchService searchService;
    private BlockService blockService;
    private ThirdPartyService thirdPartyService;
    private TransactionService transactionService;
    private StatisticsService statisticsService;
    private TxLogService txLogService;
    private InternalTransactionService internalTransactionService;
    private AccountService accountService;
    private Validators validators;



    @Autowired
    Dashboardv2(InternalTransactionService internalTransactionService, TxLogService txLogService,
        SearchService searchService, BlockService blockService, ThirdPartyService thirdPartyService,
        TransactionService transactionService, StatisticsService statisticsService,
        AccountService accountService, Validators validators){
        this.internalTransactionService = internalTransactionService;
        this.txLogService = txLogService;
        this.blockService=blockService;
        this.thirdPartyService=thirdPartyService;
        this.transactionService=transactionService;
        this.searchService=searchService;
        this.statisticsService = statisticsService;
        this.accountService = accountService;
        this.validators = validators;
    }

    /**
     *
     * @return a json object specifying the request available in this controller and the link to access the swagger-ui.
     */
    @RequestMapping("/**")
    @ApiIgnore
    public ResponseEntity dashboard(){
        return new ResponseEntity(OK);
    }

    /**
     * Consumers of this endpoint should specify either the blockhash or the blocknumber and not both.
     * @param blockHash the blockhash of the requested block.
     * @param blockNumber the blocknumber of the requested block.
     * @return the specified block or the head of the blockchain.
     */
    @GetMapping("/block")
    public ResponseEntity<Result<BlockDTO>> block(@RequestParam(value = "blockNumber", required = false) String blockNumber,
                                                  @RequestParam(value = "blockHash", required = false) String blockHash){
        BlockMapper blockMapper = BlockMapper.getInstance();
        if(isNotEmpty(blockNumber) ) {
            return packageResponse(BlockMapper.getInstance().makeResult(blockService.findByBlockNumber( Long.valueOf(blockNumber))));
        } else if(isNotEmpty(blockHash)) {
            return packageResponse(BlockMapper.getInstance().makeResult(blockService.findByBlockHash( blockHash)));
        } else {
            return packageResponse(BlockMapper.getInstance().makeResult(blockService.getHeightBlock()));
        }
    }

    /**
     *
     * @param size The number of entries to be returned. The default is 25.
     * @param page The page number to be returned. The default is 0.
     * @param startTime used to determine the time range
     * @param endTime used to determine the time range
     * @param minerAddress
     * @return A list of blocks
     */
    @GetMapping("/blocks")
    public ResponseEntity blocks(@RequestParam(value = "size", defaultValue = "25", required = false) Integer size,
                                 @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                 @RequestParam(value = "startTime", required = false) Optional<Long> startTime,
                                 @RequestParam(value = "endTime", required = false) Optional<Long> endTime,
                                 @RequestParam(value = "minerAddress", required = false) Optional<String> minerAddress){
        validators.validateSize(size);
        BlockMapper mapper = BlockMapper.getInstance();
        if (minerAddress.isPresent() && startTime.isPresent()){
            return packageResponse(mapper.makeResult(blockService.findByMinerAddress(minerAddress.get(), startTime.get(), endTime.orElse(System.currentTimeMillis()/1000), page, size)));
        }
        else if (minerAddress.isPresent()){
            return packageResponse(mapper.makeResult(blockService.findByMinerAddress(minerAddress.get(), page, size)));
        }
        else if (startTime.isPresent()){
            return packageResponse(mapper.makeResult(blockService.findBlocksInRange( startTime.get(), endTime.orElse(System.currentTimeMillis()/1000), page, size)));
        }
        else {
            return packageResponse(mapper.makeResult(blockService.findBlocks(page,size)));
        }

    }


    /**
     * This request must specify either the transaction hash or address
     * @param transactionHash the requested transaction
     * @param address a to/from/contract address
     * @return The transaction specified by the transaction hash or the last transaction in which the specified address participated
     */
    @GetMapping("/transaction")
    public ResponseEntity transaction(@RequestParam(value = "transactionHash", required = false) String transactionHash,
                                      @RequestParam(value = "address", required = false) String address){

        throw new UnsupportedOperationException("/transaction");
    }

    /**
     * This request can be used to return all transactions in a specified block (given the blocknumber or blockhash)
     * or to return all transactions in a specified time range.
     * If no parameters are supplied transactions are sored in descending order(based on block number) and returned.
     * @param blockNumber the number of the specified block
     * @param blockHash the hash of the specified block
     * @param startTime used to construct the time ranged
     * @param endTime used to construct the time range
     * @param size the
     * @param page
     * @return
     */
    @GetMapping("/transactions")
    public ResponseEntity<Result<TransactionDTO>> transactions(@RequestParam(value = "blockNumber", required = false) String blockNumber,
                                                               @RequestParam(value = "blockHash", required = false) String blockHash,
                                                               @RequestParam(value = "startTime", required = false) String startTime,
                                                               @RequestParam(value = "endTime", required = false) String endTime,
                                                               @RequestParam(value = "size", defaultValue = "25", required = false) int size,
                                                               @RequestParam(value = "page", defaultValue = "0", required = false) int page
    ){
        validators.validateSize(size);
        if(isNotEmpty(blockNumber) )
                return packageResponse( TransactionMapper.getInstance().makeResult(transactionService.findByBlockNumber(Long.valueOf(blockNumber), page, size)));
        else if(isNotEmpty(blockHash) )
                return packageResponse( TransactionMapper.getInstance().makeResult(transactionService.findByBlockHash(blockHash, page, size)));
        else if( isNotEmpty(startTime) && isNotEmpty(endTime))
            return packageResponse(TransactionMapper.getInstance().makeResult(transactionService.findByTime(page, size,Long.valueOf(startTime),Long.valueOf(endTime))));
        else throw new MissingArgumentException();

    }



    /**
     * This request must specify an address.
     * @param address The address of the account to be returned.
     * @return The specified account.
     */
    @GetMapping("/account")
    public ResponseEntity account(@RequestParam(value = "address", required = false) Optional<String> address){
        return packageResponse(
            AccountMapper
                .getMapper()
                .makeResult(accountService
                    .findByAccountAddress(address.orElseThrow(MissingArgumentException::new)))
        );
    }

    /**
     * @param page the page to be returned
     * @param size the number of accounts to be returned
     * @param sort the order in which accounts should be sorted.
     * @return A list of accounts sorted by aion balance.
     */
    @GetMapping("/accounts")
    public ResponseEntity accounts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "25") Integer size,
                                   @RequestParam(value = "sort", defaultValue = "desc") String sort){
        validators.validateSize(size);
        if (sort.equalsIgnoreCase("desc") || sort.equalsIgnoreCase("asc")) {
            return packageResponse(AccountMapper.getMapper().makeResult(accountService.findAccounts(page, size, sort)));
        } else {
            throw new IncorrectArgumentException("asc or desc");
        }
    }


    /**
     * This request must specify an address.
     *
     * @param address The address of the account to be returned.
     * @return The specified contract.
     */
    @GetMapping("/contract")
    public ResponseEntity contract(@RequestParam(value = "address", required = false) String address,
                                   @RequestParam(value = "transactionHash", required = false) String transactionHash){

        throw new UnsupportedOperationException("/contract");
    }


    /**
     * @param ownerAddress The address of the contract owner
     * @param startTime Used to determine the time range of the contracts to return
     * @param endTime Used to determine the time range of the contracts to return
     * @return A list of contracts
     */
    @GetMapping("/contracts")
    public ResponseEntity contracts(@RequestParam(value = "ownerAddress", required = false) String ownerAddress,
                                    @RequestParam(value = "startTime", required = false) String startTime,
                                    @RequestParam(value = "endTime", required = false) String endTime,
                                    @RequestParam(value = "page", defaultValue = "0") String page,
                                    @RequestParam(value = "size", defaultValue = "25") String size
    ){

        throw new UnsupportedOperationException("/contracts");
    }


    /**
     * This request must specify one parameter at most.
     * @param address the address of the contract
     * @param transactionHash the transaction hash which needs to be queried
     * @return the list of the most recent events, the list of the events executed by the address
     * or the list of events found in the specified transaction
     */
    @GetMapping("/events")
    public ResponseEntity events(@RequestParam(value = "address", required = false) String address,
                                 @RequestParam(value = "transactionHash", required = false) String transactionHash,
                                 @RequestParam(value = "page", defaultValue = "0") String page,
                                 @RequestParam(value = "size", defaultValue = "25") String size){

        throw new UnsupportedOperationException("/events");
    }


    /**
     * @param unit this is used to specify the granularity of the data. Available values are day, hour or ten minute intervals
     * @param startTime Used to determine the time range
     * @param endTime Used to determine the time range
     * @return the points to be used to plot graphs.
     */
    @GetMapping("/graph")
    public ResponseEntity graph(@RequestParam(value = "unit", defaultValue = "day") String unit,
                                @RequestParam(value = "startTime", required = false) String startTime,
                                @RequestParam(value = "endTime", required = false) String endTime){
        throw new UnsupportedOperationException("/graph");
    }


    /**
     * This request must specify one parameter at most
     * @param transactionHash the transaction to be queried
     * @param contractAddress the contract address which would emit the event
     * @param participantAddress the sender or recipient of the internal transfer
     * @return the most recent internal transfers if no parameters are specified,
     * otherwise the internal transfers that match the transaction, contract or participant.
     */
    @GetMapping("/internalTansfers")
    @ApiIgnore
    @Deprecated
    public ResponseEntity internalTransfer(@RequestParam(value = "transactionHash" , required = false) String transactionHash,
                                           @RequestParam(value = "contractAddress", required = false) String contractAddress,
                                           @RequestParam(value = "participantAddress", required = false) String participantAddress,
                                           @RequestParam(value = "page", defaultValue = "0") String page,
                                           @RequestParam(value = "size", defaultValue = "25") String size){
        throw new UnsupportedOperationException("/internalTansfers");
    }

    @GetMapping("/internalTransaction")
    public ResponseEntity<Result<InternalTransactionDTO>> internalTransaction(@RequestParam(value = "transactionHash", required = false) Optional<String> txHash,
                                                                      @RequestParam(value = "index", required = false) Optional<Integer> index,
                                                                      @RequestParam(value = "address", required = false) Optional<String> address,
                                                                      @RequestParam(value = "blockNumber",required = false) Optional<Long> blockNumber,
                                                                      @RequestParam(value = "contractAddress", required=false) Optional<String> contractAddress,
                                                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                      @RequestParam(value = "size", defaultValue = "25") Integer size){
        validators.validateSize(size);
        InternalTransactionMapper mapper = InternalTransactionMapper.getInstance();
        if (index.isPresent() && txHash.isPresent()){
            return packageResponse(mapper.makeResult(internalTransactionService.findByID(txHash.get(), index.get())));
        }
        else if (txHash.isPresent()){
            return packageResponse(mapper.makeResult(internalTransactionService.findByTxHash(txHash.get())));
        }
        else if (contractAddress.isPresent()){
            return packageResponse(mapper.makeResult(internalTransactionService.findByContractAddress(contractAddress.get())));
        }
        else if (address.isPresent()){
            return packageResponse(mapper.makeResult(internalTransactionService.findByAddress(address.get(), page, size)));
        }
        else if (blockNumber.isPresent()){
            return packageResponse(mapper.makeResult(internalTransactionService.findByBlockNumber(blockNumber.get(), page, size)));
        }
        else {
            throw new MissingArgumentException();
        }
    }


    /**
     *
     * @param type the type of metrics to be returned, supported types include rt and stable.
     * @return the current network metrics
     */
    @GetMapping("/metrics")
    public ResponseEntity<Result<MetricsDTO>> metrics(@RequestParam(value = "type", defaultValue = "rt")String type, @RequestParam(value = "blockNumber") Optional<Long> blockNumber){
        if (type.equalsIgnoreCase("rt")){
            return packageResponse(
                MetricsMapper.makeMetricsDTO(
                    blockNumber.map(statisticsService::getRtMetrics)
                        .orElseGet(statisticsService::getRtMetrics),
                    blockService.blockNumber()
                ));
        }
        else if (type.equalsIgnoreCase("stable")){
            return packageResponse(
                MetricsMapper.makeMetricsDTO(
                    blockNumber.map(statisticsService::getSbMetrics)
                        .orElseGet(statisticsService::getSbMetrics),
                    blockService.blockNumber()
                ));
        }
        else {
            throw new IncorrectArgumentException("stable or rt. Found: " + type);
        }
    }


    /**
     * A parameter must be supplied for this method
     * @param contractAddress the contract address of this token
     * @return the specified token
     */
    @GetMapping("token")
    public ResponseEntity token(@RequestParam(value = "contractAddress", required = false) String contractAddress){
        throw new UnsupportedOperationException("/token");
    }



    /**
     * @param creatorAddress the creator of the tokens
     * @param page the page to be returned
     * @param size the number of elements per page
     * @return A list of tokens
     */
    @GetMapping("/tokens")
    public ResponseEntity tokens(@RequestParam(value = "creatorAddress", required = false) String creatorAddress,
                                 @RequestParam(value = "page", defaultValue = "0") String page,
                                 @RequestParam(value = "size", defaultValue = "25") String size){
        throw new UnsupportedOperationException("/tokens");
    }


    /**
     *
     * @param contractAddress the token contract address
     * @param participantAddress the sender or receiver of the token
     * @param transactionHash the transaction in the transfers occurred
     * @param page the page to be returned
     * @param size the number of elements to be returned per page
     * @return A list of tokens
     */
    @GetMapping("/tokenTransfers")
    public ResponseEntity tokenTransfers(@RequestParam(value = "contractAddress", required = false) String contractAddress,
                                         @RequestParam(value = "participantAddress", required = false) String participantAddress,
                                         @RequestParam(value = "transactionHash", required = false) String transactionHash,
                                         @RequestParam(value = "page", defaultValue = "0") String page,
                                         @RequestParam(value = "size", defaultValue = "25") String size){
        throw new UnsupportedOperationException("/tokenTransfers");
    }


    /**
     * All parameters must be supplied
     * @param contractAddress the address of the token to be supplied
     * @param participantAddress the sender or the receiver of the token
     * @param transactionHash the transaction in which this transfer occurred
     * @return The matching token
     */
    @GetMapping("/tokenTransfer")
    public ResponseEntity tokenTransfer(@RequestParam(value = "contractAddress", required = false) String contractAddress,
                                        @RequestParam(value = "participantAddress", required = false) String participantAddress,
                                        @RequestParam(value = "transactionHash", required = false) String transactionHash){
        throw new UnsupportedOperationException("/tokenTransfer");
    }


    /**
     * @return the current health of this API.
     */
    @GetMapping("/health")
    public ResponseEntity<Result<HealthDTO>> health(){
        return packageResponse(Result.from(statisticsService.health()));
    }

    private <T> ResponseEntity<T> packageResponse(T body){
        //To be used to create custom headers

        return new ResponseEntity<>(body, OK);
    }



    @GetMapping(value = "/search")
    public ResponseEntity<SearchResult> search(@RequestParam(value = "searchParam", required = false) String searchParam) {
        return packageResponse(searchService.search(searchParam));
    }


    /**
     * @return Return the current block height of the network
     */
    @GetMapping(value = "/height")
    public  ResponseEntity<Result<Long>>  getHeightBlock() throws EntityNotFoundException {
        return packageResponse(Result.from(blockService.blockNumber()));

    }

    /**
     * Returns the transaction logs given a block number, transaction hash or contract address.
     * @param blockNumber
     * @param transactionHash
     * @param contractAddress
     * @param blockNumberStart
     * @param blockNumberEnd
     * @param start
     * @param end
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/txlogs")
    public ResponseEntity<Result<TxLogDTO>> txLog(@RequestParam("blockNumber") Optional<Long> blockNumber,
                                                  @RequestParam("transactionHash") Optional<String> transactionHash,
                                                  @RequestParam("contractAddress") Optional<String> contractAddress,
                                                  @RequestParam("blockNumberStart") Optional<Long> blockNumberStart,
                                                  @RequestParam("blockNumberEnd") Optional<Long> blockNumberEnd,
                                                  @RequestParam("start") Optional<Long> start,
                                                  @RequestParam("end") Optional<Long> end,
                                                  @RequestParam("page") Optional<Integer> page,
                                                  @RequestParam("end") Optional<Integer> size){
        validators.validateSize(size.orElse(25));
        if (transactionHash.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForTransaction(transactionHash.get())
            ));
        }
        else if (blockNumber.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForBlock(blockNumber.get(), page.orElse(0), size.orElse(25))
            ));
        }
        else if(contractAddress.isPresent() && start.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(txLogService.findLogsForContractAndInTimeRange(
                    contractAddress.get(), start.get(), end.orElse(System.currentTimeMillis()/1000), page.orElse(0), size.orElse(25)
            )));

        }
        else if (contractAddress.isPresent() && blockNumberStart.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(txLogService.findLogsForContractAndInBlockRange(
                    contractAddress.get(), blockNumberStart.get(), blockNumberEnd.orElse(blockService.blockNumber()), page.orElse(0), size.orElse(25)
            )));

        }
        else if (contractAddress.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForContract(contractAddress.get(), page.orElse(0), size.orElse(25))
            ));
        }
        else if (start.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsInTimeRange(start.get(), end.orElse(System.currentTimeMillis()/1000), page.orElse(0), size.orElse(25))
            ));
        }
        else if (blockNumberStart.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForBlockRange(blockNumberStart.get(), blockNumberEnd.orElse(blockService.blockNumber()), page.orElse(0), size.orElse(25))
            ));
        }

        throw new MissingArgumentException();
    }

    @GetMapping(value = "/view")
    @Cacheable(CacheConfig.VIEW_V2)
    public ResponseEntity<Result<ViewDTO>> view(){
        return packageResponse(ViewDTOMapper.makeDTO(statisticsService.getSbMetrics(), blockService.blockNumber(), transactionService.findAll(0, 10), blockService.findBlocks(0,4)));
    }


    boolean isNotEmpty(String str){
        return !Optional.ofNullable(str).filter(s-> !s.isEmpty() && !s.isBlank()).isEmpty();
    }

}
