package com.jarvislin.producepricechecker;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import database.Produce;

/**
 * Created by Jarvis Lin on 2015/6/15.
 */
public abstract class Fetcher {
    public abstract ArrayList<Produce> getProduces(String url);
    public abstract int getDataOffset();
}
