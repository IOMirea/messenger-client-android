package com.iomirea.http;

import java.util.Date;

public abstract class APIObject {
    protected String id;

    APIObject(String id)
    {
        // TODO: Figure out how to make Gson deserialize id as Long or switch to other library
        this.id = id;
    }

    public Long getID() {
        return Long.valueOf(id);
    }

    public Long createTimestamp()
    {
        return ((getID() >> 22) + 1546300800000L);
    }

    public Date createDate()
    {
        return new java.util.Date(createTimestamp());
    }
}
