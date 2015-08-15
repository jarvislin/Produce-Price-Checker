package database;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.UpdateTableMigration;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
@Migration(version = 3, databaseName = ProduceDatabase.NAME)
public class Migration02 extends AlterTableMigration<Produce> {
    public Migration02() {
        super(Produce.class);
    }

    @Override
    public void onPreMigrate() {
        // Simple ALTER TABLE migration wraps the statements into a nice builder notation
        super.onPreMigrate();
        addColumn(String.class, "produceName");
        addColumn(String.class, "produceNumber");
        addColumn(String.class, "middlePrice");
        addColumn(String.class, "transactionDate");
        addColumn(String.class, "transactionAmount");
        addColumn(String.class, "mainCategory");
        addColumn(String.class, "subCategory");
        addColumn(String.class, "marketNumber");
        addColumn(String.class, "marketName");
    }
}
