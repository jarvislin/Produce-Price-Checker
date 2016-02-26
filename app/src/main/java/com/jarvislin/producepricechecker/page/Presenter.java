package com.jarvislin.producepricechecker.page;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import flow.path.Path;

/**
 * Created by wu on 2015/06/12
 */
@EBean
public abstract class Presenter {
    private Context context;
    private Toast toast;

    public void setView(View view) {
        this.context = view.getContext();
        init(Path.get(context), view);
    }

    protected abstract void init(Path path, View view);

    public Context getContext() {
        return context;
    }

    public Resources getResources() {
        return getContext().getResources();
    }

    public String getString(@StringRes int StringResId) {
        return getContext().getString(StringResId);
    }

    @UiThread
    public void showToast(@StringRes int StringResId, int duration) {
        showToast(getString(StringResId), duration);
    }

    @UiThread
    public void showToast(String message, int duration) {
        toast = ToolsHelper.showToast(toast, getContext(), message, duration);
    }



}
