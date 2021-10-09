package com.semihbkgr.filebench.android.net;

public interface ClientCallback<T> {

    void success(T data);

    void fail(Throwable t);

}
