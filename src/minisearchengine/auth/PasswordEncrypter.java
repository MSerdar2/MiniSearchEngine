package minisearchengine.auth;

import minisearchengine.util.FSUtilities;

import java.io.File;
import java.util.Arrays;

public class PasswordEncrypter {

    public static final String PW_PATH = System.getProperty("user.dir") + File.separator + "auth" + File.separator + "pw.dat";

    /**
     * Takes in a password, and turns it into 32 byte hash
     * @param password Password
     * @return 32-byte hash of given password
     */
    public static byte[] encrypt(String password) {
        return Sha256.hash(password.getBytes());
    }

    /**
     * Reads the saved password from the hard storage.
     * @return Byte array of saved password
     */
    public static byte[] loadCurrentPassword() {
        byte[] encryptedPassword = FSUtilities.readBytes(PW_PATH);
        return encryptedPassword.length == 0 ? null : encryptedPassword;
    }

    /**
     * Overwrites given password on the hard storage
     * @param newPassword Password to be overwritten with
     */
    public static void overwritePassword(String newPassword) {
        byte[] encrypted = encrypt(newPassword);
        FSUtilities.writeBytes(PW_PATH, encrypted);
    }

    /**
     * Compares input password with saved hash version
     * @param source Un-encrypted password input
     * @param targetEncrypted Encrypted password input
     * @return Whether they are equal or not
     */
    public static boolean compare(String source, byte[] targetEncrypted) {
        byte[] currentEncrypted = encrypt(source);
        return Arrays.equals(currentEncrypted, targetEncrypted);
    }

}
