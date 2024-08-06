/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author saleh
 */
public class Validation {
    public static boolean checkEmail(String email) {
        email = email.trim();
        if (email.length() == 0) {
            return false;
        }
        if (email.indexOf('@') == -1) {
            return false;
        }
        return email.indexOf('.') != -1;
    }

    public static boolean checkPassword(String password) {
        return password.length() >= 8;
    }
}
