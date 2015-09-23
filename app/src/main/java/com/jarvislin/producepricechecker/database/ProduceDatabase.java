package com.jarvislin.producepricechecker.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
@Database(name = ProduceDatabase.NAME, version = ProduceDatabase.VERSION)
public class ProduceDatabase {
        public static final String NAME = "Produces";
        public static final int VERSION = 3;
}
