package database;


import android.content.ContentValues;

import com.jarvislin.producepricechecker.ProduceData;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Insert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
public class DatabaseController {
    public static ArrayList<ProduceData> getProduces(String kind) {
        List<Produce> list = new Select().from(Produce.class).where(Condition.column(Produce$Table.KIND).
                is(kind)).queryList();
        return convertData(list);
    }

    public static void clearTable() {
        Delete.table(Produce.class);
    }

    public static void clearTable(String kind) {
        new Delete()
                .from(Produce.class)
                .where(Condition.column(Produce$Table.KIND).is(kind)).query();
    }

    public static void insert(ArrayList<ProduceData> list, String kind) {
        Produce produce;
        for (ProduceData data : list) {
            produce = new Produce();
            produce.name = data.getName();
            produce.type = data.getType();
            produce.topPrice = data.getTopPrice();
            produce.mediumPrice = data.getMidPrice();
            produce.lowPrice = data.getLowPrice();
            produce.averagePrice = data.getAvgPrice();
            produce.kind = kind;
            produce.save();
        }
    }

    private static ArrayList<ProduceData> convertData(List<Produce> list) {
        ArrayList<ProduceData> dataList = new ArrayList<>();
        ProduceData data;
        for (Produce produce : list) {
            data = new ProduceData(new String[]{produce.name, produce.type, produce.topPrice, produce.mediumPrice, produce.lowPrice, produce.averagePrice});
            dataList.add(data);
        }
        return dataList;
    }
}
