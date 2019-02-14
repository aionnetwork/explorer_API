package com.aion.dashboard.downloads.controller;


import com.aion.dashboard.downloads.entities.EntityTypes;
import com.aion.dashboard.downloads.exceptions.ApiInvalidRequestException;
import com.aion.dashboard.downloads.services.RecaptchaService;
import com.aion.dashboard.downloads.utility.Utility;
import com.aion.dashboard.downloads.services.ExporterService;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class Controller {

    @Autowired
    private ExporterService exporterService;

    @Autowired
    private RecaptchaService recaptchaService;

    private final Logger general = LoggerFactory.getLogger("general");

/*CSV*/
    @GetMapping(path = "/exportAccountTxns", produces = "text/csv")
    public ResponseEntity exportAccountTxns(@RequestParam(value = "g-recaptcha-response", required = false) String recaptchaResponse,
                                                    @RequestParam(value = "accountAddress", required = false) String accountAddress,
                                                    @RequestParam(value = "tokenAddress", required = false) String tokenAddress,
                                                    @RequestParam(value = "timestampStart", required = false) String timestampStart,
                                                    @RequestParam(value = "timestampEnd", required = false) String timestampEnd,
                                                    HttpServletResponse response,
                                                    HttpServletRequest request) {
        try {
            // Verifying ReCaptcha
            String ip = request.getRemoteAddr();
            String captchaVerifyMessage = recaptchaService.verifyRecaptcha(ip, recaptchaResponse);

            // Verifying a Valid Google Recaptcha
            if(StringUtils.isNotEmpty(captchaVerifyMessage)) throw new ApiInvalidRequestException("Invalid Request: Failed Google-Recpatcha");

            // Verifying a Valid accountAddress to Download Transactions For
            accountAddress = Utility.isValidAddress(accountAddress);
            if(accountAddress == null || accountAddress.trim().isEmpty() || accountAddress.equals(StringUtils.EMPTY)) throw new ApiInvalidRequestException("Invalid Request: accountAddress is Null/Empty/Invalid");

            long start = Utility.parseDefaultStartYear(timestampStart);
            long end = Utility.parseDefaultEnd(timestampEnd);

            // Verifying a Valid Timestamp Range
            if(start > end) throw new ApiInvalidRequestException("Invalid Request: timestampStart is Greater Than timestampEnd");

            response.getWriter().write(exporterService.exportToCSV(accountAddress, tokenAddress, EntityTypes.ACCOUNT_TXNS_CSV.toString(), start, end, response));
            return new ResponseEntity<> (HttpStatus.OK);
        } catch(Exception e) {
            general.debug("exportAccountTxns", e);
            return new ResponseEntity<> (new JSONObject().put("message", "Error: Invalid Request").toString(), HttpStatus.BAD_REQUEST);
        }
    }
/*CSV*/
}
