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
public enum ChartType {
    BAR("bar"),
    LINE("line"),
    PIE("pie"),
    DOUGHNUT("doughnut"),
    RADAR("radar"),
    POLAR_AREA("polarArea");

    private final String type;

    ChartType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
