package algorithm;

import formatter.Formatter;
import primitives.Primitive;
import translator.Translator;

/**
 * This class is to implement the DES algorithm. There are several formulation functions
 * and decryption/encryption related algorithm.
 *
 * @author Wang Hewei
 */

public class DES {

    byte[] keyByteArray;

    public DES(String keyStr) {
        this.keyByteArray = keyStr.getBytes();
    }

    // an object that store primitive data or functions
    private static Primitive primitive = new Primitive();
    private static final int[] IP_Array = primitive.get_IP_Table();
    // 64 inverse initial permutation table
    private static final int[] IP1_Array = primitive.get_IP1_Table();
    private static final int[] PC1_Array = primitive.get_PC1_Table();
    // circular shift table, used for left shift of sub key
    private static final int[] LeftMove_Array = primitive.getLeftShiftTable();
    // 48 permutation table
    private static final int[] PC2_Array = primitive.get_PC2_Table();
    // 48 permutation table
    private static final int[] E_Array = primitive.get_E_Table();
    private static final int[] P_Array = primitive.get_P_Table();
    // 8 primitive functions S1-S8
    private static final int[][][] S_Box_Array = primitive.get_S_BoxTable();

    // operation of encryption and decryption, parameter flag presents encryption or decryption
    // when encryption, inputData is bytes type
    // when decryption, inputData is bytes type cipher
    public byte[] DESencrypt(byte[] inputData, int judge) {
        // Split byte data every 8 bits and ensure that the data length is an integer multiple of 8 through formatting
        Formatter formatter = new Formatter();
        byte[] keyFill = formatter.dataFormat(keyByteArray);
        byte[] dataFill = formatter.dataFormat(inputData);
        int length = dataFill.length;
        byte[] dataResult = new byte[length];
        int groups = length / 8;

        // encryption and decryption operation: it is divided into a group of 8 bytes for encryption and finally combined
        int m = 0;
        while (m < groups) {
            byte[] keyCurrent = new byte[8];
            byte[] dataCurrent = new byte[8];
            System.arraycopy(keyFill, 0, keyCurrent, 0, 8);
            System.arraycopy(dataFill, m * 8, dataCurrent, 0, 8);
            byte[] resultCur = encodeData(keyCurrent, dataCurrent, judge);
            System.arraycopy(resultCur, 0, dataResult, m * 8, 8);
            m += 1;
        }

        // if it is decryption, remove the padding bit
        byte[] dataDecrypted = null;
        if (judge == 0) {
            int dataLength = length;
            int deleteLength = dataResult[dataLength-9];
            if ((deleteLength >= 1) && (deleteLength <= 8)) {
                System.out.println("one more time");
            } else {
                deleteLength = 0;
            }
            dataDecrypted = new byte[dataLength-deleteLength-8];
            boolean judgeOfDelete = true;
            int n = 0;
            while (n < deleteLength) {
                if (deleteLength != dataResult[dataLength-9-n]) {
                    judgeOfDelete = false;
                }
                n += 1;
            }
            if (judgeOfDelete == true) {
                System.arraycopy(dataResult, 0, dataDecrypted, 0, dataLength - deleteLength - 8);
            }
        }

        if(judge == 1) {
            return dataResult;
        } else {
            return dataDecrypted;
        }
    }


    // function related to encode data, enter des_data is converted to binary byte array,
    // and des_key is converted to binary byte array, des_key refers to the key after processing length,
    // des_data refers to data after processing length, and flag refers to encryption or decryption
    private byte[] encodeData(byte[] inputKey, byte[] inputData, int judge) {
        // convert key byte array to binary byte array
        Translator translator = new Translator();
        int[] binaryKey = translator.transferBinary(inputKey);

        // convert encrypted data byte array to binary byte array
        int[] encryptData = translator.transferBinary(inputData);

        // generate sub-key
        int[][] subKey = getSubKey(binaryKey);

        // perform encryption and decryption operations
        int someJudges = judge;

        // store 64 bit data after initial transformation
        int[] M = new int[64];

        // store 64 bit data after inverse initial transformation
        int[] MIP_1 = new int[64];

        int m = 0;
        while (m < 64) {
            // Clear text IP initial transformation, physical index = logical index - 1
            M[m] = encryptData[IP_Array[m] - 1];
            m += 1;
        }

        // encryption
        if (someJudges == 1) {
            int k = 0;
            while (k < 16) {
                SeveralPermutation(M, k, someJudges, subKey);
                k += 1;
            }
        // decryption
        } else if (someJudges == 0) {
            int n = 15;
            while (n > -1) {
                SeveralPermutation(M, n, someJudges, subKey);
                n -= 1;
            }
        }
        int l = 0;
        while (l < 64) {
            // Perform inverse initial replacement IP1 operation
            MIP_1[l] = M[IP1_Array[l] - 1];
            l += 1;
        }

        // Convert 64 bit data to 8-bit bytes
        byte[] encryptResult = translator.TransferInteger(MIP_1);

        // Return the byte array after the operation is completed
        return encryptResult;
    }


    // This functions is to design to get the sub key for the inputted key.
    // In the algorithm, we first perform PC1 transformation on the key,
    // then shift the data of the sub key to the left by turns, and finally perform PC2 transformation to generate the subKeyArray.
    private int[][] getSubKey(int[] key) {
        int[][] subKeyArray = new int[16][48];
        int m = 0;
        int[] keyArray = new int[56];
        while (m < 56) {
            // PC1 conversion of key
            keyArray[m] = key[PC1_Array[m] - 1];
            m += 1;
        }
        int k = 0;
        while (k < 16) {
            // shift the data of the sub key to the left by turns
            leftShift(keyArray, LeftMove_Array[k]);
            int n = 0;
            while (n < 48) {
                subKeyArray[k][n] = keyArray[PC2_Array[n] - 1];
                n += 1;
            }
            k += 1;
        }
        return subKeyArray;
    }


