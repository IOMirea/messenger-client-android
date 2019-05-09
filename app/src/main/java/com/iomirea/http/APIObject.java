package com.iomirea.http;

import java.util.Date;

public abstract class APIObject {

    protected Long id;

    APIObject(long id)
    {
        this.id = id;
    }

    public Long getID() {
        return id;
    }

    public Long createTimestamp()
    {
        return ((id >> 22) + 1546300800000L);
    }

    public Date createDate()
    {
        return new java.util.Date(createTimestamp());
    }
}
