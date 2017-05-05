package com.jarvislin.producepricechecker.base;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;


public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    protected T presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = createPresenter();
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showToast(@StringRes int stringRes) {
        if (isAdded()) {
            getBaseActivity().showToast(stringRes);
        }
    }

    @Override
    public void showToast(String message) {
        if (isAdded()) {
            getBaseActivity().showToast(message);
        }
    }

    @Override
    public void showLoading() {
        if (isAdded()) {
            getBaseActivity().showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (isAdded()) {
            getBaseActivity().hideLoading();
        }
    }

    private BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}