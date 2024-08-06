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
public enum BaseRoute {
    CONTEXT("EPDA_Assignment_SalehAhmed_TP065015-war");

    private final String path;

    BaseRoute(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
