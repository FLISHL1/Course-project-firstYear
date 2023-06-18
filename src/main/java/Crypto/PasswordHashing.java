package Crypto;

import org.mindrot.jbcrypt.BCrypt;
public class PasswordHashing {
    public static String HashPassword(String password){
        // Генерация хеша пароля
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // Проверка соответствия введенного пароля хешу
        boolean passwordsMatch = BCrypt.checkpw(password, hashedPassword);
        return passwordsMatch? hashedPassword:null;
    }
    public static Boolean checkPass(String inputPassword, String hashPassword){
        return BCrypt.checkpw(inputPassword, hashPassword);
    }
}
