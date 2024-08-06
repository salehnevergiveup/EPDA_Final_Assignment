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
public enum ServletPackage {
    PROFILES("Profiles"),
    JOBS("Jobs"),
    COMMENTS("Comments"),
    FEEDBACK("Feedbacks"),
    APPLICATIONS("Applications"),
    USERS("Users"),
    WARNINGS("Warnings"),
    REPORTS("Reports");

    private final String path;

    ServletPackage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
