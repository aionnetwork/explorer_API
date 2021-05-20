package com.aion.dashboard.controllers;


import static org.springframework.http.HttpStatus.OK;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.controllers.mapper.*;
import com.aion.dashboard.datatransferobject.*;
 import com.aion.dashboard.exception.EntityNotFoundException;
import com.aion.dashboard.exception.IncorrectArgumentException;
import com.aion.dashboard.exception.MissingArgumentException;
import com.aion.dashboard.repositories.CirculatingSupplyJPARepository;
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
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


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
@Timed
@Api
public class Dashboardv2 {

    private SearchService searchService;
    private BlockService blockService;
     private TransactionService transactionService;
    private StatisticsService statisticsService;
    private TxLogService txLogService;
    private InternalTransactionService internalTransactionService;
    private AccountService accountService;
    private Validators validators;
    @Autowired
    CirculatingSupplyJPARepository circulatingSupplyJPARepository;


    @Autowired
    Dashboardv2(InternalTransactionService internalTransactionService, TxLogService txLogService,
        SearchService searchService, BlockService blockService,
        TransactionService transactionService, StatisticsService statisticsService,
        AccountService accountService, Validators validators){
        this.internalTransactionService = internalTransactionService;
        this.txLogService = txLogService;
        this.blockService=blockService;

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
    @ApiOperation(produces = "application/json",
        value = "Returns the block that corresponds to a block number or hash,"
            + " or if neither is supplied the best block.",
        httpMethod = "GET")
    public ResponseEntity<Result<BlockDTO>> block(@RequestParam(value = "blockNumber", required = false) String blockNumber,
                                                  @RequestParam(value = "blockHash", required = false)
                                                  @ApiParam(value = "This must be block on the main chain.") String blockHash){
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
        validators.validatePage(page);
        BlockMapper mapper = BlockMapper.getInstance();
        if (minerAddress.isPresent() && startTime.isPresent()){
            return packageResponse(mapper.makeResult(blockService.findByMinerAddress(minerAddress.get(), startTime.get(), endTime.orElse(System.currentTimeMillis()/1000), page, size)));
        }
        else if (minerAddress.isPresent()){
            return packageResponse(mapper.makeResult(blockService.findByMinerAddress(minerAddress.get(), page, size)));
        }
        else if (startTime.isPresent()){
            return packageResponse(mapper.makeResult(blockService.findBlocksInRange(startTime.get(), endTime.orElse(System.currentTimeMillis()/1000), page, size), startTime.get(), endTime.orElse(System.currentTimeMillis()/1000)));
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
    @ApiIgnore
    public ResponseEntity transaction(@RequestParam(value = "transactionHash", required = false) String transactionHash,
                                      @RequestParam(value = "address", required = false) String address){

        throw new UnsupportedOperationException("/transaction");
    }

    /**
     * This request can be used to return all transactions in a specified block (given the
     * blocknumber or blockhash) or to return all transactions in a specified time range. If no
     * parameters are supplied transactions are sored in descending order(based on block number) and
     * returned.
     *
     * @param blockNumber the number of the specified block
     * @param blockHash the hash of the specified block
     * @param startTime used to construct the time ranged
     * @param endTime used to construct the time range
     * @param size the
     * @param page
     * @return
     */
    @GetMapping("/transactions")
    @ApiOperation(
            value =
                    "This request can be used to return all transactions in a specified block "
                        + "(given the blocknumber or blockhash)"
                        + " or to return all transactions in a specified time range."
                        + "If no parameters are supplied transactions are sored in"
                        + " descending order(based on block number) and returned.",
        httpMethod = "GET",
        produces = "application/json"
    )
    public ResponseEntity<Result<TransactionDTO>> transactions(
            @RequestParam(value = "blockNumber", required = false) String blockNumber,
            @RequestParam(value = "blockHash", required = false)
            @ApiParam(value = "This must be block on the main chain.")  String blockHash,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        validators.validateSize(size);
        validators.validatePage(page);
        if(isNotEmpty(blockNumber) )
                return packageResponse( TransactionMapper.getInstance().makeResult(transactionService.findByBlockNumber(Long.valueOf(blockNumber), page, size)));
        else if(isNotEmpty(blockHash) )
                return packageResponse( TransactionMapper.getInstance().makeResult(transactionService.findByBlockHash(blockHash, page, size)));
        else if( isNotEmpty(startTime) && isNotEmpty(endTime))
            return packageResponse(TransactionMapper.getInstance().makeResult(transactionService.findByTime(page, size,Long.valueOf(startTime),Long.valueOf(endTime)), Long.valueOf(startTime),Long.valueOf(endTime)));
        else
            return packageResponse(TransactionMapper.getInstance().makeResult(transactionService.findAll(page, size)));
    }



    /**
     * This request must specify an address.
     * @param address The address of the account to be returned.
     * @return The specified account.
     */
    @GetMapping("/account")
    @ApiOperation(value = "Returns the balance and nonce of an account. "
        + "An empty result is returned if the dashboard has not found the account.",
        produces = "application/json",
        httpMethod = "GET"
    )
    public ResponseEntity account(@RequestParam(value = "address", required = false) Optional<String> address){
        return packageResponse(
            AccountMapper
                .getMapper()
                .makeResult(Optional.ofNullable(
                    accountService
                        .findByAccountAddress(address.orElseThrow(MissingArgumentException::new))
                ).orElseThrow())
        );
    }

    /**
     * @param page the page to be returned
     * @param size the number of accounts to be returned
     * @param sort the order in which accounts should be sorted.
     * @return A list of accounts sorted by aion balance.
     */
    @GetMapping("/accounts")
    @Cacheable(CacheConfig.ACCOUNT_LIST)
    @ApiOperation(value = "Returns accounts sorted by balance.")
    public ResponseEntity accounts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "25") Integer size,
                                   @RequestParam(value = "sort", defaultValue = "desc")
                                   @ApiParam(allowableValues = "asc, desc") String sort){
        validators.validateSize(size);
        validators.validatePage(page);
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
    @ApiIgnore
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
    @ApiIgnore
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
    @ApiIgnore
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
    @ApiIgnore
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
    @Cacheable(CacheConfig.INTERNAL_TRANSACTION)
    @ApiOperation(value = "Returns a page of internal transactions or the matching internal "
        + "transaction if an index and hash is supplied.",
        produces = "application/json",
        httpMethod = "GET"
    )
    public ResponseEntity<Result<InternalTransactionDTO>> internalTransaction(@RequestParam(value = "transactionHash", required = false) Optional<String> txHash,
                                                                      @RequestParam(value = "index", required = false) Optional<Integer> index,
                                                                      @RequestParam(value = "address", required = false) Optional<String> address,
                                                                      @RequestParam(value = "blockNumber",required = false) Optional<Long> blockNumber,
                                                                      @RequestParam(value = "contractAddress", required=false) Optional<String> contractAddress,
                                                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                      @RequestParam(value = "size", defaultValue = "25") Integer size){
        validators.validateSize(size);
        validators.validatePage(page);
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
    @Cacheable(value = CacheConfig.STATISTICS_METRICS)
    @ApiOperation(value = "Returns the latest metrics for the network or the network at the "
        + "specified block number. The supported metrics types are rt(realtime) and stable.")
    public ResponseEntity<Result<MetricsDTO>> metrics(
        @RequestParam(value = "type", defaultValue = "rt")
        @ApiParam(allowableValues = "rt, stable") String type,
        @RequestParam(value = "blockNumber") Optional<Long> blockNumber){
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
    @ApiIgnore
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
    @ApiIgnore
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
    @ApiIgnore
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
    @ApiIgnore
    public ResponseEntity tokenTransfer(@RequestParam(value = "contractAddress", required = false) String contractAddress,
                                        @RequestParam(value = "participantAddress", required = false) String participantAddress,
                                        @RequestParam(value = "transactionHash", required = false) String transactionHash){
        throw new UnsupportedOperationException("/tokenTransfer");
    }


    /**
     * @return the current health of this API.
     */
    @GetMapping("/health")
    @ApiOperation(value = "Returns the health of the api.",
        produces = "application/json")
    public ResponseEntity<Result<HealthDTO>> health(){
        return packageResponse(Result.from(statisticsService.health()));
    }

    private <T> ResponseEntity<T> packageResponse(T body){
        //To be used to create custom headers

        return new ResponseEntity<>(body, OK);
    }



    @GetMapping(value = "/search")
    @ApiOperation("Returns the key and resource type for a specified search parameter.")
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

    @GetMapping(value = "/validatorStatistics")
    @Cacheable(CacheConfig.STATISTICS_VALIDATORS)
    @ApiOperation(value = "The validator statistics at the specified block number or the latest "
        + "statistics",
        produces = "application/json",
        httpMethod = "GET"
    )
    public ResponseEntity<Result<ValidatorStatsDTO>> validators(
        @RequestParam(value = "blockNumber") Optional<Long> blockNumber,
        @RequestParam(value="sealType") @ApiParam(allowableValues = "POS,POW") Optional<String> sealType,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "25") Integer size){
        validators.validateSize(size);
        validators.validatePage(page);
        if(sealType.isPresent()){
            return packageResponse(
                ValidatorStatsMapper.getInstance().makeResult(
                    sealType.filter(s-> s.equalsIgnoreCase("POW") ||
                        s.equalsIgnoreCase("POS"))
                    .map(seal -> {
                        if (blockNumber.isPresent())
                            return statisticsService.validatorStats(blockNumber.get(),
                                seal, page, size);
                        else return statisticsService.validatorStats(seal, page, size);
                    }).orElseThrow(()-> new IncorrectArgumentException("POW or POS."))
                )
            );
        }else {
            return packageResponse(
                ValidatorStatsMapper.getInstance().makeResult(
                    blockNumber
                        .map(b -> statisticsService.validatorStats(b, page, size))
                        .orElseGet(() -> statisticsService.validatorStats(page, size)))
            );
        }
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
    @Cacheable(CacheConfig.TX_LOG)
    @ApiOperation(value = "Returns the transaction logs for the given parameter.")
    public ResponseEntity<Result<TxLogDTO>> txLog(@RequestParam("blockNumber") Optional<Long> blockNumber,
                                                  @RequestParam("transactionHash") Optional<String> transactionHash,
                                                  @RequestParam("contractAddress") Optional<String> contractAddress,
                                                  @RequestParam("blockNumberStart") Optional<Long> blockNumberStart,
                                                  @RequestParam("blockNumberEnd") Optional<Long> blockNumberEnd,
                                                  @RequestParam("start") Optional<Long> start,
                                                  @RequestParam("end") Optional<Long> end,
                                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "end", defaultValue = "25") Integer size){
        validators.validateSize(size);
        validators.validatePage(page);
        if (transactionHash.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForTransaction(transactionHash.get())
            ));
        }
        else if (blockNumber.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForBlock(blockNumber.get(), page, size)
            ));
        }
        else if(contractAddress.isPresent() && start.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(txLogService.findLogsForContractAndInTimeRange(
                    contractAddress.get(), start.get(), end.orElse(System.currentTimeMillis()/1000), page, size
            ), start.get(), end.orElse(System.currentTimeMillis()/1000)));

        }
        else if (contractAddress.isPresent() && blockNumberStart.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(txLogService.findLogsForContractAndInBlockRange(
                    contractAddress.get(), blockNumberStart.get(), blockNumberEnd.orElse(blockService.blockNumber()), page, size
            )));

        }
        else if (contractAddress.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForContract(contractAddress.get(), page, size)
            ));
        }
        else if (start.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsInTimeRange(start.get(), end.orElse(System.currentTimeMillis()/1000), page, size)
                ,start.get(), end.orElse(System.currentTimeMillis()/1000)
            ));
        }
        else if (blockNumberStart.isPresent()){
            return packageResponse(TxLogMapper.getInstance().makeResult(
                    txLogService.findLogsForBlockRange(blockNumberStart.get(), blockNumberEnd.orElse(blockService.blockNumber()), page, size)
            ));
        }

        throw new MissingArgumentException();
    }

    @GetMapping(value = "/view")
    @Cacheable(CacheConfig.VIEW_V2)
    @ApiOperation(value = "Returns the latest set of blocks and transactions from the block chain.")
    public ResponseEntity<Result<ViewDTO>> view(){
        return packageResponse(ViewDTOMapper.makeDTO(statisticsService.getSbMetrics(),
            blockService.blockNumber(),
            transactionService.findAll(0, 10),
            blockService.findBlocks(0,4)));
    }


    boolean isNotEmpty(String str){
        return !Optional.ofNullable(str).filter(s-> !s.isEmpty() && !s.isBlank()).isEmpty();
    }

    @GetMapping(value = "/accountsStat")
    public ResponseEntity<Result<AccountStatsDTO>> accountsStat(@RequestParam(value = "blockNumber") Optional<Long> blockNumber){
        return packageResponse(AccountsStatsMapper.getMapper()
            .makeResult(blockNumber
                .map(statisticsService::accountStats)
                .orElseGet(statisticsService::recentAccountStats)));
    }

    @GetMapping(value = "/transactionStats")
    public ResponseEntity<Result<TransactionStatsDTO>> transactionsStats(){
        return packageResponse(TransactionStatsMapper.getMapper().makeResult(statisticsService.allTransactionStats()));
    }

    @GetMapping(value = "/transactionStat")
    public ResponseEntity<Result<TransactionStatsDTO>> transactionsStat(@RequestParam(value = "blockNumber")Optional<Long> blockNumber){
        return packageResponse(TransactionStatsMapper.getMapper().makeResult(
            blockNumber.map(statisticsService::transactionStats).orElseGet(statisticsService::transactionStats)
        ));
    }
    @Cacheable(CacheConfig.CIRCULATING_SUPPLY)
    @GetMapping(value = "/circulating-supply")
    public ResponseEntity<Result<CirculatingSupplyDTO>> getCirculatingSupply(){
        return packageResponse(CirculatingSupplyMapper
                .getMapper()
                .makeResult(circulatingSupplyJPARepository.findByOrderByTimestampDesc(PageRequest.of(0, 1)).getContent().get(0)));

    }
}
