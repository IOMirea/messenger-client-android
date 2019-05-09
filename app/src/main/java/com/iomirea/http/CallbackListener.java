package com.iomirea.http;

public abstract class CallbackListener<T> {
    public abstract void callback(Callback<T> cb);
}
