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
public enum JspFile {
    CREATE("create.jsp"),
    INDEX("index.jsp"),
    VIEW("view.jsp"),
    UPDATE("update.jsp"),
    UPDATE_PASSWORD("update_password.jsp"),
    LOGIN("login.jsp"),
    FORGETPASSWORD("forgetPassword.jsp"),
    REGISTER("register.jsp"),
    STYLE("style.jsp"),
    ERROR_401("401.jsp"),
    ERROR_403("403.jsp"),
    ERROR_404("404.jsp"),
    ERROR_405("405.jsp"),
    DASHBOARD("dashboard.jsp"),
    BOOTSTRAP_APP("bootstrapApp.jsp");

    private final String path;

    JspFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
