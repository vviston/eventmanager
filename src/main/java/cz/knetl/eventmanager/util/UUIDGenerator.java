package cz.knetl.eventmanager.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Unique random Id generator
 * */
public class UUIDGenerator {

    private static SecureRandom RANDOM = null;

    private UUIDGenerator(){
    }


    /*
     * Generate 21 digits number ID
     * */
    public static String getNext(){
        final Long timestamp =  new Date().getTime();
        final SecureRandom rnd = getRandom();
        final BigInteger randomNumber = BigInteger.valueOf(10000000L + rnd.nextInt(90000000));
        final StringBuilder sbUuid = new StringBuilder();
        sbUuid.append(randomNumber).append(timestamp);

        return sbUuid.toString();

    }

    private static SecureRandom getRandom(){
        if(RANDOM == null){
            RANDOM = new SecureRandom();
        }
        return RANDOM;
    }


}
