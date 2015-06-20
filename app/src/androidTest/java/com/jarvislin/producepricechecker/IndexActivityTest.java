package com.jarvislin.producepricechecker;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.jarvislin.producepricechecker.activity.IndexActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class IndexActivityTest extends ActivityInstrumentationTestCase2<IndexActivity> {
    IndexActivity mActivity;
    Button mFruit;
    Button mVegetable;
    Button mSettings;

    public IndexActivityTest() {
        super(IndexActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mFruit = (Button) mActivity.findViewById(R.id.fruit);
        mVegetable = (Button) mActivity.findViewById(R.id.vegetable);
        mSettings = (Button) mActivity.findViewById(R.id.settings);
    }

    public void testPreConditions() {
        assertEquals(mFruit.isClickable(), true);
        assertEquals(mVegetable.isClickable(), true);
        assertEquals(mSettings.isClickable(), true);
    }
}