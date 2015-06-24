package database;

import android.database.sqlite.SQLiteDatabase;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
@Database(name = ProduceDatabase.NAME, version = ProduceDatabase.VERSION)
public class ProduceDatabase {
        public static final String NAME = "Produces";
        public static final int VERSION = 2;
}
