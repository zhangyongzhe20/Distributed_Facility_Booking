import org.apache.commons.lang.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Marshal {
    /**
     * Marshal int into bytes, using Big-Endian format
     * @param x {@code int}
     * @return {@code byte[]}
     */
    public static byte[] marshalInt(int x){
        return new byte[]{
                (byte)(x >> 24),
                (byte)(x >> 16),
                (byte)(x >> 8),
                (byte)(x)
        };
    }

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
     * Marshal String into bytes
     * @param s {@code String}
     * @return {@code byte[]}
//   * @throws UnsupportedEncodingExceptions
     */
    public static byte[] marshalString(String s) {
        byte[] ret = new byte[s.length()];
        for(int i = 0; i < s.length(); i++) {
            ret[i] = (byte)s.charAt(i);
        }
        return ret;
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

    /**
     *
     * @param collectedMsg {@code ArrayList<Object>} collect data in each service
     * @return {@code byte[]} ready for UDP
     */
    public static byte[] marshalMsg(ArrayList<Object> collectedMsg){
        // create a list<Byte>
        List<Byte> constructedMsg = new ArrayList<>();
        // loop the collectedMsg and marshall
        for(Object obj : collectedMsg){
            if(obj.getClass() == Integer.class){
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt((Integer) obj))));
            }else if(obj.getClass() == String.class){
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalString((String) obj))));
            }
        }
        // convert Byte[] to byte[] for UDP
        return ArrayUtils.toPrimitive(constructedMsg.toArray(new Byte[0]));
    }
}
