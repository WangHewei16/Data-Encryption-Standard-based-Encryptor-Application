package translator;

/**
 * This class is the class including two different functions related to transfer data.
 * The first one is to transfer 64-bit binary data to eight integers, while the second one is to transfer the data into binary format
 *
 * @author Wang Hewei
 */

public class Translator {
    // convert the data in the array storing 64-bit binary data to eight integers
    public byte[] TransferInteger(int[] data) {
        byte[] ByteResult = new byte[8];
        int m = 0;
        int n;
        while (m < 8) {
            n = 0;
            while (n < 8) {
                ByteResult[m] += (data[(m << 3) + n] << (7 - n));
                n += 1;
            }
            m += 1;
        }
        int k = 0;
        while (k < 8) {
            ByteResult[k] %= 256;
            if (!(ByteResult[k] < 128) && !(ByteResult[k] == 128)) {
                ByteResult[k] -= 255;
            }
            k += 1;
        }
        return ByteResult;
    }


    // transfer the data into binary format, then store into the byte array
    public int[] transferBinary(byte[] byteData) {
        int[] binaryArray = new int[8];
        int m = 0;
        while (m < 8) {
            binaryArray[m] = byteData[m];
            if (binaryArray[m] < 0) {
                binaryArray[m] += 256;
                binaryArray[m] %= 256;
            }
            m += 1;
        }
        int[] byteArray = new int[64];
        int k = 0;
        int n;
        while (k < 8) {
            n = 0;
            while (n < 8) {
                byteArray[k * 8 + 7 - n] = binaryArray[k] % 2;
                binaryArray[k] = binaryArray[k] / 2;
                n += 1;
            }
            k += 1;
        }
        return byteArray;
    }
}

