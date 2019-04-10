package com.aion.dashboard.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
public class Utility {



    private static final Pattern ACCOUNT_REGEX = Pattern.compile("(0x)?(0{60,64}|a0)[A-Fa-f0-9]*");

    public static class UtilityConfig {
        static final long TRANSACTION_TIME_RANGE = Long.parseLong(Optional.ofNullable(System.getenv("TRANSACTION_TIME_RANGE")).orElse("7"));

        static final long BLOCK_TIME_RANGE =  Long.parseLong(Optional.ofNullable(System.getenv("BLOCK_TIME_RANGE")).orElse("7"));

    }


    private static BigDecimal WeiRate = BigDecimal.valueOf(10).pow(18);

    private static final int PageSizeLimit = 999;

    private static final int DefaultPageSize = 25;

    private static final int DefaultPageNumber = 0;

    private static final Set<String> AddressWhiteList = Set.copyOf(List.of(
            "08efa07244bacb5dc92daef46474560b57e9cfc2be62072fa21decf64051a317",
            "0a0b61014dc764640ee6087095faf68dfeb4e4b9ee681225611fea65932c93e4",
            "1318abaea6686c79eaa4ba37f5eef843bd065e74560ed1094133641b4f2395b8",
            "24a970ec53022623d95989e3340ff180a843c8645841699c4c19a227287901cf",
            "38dd1ae75523611bed9d4b647016dc8b0735c42317bd03e9bd65ceca69e8c46c",
            "64b09d162e2f91d3308381f28c02b9d0e5cb4270a43465b696e1db6210aad189",
            "7b8289c4b1ada70af8794b508f6db000ca25f1d1821c053da302a0403f73b29e",
            "aebf26d4d5438304e277a3d5104a3b056d8617f586f33560d4b2e7984a2bfe01",
            "af504da64d6cb7ec06a462b2c43a3cb4e482db3b41fde42b537f4c64fedc894b",
            "af694e98b964b275d723c2c66e224739d97d36125bba985215bca1075656b82e",
            "c00dcc9fe51c73767fad07cd4da990a8aa7487f40ba5718f711b4fdc09ae5b6e",
            "cb68ed818e6f712b315d53e0cce541b2d2e4c0d101ef1fcc91bccef1c21dffd7",
            "d801109bab93a4ab71c16e85389d9c366a23a975693cbdcc7e6db85561f1d5ce",
            "f5079727f0c503b5eae232dbd303128338576fc1db867892ae07dcc920691ab9"));

    public static boolean isValidAddress(String strAddr){

        if (strAddr == null) {
            return false;
        } else {
            String addrToTest = strAddr.startsWith("0x") ? strAddr.substring(2) : strAddr;

            if (addrToTest.length() != 64) {
                return false;
            } else return  ACCOUNT_REGEX.matcher(addrToTest).find() || AddressWhiteList.contains(addrToTest);
        }
    }

    public static boolean validLong(String searchParam) {
        try {
            if(!searchParam.matches("-?[0-9]+")) return false;
            else return true;
        }
        catch(Exception e) { return false; }
    }

    public static boolean validInt(String searchParam) {
        try {
            if(!searchParam.matches("-?[0-9]+")) return false;
            else return true;
        }
        catch(Exception e) { return false; }
    }

    public static boolean validHex(String searchParam) {
        if(searchParam.length() != 64 || !searchParam.matches("-?[0-9a-fA-F]+")) return false;
        else return true;
    }

    public static BigDecimal toAion(BigDecimal bigDecimal) {
        return bigDecimal.divide(WeiRate);
    }
    public static BigDecimal toWei(BigDecimal bigDecimal){
        return bigDecimal.multiply(WeiRate);
    }

    public static int parseRequestedPage(String page){
        return page == null || !Utility.validInt(page) ? DefaultPageNumber : Integer.parseInt(page);
    }

    public static int parseRequestedSize(String size){
        if(size == null || !Utility.validInt(size)) return DefaultPageSize;
        if(size.length() >= 4) return PageSizeLimit;
        else return Integer.parseInt(size);

    }

    public static long parseDefaultEnd(String timestampEnd) {
        return timestampEnd == null || !Utility.validLong(timestampEnd) ? System.currentTimeMillis()/1000 : Long.parseLong(timestampEnd);
    }


    public static long optionalStartBlks(String timestampStart){
        return optionalStart(timestampStart, daysToSeconds(UtilityConfig.BLOCK_TIME_RANGE));

    }



    public static long optionalStartTx(String timestampStart){
        return optionalStart(timestampStart, daysToSeconds(UtilityConfig.TRANSACTION_TIME_RANGE));
    }


    public static void validateTxListPeriod(ZonedDateTime zdtStart, ZonedDateTime zdtEnd) throws Exception {
        long period = Math.abs(zdtStart.toEpochSecond() - zdtEnd.toEpochSecond());
        if (period > daysToSeconds(UtilityConfig.TRANSACTION_TIME_RANGE) ){
            throw new Exception();
        }
    }

    public static void validateBlkListPeriod(ZonedDateTime zdtStart, ZonedDateTime zdtEnd) throws Exception {
        long period = Math.abs(zdtStart.toEpochSecond() - zdtEnd.toEpochSecond());
        if (period > daysToSeconds(UtilityConfig.BLOCK_TIME_RANGE) ){
            throw new Exception();
        }
    }

    private static long optionalStart(String timestampStart, long defaultValue){
        return timestampStart == null || !Utility.validLong(timestampStart) ? (System.currentTimeMillis() /1000) - defaultValue : Long.parseLong(timestampStart);


    }


    private static long daysToSeconds(long days){
        return days * 60 * 60 * 24;
    }


    public static long parseDefaultStartYear(String timestampStartYear) {
        if (timestampStartYear == null || !Utility.validLong(timestampStartYear)) {
            return Math.max(ZonedDateTime.now().minusYears(1).toEpochSecond(), ZonedDateTime.now().withYear(2018).withDayOfYear(1).toEpochSecond());
        } else {
            return Long.parseLong(timestampStartYear);
        }
    }

    public static long parseDefaultStartMonth(String timestampStartMonth) {
        return timestampStartMonth == null || !Utility.validLong(timestampStartMonth) ? ZonedDateTime.now().minusMonths(1).toEpochSecond() : Long.parseLong(timestampStartMonth);
    }

    public static void validatePeriod(ZonedDateTime dateStart, ZonedDateTime dateEnd) throws Exception {
        Period period = Period.between(dateStart.toLocalDate(), dateEnd.toLocalDate());
        if ((period.getMonths() >=1 && period.getDays()>=1) || (period.getMonths() > 1) || (period.getYears() > 0)) {
            throw new Exception();
        }
    }

    public static void validatePeriod(LocalDate dateStart, LocalDate dateEnd) throws Exception {
        Period period = Period.between(dateStart, dateEnd);
        if ((period.getMonths() >=1 && period.getDays()>=1) || (period.getMonths() > 1) || (period.getYears() > 0)) {
            throw new Exception();
        }
    }
}
