package com.aion.dashboard.controllers;


import com.aion.dashboard.controllers.mapper.BlockMapper;
import com.aion.dashboard.controllers.mapper.TransactionMapper;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.exception.EntityNotFoundException;
import com.aion.dashboard.services.BlockService;
import com.aion.dashboard.services.SearchService;
import com.aion.dashboard.services.ThirdPartyService;
import com.aion.dashboard.services.TransactionService;
import com.aion.dashboard.view.Result;
import com.aion.dashboard.view.ResultInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;


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



    @Autowired
    Dashboardv2(SearchService searchService, BlockService blockService, ThirdPartyService thirdPartyService, TransactionService transactionService){
        this.blockService=blockService;
        this.thirdPartyService=thirdPartyService;
        this.transactionService=transactionService;
        this.searchService=searchService;
    }

    /**
     *
     * @return a json object specifying the request available in this controller and the link to access the swagger-ui.
     */
    @RequestMapping("/**")
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
    public ResponseEntity<Block> block(@RequestParam(value = "blockNumber", required = false) String blockNumber,
                                       @RequestParam(value = "blockHash", required = false) String blockHash){

        if(isNotEmpty(blockNumber) )
            return new ResponseEntity( Result.from(BlockMapper.makeBlockDTO(blockService.findByBlockNumber( Long.valueOf(blockNumber)))), HttpStatus.OK);
        if(isNotEmpty(blockHash))
            return new ResponseEntity(Result.from(BlockMapper.makeBlockDTO(blockService.findByBlockHash( blockHash))), HttpStatus.OK);

        throw new UnsupportedOperationException("/block");
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
    public ResponseEntity blocks(@RequestParam(value = "size", defaultValue = "25", required = false) String size,
                                 @RequestParam(value = "page", defaultValue = "0", required = false) String page,
                                 @RequestParam(value = "startTime", required = false) String startTime,
                                 @RequestParam(value = "endTime", required = false) String endTime,
                                 @RequestParam(value = "minerAddress", required = false) String minerAddress){


        throw new UnsupportedOperationException("/blocks");
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
    public ResponseEntity transactions( @RequestParam(value = "blockNumber", required = false) String blockNumber,
                                        @RequestParam(value = "blockHash", required = false) String blockHash,
                                        @RequestParam(value = "startTime", required = false) String startTime,
                                        @RequestParam(value = "endTime", required = false) String endTime,
                                        @RequestParam(value = "size", defaultValue = "25", required = false) int size,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) int page
    ){


        if(isNotEmpty(blockNumber) )
                return new ResponseEntity(
                        TransactionMapper.makeTransactionDTOList(transactionService.findByBlockNumber(Long.valueOf(blockNumber), page, size))
                        , HttpStatus.OK);

        if(isNotEmpty(blockHash) )
                return new ResponseEntity(
                        TransactionMapper.makeTransactionDTOList(transactionService.findByBlockHash(blockHash, page, size))
                        , HttpStatus.OK);


        if( isNotEmpty(startTime) &&
                    isNotEmpty(endTime))
            return new ResponseEntity(
                    TransactionMapper.makeTransactionDTOList(transactionService.findByTime(page, size,Long.valueOf(startTime),Long.valueOf(endTime)))
                    , HttpStatus.OK);


        throw new UnsupportedOperationException("/transactions");
    }



    /**
     * This request must specify an address.
     * @param address The address of the account to be returned.
     * @return The specified account.
     */
    @GetMapping("/account")
    public ResponseEntity account(@RequestParam(value = "address", required = false) String address){

        throw new UnsupportedOperationException("/account");
    }

    /**
     * @param page the page to be returned
     * @param size the number of accounts to be returned
     * @param sort the order in which accounts should be sorted.
     * @return A list of accounts sorted by aion balance.
     */
    @GetMapping("/accounts")
    public ResponseEntity accounts(@RequestParam(value = "page", defaultValue = "0") String page,
                                   @RequestParam(value = "size", defaultValue = "25") String size,
                                   @RequestParam(value = "sort", defaultValue = "desc") String sort){

        throw new UnsupportedOperationException("/accounts");
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
    public ResponseEntity internalTransfer(@RequestParam(value = "transactionHash" , required = false) String transactionHash,
                                           @RequestParam(value = "contractAddress", required = false) String contractAddress,
                                           @RequestParam(value = "participantAddress", required = false) String participantAddress,
                                           @RequestParam(value = "page", defaultValue = "0") String page,
                                           @RequestParam(value = "size", defaultValue = "25") String size){
        throw new UnsupportedOperationException("/internalTansfers");
    }

    /**
     *
     * @param type the type of metrics to be returned, supported types include rt and stable.
     * @return the current network metrics
     */
    @GetMapping("/metrics")
    public ResponseEntity metrics(@RequestParam(value = "type", defaultValue = "rt")String type){
        throw new UnsupportedOperationException("/metrics");
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
    public ResponseEntity health(){
        throw new UnsupportedOperationException("/health");
    }

    private <T> ResponseEntity<T> packageResponse(T body){
        //To be used to create custom headers

        return new ResponseEntity<>(body, OK);
    }



    @GetMapping(value = "/search")
    public ResponseEntity<ResultInterface> search(@RequestParam(value = "searchParam", required = false) String searchParam) {
        return packageResponse(searchService.search(searchParam));
    }


    /**
     * @return Return the current block height of the network
     */
    @GetMapping(value = "/height")
    public  ResponseEntity<ResultInterface>  getHeightBlock() throws EntityNotFoundException {
        return packageResponse(Result.from(BlockMapper.makeBlockDTO(blockService.getHeightBlock())));

    }




    boolean isNotEmpty(String str){
        return !Optional.ofNullable(str).filter(s-> !s.isEmpty() && !s.isBlank()).isEmpty();
    }

}
