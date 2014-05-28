package com.tekadept.gsontest.app;

import com.google.gson.annotations.SerializedName;

public class JsonValidate {

    public String object_or_array;
    public boolean empty;
    public long parse_time_nanoseconds;
    @SerializedName("validate")
    public boolean isValid;
    public int size;
}
