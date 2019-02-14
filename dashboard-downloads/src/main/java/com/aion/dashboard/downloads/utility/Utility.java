package com.aion.dashboard.downloads.utility;

import com.aion.dashboard.downloads.exceptions.ApiInvalidRequestException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Utility {
    private Utility() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    private static BigDecimal weiRate = BigDecimal.valueOf(10).pow(18);

    private static final Pattern ACCOUNT_REGEX = Pattern.compile("(0x)?(0{60,64}|a0)[A-Fa-f0-9]*");

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

    public static String isValidAddress(String strAddr){
        if (strAddr == null) {
            return StringUtils.EMPTY;
        } else {
            String addrToTest = strAddr.startsWith("0x") ? strAddr.substring(2) : strAddr;
            if (addrToTest.length() != 64) {
                return StringUtils.EMPTY;
            } else if(ACCOUNT_REGEX.matcher(addrToTest).find() || AddressWhiteList.contains(addrToTest)) {
                return strAddr.toLowerCase();
            } else {
                return StringUtils.EMPTY;
            }
        }
    }

    public static boolean validLong(String searchParam) {
        return searchParam.matches("-?[0-9]+");
    }

    public static boolean validInt(String searchParam) {
        return searchParam.matches("-?[0-9]+");
    }

    public static boolean validHex(String searchParam) {
        return searchParam.length() == 64 && searchParam.matches("-?[0-9a-fA-F]+");
    }

    public static BigDecimal toAion(BigDecimal bigDecimal){
        return bigDecimal.divide(weiRate);
    }

    public static long parseDefaultEnd(String timestampEnd) throws ApiInvalidRequestException {
        if (timestampEnd == null || !Utility.validLong(timestampEnd)) {
            return System.currentTimeMillis() / 1000;
        } else {
            long ts =  ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestampEnd)).truncatedTo(ChronoUnit.DAYS), ZoneId.of("UTC")).toEpochSecond();
            ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")).withYear(2018).withMonth(4).withDayOfMonth(23);
            if(ts < zdt.toEpochSecond() || ts > ZonedDateTime.now().toEpochSecond()) throw new ApiInvalidRequestException("Invalid Request: timestampEnd is invalid");
            else return ts;
        }
    }

    public static long parseDefaultStartYear(String timestampStartYear) throws ApiInvalidRequestException {
        if (timestampStartYear == null || !Utility.validLong(timestampStartYear)) {
            return Math.max(ZonedDateTime.now().minusYears(1).toEpochSecond(), ZonedDateTime.now().withYear(2018).withDayOfYear(1).toEpochSecond());
        } else {
            long ts =  ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestampStartYear)).truncatedTo(ChronoUnit.DAYS), ZoneId.of("UTC")).toEpochSecond();
            ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")).withYear(2018).withMonth(4).withDayOfMonth(23);
            if(ts < zdt.toEpochSecond() || ts > ZonedDateTime.now().toEpochSecond()) throw new ApiInvalidRequestException("Invalid Request: timestampStart is invalid");
            else return ts;
        }
    }

    public static long parseDefaultStartMonth(String timestampStartMonth) throws ApiInvalidRequestException {
        if(timestampStartMonth == null || !Utility.validLong(timestampStartMonth)) {
            return ZonedDateTime.now().minusMonths(1).toEpochSecond();
        } else {
            long ts =  ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestampStartMonth)).truncatedTo(ChronoUnit.DAYS), ZoneId.of("UTC")).toEpochSecond();
            ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")).withYear(2018).withMonth(4).withDayOfMonth(23);
            if(ts < zdt.toEpochSecond() || ts > ZonedDateTime.now().toEpochSecond()) throw new ApiInvalidRequestException("Invalid Request: timestampStart is invalid");
            else return ts;
        }
    }
}