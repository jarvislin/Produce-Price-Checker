package com.jarvislin.producepricechecker.util;

import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Jarvis Lin on 2015/6/19.
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultString("109")
    String fruitMarketList();

    @DefaultString("109")
    String vegetableMarketList();

    @DefaultString("customer")
    String userMode();

    @DefaultString("")
    String fruitUpdateDate();

    @DefaultString("")
    String vegetableUpdateDate();

    @DefaultFloat(0.6f)
    float unit();

    @DefaultString("")
    String bookmarks();

    @DefaultFloat(0.3f)
    float lowProfit();

    @DefaultFloat(0.5f)
    float hightProfit();

}
