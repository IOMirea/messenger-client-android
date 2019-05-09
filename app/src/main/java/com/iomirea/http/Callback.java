package com.iomirea.http;

import com.android.volley.VolleyError;

public class Callback<T> {
    private final T obj;
    private final VolleyError error;
    private final boolean success;

    Callback(T response) {
        this.obj = response;
        this.error = null;
        this.success = true;
    }

    Callback(VolleyError e) {
        this.obj = null;
        this.error = e;
        this.success = false;
    }

    public T getObj() {
        return obj;
    }

    public VolleyError getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
