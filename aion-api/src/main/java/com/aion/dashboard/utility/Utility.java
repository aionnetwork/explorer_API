package com.aion.dashboard.utility;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("Duplicates")
public class Utility {
    private static BigDecimal WeiRate = BigDecimal.valueOf(10).pow(18);

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

    public static BigDecimal fromNano(String str){
        return new BigDecimal(new BigInteger(str, 16)).divide(WeiRate);
    }
}
