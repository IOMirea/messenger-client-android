package com.iomirea.http;

import java.util.Date;
import java.lang.Long;

public abstract class APIObject {

    protected Long id;

    public APIObject(long id)
    {
        this.id = id;
    }

    public Long idToUnixTimeStamp()
    {
        return ((id >> 22) + 1546300800000L);
    }

    public Date idToDate()
    {
        return new java.util.Date(id);
    }
}
