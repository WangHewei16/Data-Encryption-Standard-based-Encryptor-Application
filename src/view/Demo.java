package view;

/**
 * This class is the main class that init the encryptor and decryptor interface.
 * In this class, two GUI object are called and two interface are shown in the window
 *
 * @author Wang Hewei
 */

public class Demo {
    public static void main(String[] args) {
        GUI_Decryptor gui_decryption = new GUI_Decryptor();
        GUI_Encryptor gui_encryption = new GUI_Encryptor();
        gui_decryption.initUI();
        gui_encryption.initUI();
    }
}
