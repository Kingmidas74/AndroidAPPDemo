package com.demo.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by midas on 18.02.18.
 */

public class Scene extends Entity  {
    @SerializedName("Tasks")
    public ArrayList<Task> Tasks = new ArrayList<Task>();
    @SerializedName("Status")
    public StatusList Status = StatusList.CREATED;
    @SerializedName("Title")
    public String Title ="";
    @SerializedName("Logs")
    public ArrayList<String> Logs = new ArrayList<String>();
    @SerializedName("Cost")
    public Double Cost = 0.0;
    @SerializedName("RenderType")
    public RenderTypeList RenderType = RenderTypeList.RENDER;

}
