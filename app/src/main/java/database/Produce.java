package database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
@ModelContainer
@Table(databaseName = ProduceDatabase.NAME)
public class Produce extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    int id;

    @Column
    String name;

    @Column
    String type;

    @Column
    String topPrice;

    @Column
    String mediumPrice;

    @Column
    String lowPrice;

    @Column
    String averagePrice;

    @Column
    String kind;
}
