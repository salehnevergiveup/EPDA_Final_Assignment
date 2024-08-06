/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.enums;

/**
 *
 * @author saleh
 */
public enum ServletFile {
    LOGIN("Login"),
    FORGETPASSWORD("ForgetPassword"),
    LOGOUT("Logout"),
    REGISTER("Register"),
    INDEX("Index"),
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    VIEW("View"),
    DASHBOARD("Dashboard"),
    UPDATE_PASSWORD("UpdatePassword"),
    BOOTSTRAP_APP("SeederServlet");
    

    private final String path;

    ServletFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
