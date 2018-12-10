package com.demo.api;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Midas on 18.03.2018.
 */

public class RenderSettings {
    public class Camera
    {
        public Integer idx;
        public Boolean selected;
        public String name;
    }

    public class RAM
    {
        public Integer idx;
        public Boolean selected;
        public String name;
    }

    public class StateSets
    {
        public Integer idx;
        public Boolean selected;
        public String name;
    }

    public class SceneStates
    {
        public Integer idx;
        public Boolean selected;
        public String name;
    }

    public class RenderElement
    {
        public Integer idx;
        public String name;
        public Boolean isSelected;
        public Boolean isEnabled;
        public Boolean filterOn;
        public String type;
        public ArrayList<Property> properties;
    }

    public class Property
    {
        public String Key;
        public String Value;
        public Boolean IsChanged;
    }

    public String Soft="";
    public String Name="";
    public String Render="";
    public Integer Id = 0;
    public Boolean IsDistribute=false;


    public ArrayList<Property> MaxProperties = new ArrayList<Property>();
    public ArrayList<Property> RenderProperties  = new ArrayList<Property>();

    public ArrayList<RenderElement> RenderElements = new ArrayList<RenderElement>();
    public ArrayList<StateSets> Sets  = new ArrayList<StateSets>();
    public ArrayList<SceneStates> States  = new ArrayList<SceneStates>();
    public ArrayList<Camera> Cameras = new ArrayList<Camera>();
    public ArrayList<RAM> RAMValues = new ArrayList<RAM>();
}
