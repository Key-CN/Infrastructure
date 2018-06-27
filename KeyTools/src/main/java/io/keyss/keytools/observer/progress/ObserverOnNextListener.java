package io.keyss.keytools.observer.progress;

public interface ObserverOnNextListener<T> {

    public void onNext(T t);

    public void onError(Throwable e);
}
