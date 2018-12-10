package com.demo.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by midas on 18.02.18.
 */

public class Archive extends Entity {
    @SerializedName("Date")
    public Date Date = new Date();
    @SerializedName("Title")
    public String Title ="";
    @SerializedName("Size")
    public Double Size = 0.0;
}
