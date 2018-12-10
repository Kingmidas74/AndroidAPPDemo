package com.demo.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by midas on 18.02.18.
 */

public class Node extends Entity  {
    @SerializedName("NodeName")
    public String NodeName = "";
    @SerializedName("FrameName")
    public String FrameName = "";
    @SerializedName("InfoStatus")
    public String InfoStatus = "";
    @SerializedName("ImgBase64")
    public String ImgBase64="";
}
