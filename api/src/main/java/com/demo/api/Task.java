package com.demo.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by midas on 18.02.18.
 */

public class Task extends Entity  {
    @SerializedName("ProjectId")
    public Integer SceneId = 0;
    @SerializedName("Title")
    public String Title = "";
    @SerializedName("Camera")
    public String Camera = "";
    @SerializedName("Resolution")
    public String Resolution = "";
    @SerializedName("TaskType")
    public TaskTypes TaskType = TaskTypes.RENDER;
    @SerializedName("Frames")
    public String Frames = "";
    @SerializedName("Status")
    public StatusList Status = StatusList.CREATED;
    @SerializedName("CreateDate")
    public Date Date = new Date();
    @SerializedName("StartDate")
    public Date EndDate = new Date();
    @SerializedName("EndDate")
    public Date StartDate = new Date();
    @SerializedName("Cost")
    public Double Cost = 0.0;
}
