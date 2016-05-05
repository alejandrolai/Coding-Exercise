package com.alejandrolai.bofa.model;

/**
 * Created by Alejandro on 5/4/16.
 */
public class Place {

    private String mId;
    private String mName;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    private String mAddress;
}
