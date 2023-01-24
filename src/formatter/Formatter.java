package formatter;

/**
 * This class is to format the data which less than 8-bit.
 *
 * @author Wang Hewei
 */

public class Formatter {
    // data format refer to the supplement operation of less than 8-bit multiple bytes
     public byte[] dataFormat(byte[] inputData) {
        int len = inputData.length;
        int supplement = 8 - len % 8;
        byte[] fillData = new byte[supplement+len];
        System.arraycopy(inputData, 0, fillData, 0, len);
        int m = len;
        while (m < len + supplement) {
            fillData[m] = (byte) supplement;
            m += 1;
        }
        return fillData;
    }
}
