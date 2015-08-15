package database;


import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
public class DatabaseController {
//    public static ArrayList<Produce> getProduces(String category) {
//        return new ArrayList<Produce>(new Select().from(Produce.class).where(Condition.column(Produce$Table.KIND).
//                is(category)).queryList());
//
//    }
//
//    public static boolean isBookmark(String name, String type, String category) {
//        if(TextUtils.isEmpty(name)) {
//            return false;
//        }
//        return !new ArrayList<Produce>(new Select().from(Produce.class)
//                .where(Condition.column(Produce$Table.PRODUCENAME).is(name))
//                .and(Condition.column(Produce$Table.TYPE).is(type))
//                .and(Condition.column(Produce$Table.KIND).is(category))
//                .queryList()).isEmpty();
//    }
//
//    public static ArrayList<Produce> getBookmarks(String category) {
//        return new ArrayList<Produce>(new Select().from(Produce.class)
//                .where(Condition.column(Produce$Table.KIND).is(category))
//                .queryList());
//    }
//
    public static void clearTable() {
        Delete.table(Produce.class);
    }
//
//    public static void clearTable(String category) {
//        new Delete()
//                .from(Produce.class)
//                .where(Condition.column(Produce$Table.KIND).is(category)).query();
//    }
//
//    public static void delete(String name, String type, String category) {
//        new Delete()
//                .from(Produce.class)
//                .where(Condition.column(Produce$Table.PRODUCENAME).is(name))
//                .and(Condition.column(Produce$Table.TYPE).is(type))
//                .and(Condition.column(Produce$Table.KIND).is(category))
//                .query();
//    }
//
//    public static void insertBookmark(Produce object, String category) {
//        Produce bookmark = new Produce();
//        bookmark.produceName = object.produceName;
//        bookmark.type = object.type;
//        bookmark.topPrice = object.topPrice;
//        bookmark.mediumPrice = object.mediumPrice;
//        bookmark.lowPrice = object.lowPrice;
//        bookmark.averagePrice = object.averagePrice;
//        bookmark.date = object.date;
//        bookmark.category = category;
//        bookmark.save();
//    }
//
//    public static void updateBookmark(ArrayList<Produce> produces, String category) {
//        ArrayList<Produce> bookmarks = getProduces(category);
//        for(Produce bookmark : bookmarks){
//            for(Produce produce : produces){
//                if(bookmark.produceName.equals(produce.produceName) && bookmark.type.equals(produce.type)){
//                    Condition[] conditions = {
//                            Condition.column(Produce$Table.TOPPRICE).eq(produce.topPrice)
//                            ,Condition.column(Produce$Table.MEDIUMPRICE).eq(produce.mediumPrice)
//                            ,Condition.column(Produce$Table.LOWPRICE).eq(produce.lowPrice)
//                            ,Condition.column(Produce$Table.AVERAGEPRICE).eq(produce.averagePrice)
//                            ,Condition.column(Produce$Table.DATE).eq(produce.date)
//                    };
//
//                    new Update(Produce.class)
//                            .set(conditions)
//                            .where(Condition.column(Produce$Table.TYPE).is(produce.type))
//                            .and(Condition.column(Produce$Table.PRODUCENAME).is(produce.produceName))
//                            .and(Condition.column(Produce$Table.KIND).is(category))
//                            .queryClose();
//                }
//            }
//        }
//
//    }
}
