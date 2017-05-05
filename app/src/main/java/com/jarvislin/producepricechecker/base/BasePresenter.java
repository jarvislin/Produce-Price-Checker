package com.jarvislin.producepricechecker.base;

import android.content.Context;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class BasePresenter<T extends BaseView> {

    private final T view;
    private Reference<T> reference;

    public BasePresenter(T view) {
        this.view = view;
        reference = new WeakReference<>(view);
    }

    protected void errorHandling(Throwable throwable) {

    }

    protected void detachView() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
    }

    protected Context getContext() {
        return view.getContext();
    }
}