    // shift the subkey to the left according to the circular shift table
    private void leftShift(int[] allKeyArray, int positionChangeValue) {
        int[] c0 = new int[28];
        int[] d0 = new int[28];
        int[] c1 = new int[28];
        int[] d1 = new int[28];
        // divide 56 bit data into two groups of 28 bit cO and cO
        int m = 0;
        while (m < 28) {
            c0[m] = allKeyArray[m];
            d0[m] = allKeyArray[m + 28];
            m += 1;
        }
        if (positionChangeValue == 1) {
            // rotate left one bit
            m = 0;
            while (m < 27) {
                c1[m] = c0[m + 1];
                d1[m] = d0[m + 1];
                m += 1;
            }
            c1[27] = c0[0];
            d1[27] = d0[0];
        } else if (positionChangeValue == 2) {
            // rotate two bits left
            m = 0;
            while (m < 26) {
                c1[m] = c0[m + 1];
                d1[m] = d0[m + 1];
                m += 1;
            }
            c1[26] = c0[0];
            d1[26] = d0[0];
            c1[27] = c0[1];
            d1[27] = d0[1];
        }
        // merge the left and right 28 bit data c1 and d1 after moving the data to the left
        int n = 0;
        while (n < 28) {
            allKeyArray[n] = c1[n];
            allKeyArray[n + 28] = d1[n];
            n += 1;
        }
    }


    // S box transformation: divide 48 bit data into 8 groups of 6 bits each,
    // and then convert them into 6 bit data through operation and recombine them
    private int[] S_Permutation(int[] inputData) {
        int[][] S_Array = new int[8][6];
        int[] boxData = new int[8];
        int[] result = new int[32];
        int m = 0;
        while (m < 8) {
            int j = 0;
            while (j < 6) {
                S_Array[m][j] = inputData[(m * 6) + j];
                j += 1;
            }
            // go through S box to get 8 numbers
            boxData[m] = S_Box_Array[m][(S_Array[m][0] << 1) + S_Array[m][5]][(S_Array[m][1] << 3) + (S_Array[m][2] << 2) + (S_Array[m][3] << 1) + S_Array[m][4]];
            // 8 numbers are transformed and output to binary
            int n = 0;
            while (n < 4) {
                result[m * 4 + 3 - n] = boxData[m] % 2;
                boxData[m] = boxData[m] / 2;
                n += 1;
            }
            m += 1;
        }
        return result;
    }


    // E transformation: make the data is changed from 32 bits to 48 bits.
    // the data after E transformation and the sub-key KeyArray[t][i] are added bitwise without carry,
    // and then perform the XOR
    private int[] E_Permutation(int[] R0, int[][] keyDoubleArray, int t) {
        int[] result = new int[48];
        int i = 0;
        while (i < 48) {
            result[i] = R0[E_Array[i] - 1];
            result[i] = result[i] + keyDoubleArray[t][i];
            // XOR: when the result of two equal numbers is 0, their arithmetic sum is 2 or 0
            if (result[i] == 2 || result[i] == 0) {
                result[i] = 0;
            }
            i += 1;
        }
        return result;
    }


    // P transformation: The 32-bit data output from the S box is used as the parameter input for P transformation.
    // P[] is a cyclic shift. RP[i] stores the nth bit of the input data according to the cyclic transformation table.
    // The left and right 32-bit data groups L and R are recombined into M, and the array M is returned.
    // During the last transformation, the left and right sides are not interchangeable.
    // Two transformations are used here to achieve invariance.
    private void P_Permutation(int[] S, int[] M, int[] L0, int[] L1, int[] R0, int[] R1, int judge, int seconds) {
        int[] RP = new int[32];
        int i = 0;
        while (i < 32) {
            // RP[i] stores the nth bit of the input data according to the circular transformation table
            RP[i] = S[P_Array[i] - 1];
            // move right to left
            L1[i] = R0[i];
            // XOR
            R1[i] = L0[i] + RP[i];
            if (R1[i] == 2 || R1[i] == 0) {
                R1[i] = 0;
            }
            // reorganize the left and right 32-bit data groups L and R into M, and return the array M
            if (judge == 0 && seconds == 0) {
                M[i] = R1[i];
                M[i + 32] = L1[i];
            } else if (judge == 1 && seconds == 15) {
                M[i] = R1[i];
                M[i + 32] = L1[i];
            } else {
                M[i] = L1[i];
                M[i + 32] = R1[i];
            }
            i += 1;
        }
    }


    // include E transformation, XOR with sub key, S box compression, P transformation, etc.
    private void SeveralPermutation(int[] M, int t, int judge, int[][] keyarray) {
        int[] L0 = new int[32];
        int[] R0 = new int[32];
        int[] L1 = new int[32];
        int[] R1 = new int[32];

        int m = 0;
        // divide 64 bit plaintext into two groups of left and right 32-bit data
        while (m < 32) {
            // initialization on the left side of cipher
            L0[m] = M[m];

            // initialization on the right side of cipher
            R0[m] = M[m + 32];
            m += 1;
        }

        // E transformation
        int[] E_Result = E_Permutation(R0, keyarray, t);

        // S box transformation
        int[] S_Result = S_Permutation(E_Result);

        // P transformation
        P_Permutation(S_Result, M, L0, L1, R0, R1, judge, t);
    }
}