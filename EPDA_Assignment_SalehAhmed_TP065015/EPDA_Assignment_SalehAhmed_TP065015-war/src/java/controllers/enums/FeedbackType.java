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
public enum FeedbackType {
    POSITIVE("Positive"),
    NEGATIVE("Negative");

    private final String type;

    FeedbackType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    @Override
    public String toString() {
        return type;
    }
}