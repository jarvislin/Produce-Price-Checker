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
    public String name;

    @Column
    public String type;

    @Column
    public String topPrice;

    @Column
    public String mediumPrice;

    @Column
    public String lowPrice;

    @Column
    public String averagePrice;

    @Column
    public String kind;
}
