/*
 * Copyright 2014 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jarvislin.producepricechecker.path;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;

import flow.Flow;
import flow.path.PathContainerView;

public class FrameLayoutContainerView extends FrameLayout implements HandlesBack, PathContainerView, PageListener, PageResume {
    private SimplePathContainer container;
    private boolean disabled;
    private boolean isFirstTimeLoading = true;
    private View child;

    public FrameLayoutContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.container = new SimplePathContainer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !disabled && super.dispatchTouchEvent(ev);
    }

    @Override
    public ViewGroup getContainerView() {
        return this;
    }

    @Override
    public boolean onBackPressed() {
        ViewGroup childView = getCurrentChild();
        if (childView instanceof HandlesBack) {
            if (((HandlesBack) childView).onBackPressed()) {
                return true;
            }
        }
        return Flow.get(childView).goBack();
    }

    @Override
    public ViewGroup getCurrentChild() {
        return (ViewGroup) getContainerView().getChildAt(0);
    }

    @Override
    public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        disabled = true;
        isFirstTimeLoading = child == null;
        container.executeTraversal(this, traversal, new Flow.TraversalCallback() {
            @Override
            public void onTraversalCompleted() {
                callback.onTraversalCompleted();
                disabled = false;
            }
        });
    }

    @Override
    public void addView(@NonNull View child) {
        super.addView(child);
        this.child = child; //不能用 getCurrentChild()，因為有過場動畫，會造成取得的 View 是舊的
        onPageStart((ActivityComponentHelper) getContext());
        if (!isFirstTimeLoading){
            onPageResume();
        }
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        if (child instanceof PageListener) {
            ((PageListener) child).onPageStart(componentHelper);
        }
    }

    @Override
    public void onPrepareOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {
        if (child instanceof PageListener) {
            ((PageListener) child).onPrepareOptionsMenu(componentHelper, menu);
        }
    }

    @Override
    public void onPageResume() {
        if (child instanceof PageResume) {
            ((PageResume) child).onPageResume();
        }
    }
}
