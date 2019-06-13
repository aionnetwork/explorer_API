package com.aion.dashboard.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.LongStream;
@Deprecated
public class RewardsCalculator {
	private static BigInteger m;

    public static final long YEARLY_BLOCK_COUNT = 3110400;
	public static final long MONTHLY_BLOCK_COUNT = 259200;

	public static final BigDecimal YEARLY_INFLATION_RATE = new BigDecimal("1.01");

	public static final BigInteger BLOCK_ONE_BLOCK_REWARD = new BigInteger("748994641621655092");
	public static final BigInteger AFTER_MONTH_ONE_BLOCK_REWARD = new BigInteger("1497989283243310185");

    /**
     * Pre-calculate the desired increment
     */
	static {
		m = AFTER_MONTH_ONE_BLOCK_REWARD.subtract(BLOCK_ONE_BLOCK_REWARD).divide(BigInteger.valueOf(MONTHLY_BLOCK_COUNT));
	}

	/**
	 * Linear ramp function that falls off after the upper bound
	 */
	public static BigInteger calculateReward(long number) {
		if(number < 1) return BigInteger.ZERO;
		if(number <= MONTHLY_BLOCK_COUNT) return BigInteger.valueOf(number).multiply(m).add(BLOCK_ONE_BLOCK_REWARD);
		if(number <= YEARLY_BLOCK_COUNT)  return AFTER_MONTH_ONE_BLOCK_REWARD;
		else {
            long NoYP = number / YEARLY_BLOCK_COUNT;                        // Calculate the Number of Years Passed (NoYP)
            BigDecimal result = new BigDecimal(AFTER_MONTH_ONE_BLOCK_REWARD);
            for(int i = 0; i < NoYP; i++) result = result.multiply(YEARLY_INFLATION_RATE);
		    return BigInteger.valueOf(result.longValue());
		}
	}

    public BigInteger getDelta() { return m; }

	public static BigDecimal calculateTotalRewardsAtBlockNumber(long BlockNum){
		return LongStream.range(1, BlockNum+1).parallel().mapToObj(RewardsCalculator::calculateReward).map(b -> new BigDecimal(b).scaleByPowerOfTen(-18)).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
