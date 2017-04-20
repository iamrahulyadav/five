package com.spate.in.Models;

/**
 * Created by shubhamagrawal on 01/04/17.
 */

public class RatingParameter {
    private double ratingStar;
    private String name;

    public RatingParameter(double ratingStar, String name) {
        this.ratingStar = ratingStar;
        this.name = name;
    }

    public RatingParameter() {
    }

    public double getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(double ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
