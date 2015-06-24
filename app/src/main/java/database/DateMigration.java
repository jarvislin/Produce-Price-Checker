package database;

import android.database.sqlite.SQLiteDatabase;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by Jarvis Lin on 2015/6/24.
 */
@Migration(version = 2, databaseName = ProduceDatabase.NAME)
public class DateMigration extends AlterTableMigration<Produce> {

    public DateMigration() {
        super(Produce.class);
    }

    @Override
    public void onPreMigrate() {
        // Simple ALTER TABLE migration wraps the statements into a nice builder notation
        super.onPreMigrate();
        addColumn(String.class, "date");
    }
}
