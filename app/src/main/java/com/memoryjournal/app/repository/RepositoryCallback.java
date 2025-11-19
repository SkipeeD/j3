package com.memoryjournal.app.repository;

public interface RepositoryCallback<T> {
    void onSuccess(T result);

    void onError(Exception exception);
}
