package com.tekadept.gsontest.app;

public class TestPojo {
    private int value1 = 1;
    private String value2 = "abc";
    private int values[] = {1, 2, 3, 4};
    private transient int value3 = 3;

    // no args ctor
    TestPojo() {
    }
}
