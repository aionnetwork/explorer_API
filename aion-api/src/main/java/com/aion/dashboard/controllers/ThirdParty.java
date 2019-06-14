package com.aion.dashboard.controllers;

import com.aion.dashboard.controllers.mapper.TransactionMapper;
import com.aion.dashboard.exception.EntityNotFoundException;
import com.aion.dashboard.exception.IOFoundException;
import com.aion.dashboard.services.BlockService;
import com.aion.dashboard.services.ThirdPartyService;
import com.aion.dashboard.services.TransactionService;
import com.aion.dashboard.utility.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/thirdParty")
public class ThirdParty {

    private BlockService blockService;
    private ThirdPartyService thirdPartyService;
    private TransactionService transactionService;

    @Autowired
    ThirdParty(BlockService blockService,ThirdPartyService thirdPartyService,TransactionService transactionService){
        this.blockService=blockService;
        this.thirdPartyService=thirdPartyService;
        this.transactionService=transactionService;
    }

    @GetMapping(value = "/getHeightBlock")
    public ResponseEntity<String>  getHeightBlock() throws EntityNotFoundException {
        return new ResponseEntity(blockService.getHeightBlock(), HttpStatus.OK);

    }

    @GetMapping(value = "/getCirculatingSupply")
    public  ResponseEntity<String> getCirculatingSupply() throws IOFoundException {

        try {
            return new ResponseEntity(thirdPartyService.getCirculatingSupply(), HttpStatus.OK);
        }catch (IOException i) {
            throw new IOFoundException("Error reading the source");
        }

    }

    @GetMapping(value = "/getTransactions")
    public ResponseEntity<String>  getTransactions(@RequestParam("block_number") long block_number,
                                                   @RequestParam(value = "page",defaultValue = "0") int page,
                                                   @RequestParam(value = "size",defaultValue = "10") int size) throws EntityNotFoundException {

        return new ResponseEntity(
                TransactionMapper.makeTransactionDTOList(transactionService.findByBlockNumber(block_number,page,  size))
                , HttpStatus.OK);


    }

}
