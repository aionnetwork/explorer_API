package com.aion.dashboard.downloads.utility;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RewardsCalculator {
    private static BigInteger m;

    public static final long YEARLY_BLOCK_COUNT = 3110400;
    public static final long MONTHLY_BLOCK_COUNT = 259200;

    public static final BigDecimal YEARLY_INFLATION_RATE = new BigDecimal("1.01");

    public static final BigInteger BLOCK_ONE_BLOCK_REWARD = BigInteger.valueOf(748994641621655092L);
    public static final BigInteger AFTER_MONTH_ONE_BLOCK_REWARD = BigInteger.valueOf(1497989283243310185L);

    /**
     * Pre-calculate the desired increment
     */
    static {
        long delta = MONTHLY_BLOCK_COUNT - 0;
        m = AFTER_MONTH_ONE_BLOCK_REWARD.subtract(BLOCK_ONE_BLOCK_REWARD).divide(BigInteger.valueOf(delta));
    }

    /**
     * Linear ramp function that falls off after the upper bound
     */
    public static BigInteger calculateReward(long number) {
        if(number < 1) return BigInteger.ZERO;
        if(number <= MONTHLY_BLOCK_COUNT) return BigInteger.valueOf(number - 1).multiply(m).add(BLOCK_ONE_BLOCK_REWARD);
        if(number <= YEARLY_BLOCK_COUNT)  return AFTER_MONTH_ONE_BLOCK_REWARD;
        else {
            long noYP = number / YEARLY_BLOCK_COUNT;                        // Calculate the Number of Years Passed (noYP)
            BigDecimal result = BigDecimal.valueOf(AFTER_MONTH_ONE_BLOCK_REWARD.doubleValue());
            for(int i = 0; i < noYP; i++) result = result.multiply(YEARLY_INFLATION_RATE);
            return BigInteger.valueOf(result.longValue());
        }
    }

    public BigInteger getDelta() { return m; }
}
