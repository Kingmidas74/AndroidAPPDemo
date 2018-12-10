package com.demo.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by midas on 18.02.18.
 */

public class PaymentResponseType {

    public class Result {
        @SerializedName("data")
        public String url = "";
    }

    @SerializedName("Result")
    public Result result = new Result();
}
