package com.jarvislin.producepricechecker.base;

import android.content.Context;
import android.support.annotation.StringRes;

public interface BaseView {
    Context getContext();

    void showToast(@StringRes int stringRes);

    void showToast(String message);

    void showLoading();

    void hideLoading();
}
