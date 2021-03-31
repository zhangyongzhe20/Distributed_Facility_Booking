package utils;

import java.util.ArrayList;
/**
 * @author Z. YZ
 */
public class UnMarshal {
    /**
     * Unmarshal bytes into int, using Big-Endian format
     * @param b {@code byte[]}
     * @param start {@code int} starting index
     * @return {@code int}
     */
    public static int unmarshalInteger(byte[] b, int start){
        return b[start] << 24|(b[start+1] & 0xFF) << 16|(b[start+2] & 0xFF) << 8|(b[start+3] & 0xFF);
    }


    /**
     * Unmarshal bytes into String
     * @param b {@code byte[]}
     * @param start {@code int} starting index
     * @param end {@code int} ending index
     * @return {@code String}
     */
    public static String unmarshalString(byte[] b, int start, int end){
        char[] c = new char[end - start];
        for(int i = start; i < end; i++) {
            c[i-start] = (char)(b[i]);
        }
        return new String(c);
    }
}
