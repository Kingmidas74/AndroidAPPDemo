package com.demo.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by midas on 18.02.18.
 */

public class Payment extends Entity  {
    @SerializedName("Title")
    public String Title = "";
    @SerializedName("Date")
    public Date Date = new Date();
    @SerializedName("Cost")
    public Double Cost = 0.0;
}
