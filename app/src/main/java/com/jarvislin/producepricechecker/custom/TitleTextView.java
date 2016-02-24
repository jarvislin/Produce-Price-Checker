package com.jarvislin.producepricechecker.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarvislin.producepricechecker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jarvis Lin on 2016/2/24.
 */
@EViewGroup(R.layout.title_text_view)
public class TitleTextView extends LinearLayout {
    @ViewById
    TextView title;
    @ViewById
    TextView content;

    private String titleText, contentText;

    public TitleTextView(Context context) {
        super(context);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleTextView, 0, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {

            switch (a.getIndex(i)) {
                case R.styleable.TitleTextView_title_text:
                    titleText = a.getString(R.styleable.TitleTextView_title_text);
                    break;
                case R.styleable.TitleTextView_content_text:
                    contentText = a.getString(R.styleable.TitleTextView_content_text);
                    break;
                default:
                    break;
            }
        }

        a.recycle();
    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    public void init() {
        setTitle(titleText);
        setText(contentText);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public String getText() {
        return content.getText().toString();
    }

    public void setText(String string) {
        content.setText(string);
    }

}