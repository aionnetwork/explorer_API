package com.aion.dashboard.services;

import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.view.SearchResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
public class SearchServiceMysqlTest {


    @Autowired
    public SearchServiceMysql serviceMysql;




    @Before
    public void setup(){


    }




    @After
    public void teardown(){

    }




    @Test
    public void search() {

        //testing blocks
        var blockSearchNumSuccess = serviceMysql.search("10");

        var blockSearchNumFail = serviceMysql.search("10000");


        assertTrue(blockSearchNumFail.getSearchResults().isEmpty());
        assertFalse(blockSearchNumSuccess.getSearchResults().isEmpty());

        assertTrue(blockSearchNumSuccess.getSearchResults().contains(new SearchResult.SearchResultValues("10","block")));

        //testing transactions
        var transactionSearchSuccess = serviceMysql.search("05399bf17fdd57b64a06102d922abf161e53d8ad95b96d602a6d4df76af9d5b4");
        var transactionSearchFail = serviceMysql.search("05399bf17fdd57b64a06102d922abf161e53d8ad95b96d602a6d4df76af9d5c4");

        assertTrue(transactionSearchFail.getSearchResults().isEmpty());
        assertFalse(transactionSearchSuccess.getSearchResults().isEmpty());

        assertTrue(transactionSearchSuccess.getSearchResults().contains(new SearchResult.SearchResultValues("05399bf17fdd57b64a06102d922abf161e53d8ad95b96d602a6d4df76af9d5b4","transaction")));


        //testing address
        var accountAddrSearchSuccess = serviceMysql.search("     0xa0236a8bbe698c3293cb75cdb5bdbbae18f17507878778d42686e61d6410d643");
        var accountAddrSearchFail = serviceMysql.search("    0xa0236a8bbe698c3293cb75cdb5bdbbac18f17507878778d42686e61d6410d643");

        assertTrue(accountAddrSearchFail.getSearchResults().isEmpty());
        assertFalse(accountAddrSearchSuccess.getSearchResults().isEmpty());


        assertTrue(accountAddrSearchSuccess.getSearchResults().stream().allMatch(rs->
                rs.key.equals("a0236a8bbe698c3293cb75cdb5bdbbae18f17507878778d42686e61d6410d643")
                && (rs.type.equals("account") || rs.type.equals("contract") || rs.type.equals("token"))
        ));

        var tokenByNameSearch= serviceMysql.search("SantaCoin");

        assertFalse(tokenByNameSearch.getSearchResults().isEmpty());

    }





}