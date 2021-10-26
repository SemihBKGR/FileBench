package com.semihbkgr.filebench.android.net;

public interface ClientCallback<T> {

    void success(T data);

    void error(ErrorModel errorModel);

    void fail(Throwable t);

}
