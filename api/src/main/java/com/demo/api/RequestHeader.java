package com.demo.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by midas on 17.02.18.
 */

final class RequestHeader<T> {
    public String Key = "";
    public String Value="";

    public RequestHeader(String key, String value)
    {
        Key=key;
        Value=value;
    }

    public RequestHeader(String key, List<T> values)
    {
        Key=key;
        Value=values.toString().replace("[", "")
                .replace("]", "")
                .replace(" ", "");
    }
}
