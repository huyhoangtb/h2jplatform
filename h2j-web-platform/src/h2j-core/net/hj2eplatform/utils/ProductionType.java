/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

/**
 *
 * @author HuyHoang
 */
public enum ProductionType {

    TRAVEL_SERVICE(0), TOUR(1), TOUR_GROUP(11), HOTEL(2), ARTICLE(3), CATEGORY(4);
    private Integer type;

    private ProductionType(Integer type) {
        this.type = type;
    }

    public int toInteger() {
        return this.type;
    }

    public static ProductionType getInstance(int type) {
        switch (type) {
            case 1:
                return TOUR;
            case 2:
                return HOTEL;
            case 3:
                return ARTICLE;
        }
        return null;
    }

    public String toString() {
        return this.type.toString();
    }
}
