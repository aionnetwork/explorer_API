package com.aion.dashboard.utility;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RewardsCalculatorTest {

    @Test
    void pastReward() {
        final long BLK_NUM = 100;
        BigInteger result = RewardsCalculator.calculateReward(BLK_NUM);
        BigInteger expectedResult = new BigInteger("749280715963941121");
        assertEquals(result, expectedResult);
    }

    @Test
    void currentReward() {
        final long BLK_NUM = 1000000;
        BigInteger result = RewardsCalculator.calculateReward(BLK_NUM);
        BigInteger expectedResult = RewardsCalculator.AFTER_MONTH_ONE_BLOCK_REWARD;
        assertEquals(result, expectedResult);
    }

    @Test
    void futureReward() {
        final long BLK_NUM = 3500000;
        BigInteger result = RewardsCalculator.calculateReward(BLK_NUM);
        BigInteger expectedResult = new BigInteger("1512969176075743180");
        assertEquals(result, expectedResult);
    }
}
