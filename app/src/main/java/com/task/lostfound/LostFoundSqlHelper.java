package com.task.lostfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LostFoundSqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "LostFoundSqlHelper";
    //声明数据库帮助器的实例
    public static LostFoundSqlHelper lostFoundSqlHelper = null;
    //声明数据库的实例
    private SQLiteDatabase db = null;
    //声明数据库的名称
    public static final String DB_NAME = "lostfound.db";
    //声明表的名称
    public static final String TABLE_NAME = "lostfound";
    //声明数据库的版本号
    public static int DB_VERSION = 1;

    public LostFoundSqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public LostFoundSqlHelper(@Nullable Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    public static LostFoundSqlHelper getInstance(Context context, int version) {
        if (lostFoundSqlHelper == null && version > 0) {
            lostFoundSqlHelper = new LostFoundSqlHelper(context, version);
        } else if (lostFoundSqlHelper == null) {
            lostFoundSqlHelper = new LostFoundSqlHelper(context);
        }
        return lostFoundSqlHelper;
    }

    public SQLiteDatabase openWriteLink() {
        if (db == null || !db.isOpen()) {
            db = lostFoundSqlHelper.getWritableDatabase();
        }
        return db;
    }

    public SQLiteDatabase openReadLink() {
        if (db == null || !db.isOpen()) {
            db = lostFoundSqlHelper.getReadableDatabase();
        }
        return db;
    }

    //关闭数据库的读连接
    public void closeLink() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //如果存在user_info表，则删除该表
        String drop_sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(drop_sql);
        //创建user_info表
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name VARCHAR NOT NULL,"
                + "post_type VARCHAR NOT NULL,"
                + "phone VARCHAR NOT NULL,"
                + "description VARCHAR NOT NULL,"
                + "location VARCHAR NOT NULL,"
                + "update_time VARCHAR NOT NULL,"
                + "lat VARCHAR NOT NULL,"
                + "lng VARCHAR NOT NULL"
                + ");";
        db.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //根据指定条件删除表记录
    public int delete(String condition,String id) {
        // 执行删除记录动作，该语句返回删除记录的数目
        //参数一：表名
        //参数二：whereClause where子句
        //参数三：whereArgs 您可以在 where 子句中包含 ?s，
        // 它将被 whereArgs 中的值替换。这些值将绑定为字符串。
        int result=-1;
        try{
           result=db.delete(TABLE_NAME, condition, new String[]{id});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //删除该表所有记录
    public int deleteAll() {
        // 执行删除记录动作，该语句返回删除记录的数目
        return db.delete(TABLE_NAME, "1=1", null);
    }

    // 往该表添加一条记录
    public long insert(LostFound lostFound) {
        List<LostFound> infoList = new ArrayList<>();
        infoList.add(lostFound);
        return insert(infoList);
    }

    // 往该表添加多条记录
    public long insert(List<LostFound> infoList) {
        long result = -1;
        for (int i = 0; i < infoList.size(); i++) {
            LostFound lostFound = infoList.get(i);
            // 不存在唯一性重复的记录，则插入新记录
            ContentValues cv = new ContentValues();
            cv.put("name", lostFound.getName());
            cv.put("post_type", lostFound.getPostType());
            cv.put("phone", lostFound.getPhone());
            cv.put("description", lostFound.getDescription());
            cv.put("location", lostFound.getLocation());
            cv.put("update_time", lostFound.getDate());
            cv.put("lat", lostFound.getLat());
            cv.put("lng", lostFound.getLng());
            // 执行插入记录动作，该语句返回插入记录的行号
            //参数二：参数未设置为NULL,参数提供可空列名称的名称，以便在 cv 为空的情况下显式插入 NULL。
            //参数三：values 此映射包含行的初始列值。键应该是列名，值应该是列值
            result = db.insert(TABLE_NAME, "", cv);
            // 添加成功则返回行号，添加失败则返回-1
            if (result == -1) {
                return result;
            }
        }
        return result;
    }

    //根据条件更新指定的表记录
    public int update(LostFound lostFound, String condition) {
        ContentValues cv = new ContentValues();
        cv.put("name", lostFound.getName());
        cv.put("post_type", lostFound.getPostType());
        cv.put("phone", lostFound.getPhone());
        cv.put("description", lostFound.getDescription());
        cv.put("location", lostFound.getLocation());
        cv.put("update_time", lostFound.getDate());
        cv.put("lat", lostFound.getLat());
        cv.put("lng", lostFound.getLng());
        //执行更新记录动作，该语句返回更新的记录数量
        //参数二：values 从列名到新列值的映射
        //参数三：whereClause 更新时要应用的可选 WHERE 子句
        //参数四：whereArgs 您可以在 where 子句中包含 ?s，
        //它将被 whereArgs 中的值替换。这些值将绑定为字符串。
        return db.update(TABLE_NAME, cv, condition, null);
    }

    public int update(LostFound lostFound) {
        // 执行更新记录动作，该语句返回更新的记录数量
        return update(lostFound, "rowid=" + lostFound.getName());
    }

    public List<LostFound> query(String condition) {
        String sql = String.format("select rowid,_id,name,post_type,phone,description,location,update_time,lat,lng" +
                " from %s", TABLE_NAME);
        List<LostFound> infoList = new ArrayList<>();
        // 执行记录查询动作，该语句返回结果集的游标
        //参数一:SQL查询
        //参数二:selectionArgs
        //您可以在查询的 where 子句中包含 ?s，它将被 selectionArgs 中的值替换。这些值将绑定为字符串。
        Cursor cursor = db.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            LostFound lostFound = new LostFound();
            //Xxx getXxx(columnIndex):根据字段下标得到对应的值
            //int getColumnIndex():根据字段名得到对应的下标
            //cursor.getLong()：以 long 形式返回所请求列的值。
            //getColumnIndex() 获取给定列名的从零开始的列索引,如果列名不存在返回-1
            int name_idx = cursor.getColumnIndex("name");
            int post_type_idx = cursor.getColumnIndex("post_type");
            int phone_idx = cursor.getColumnIndex("phone");
            int update_time_idx = cursor.getColumnIndex("update_time");
            int location_idx = cursor.getColumnIndex("location");
            int description_idx = cursor.getColumnIndex("description");
            int id_idx = cursor.getColumnIndex("_id");
            int lat_idx = cursor.getColumnIndex("lat");
            int lng_idx = cursor.getColumnIndex("lng");
            lostFound.setId(cursor.getInt(id_idx));
            lostFound.setName(cursor.getString(name_idx));
            lostFound.setPostType(cursor.getString(post_type_idx));
            lostFound.setPhone(cursor.getString(phone_idx));
            lostFound.setDate(cursor.getString(update_time_idx));
            lostFound.setDescription(cursor.getString(description_idx));
            lostFound.setLocation(cursor.getString(location_idx));
            lostFound.setLat(cursor.getString(lat_idx));
            lostFound.setLng(cursor.getString(lng_idx));
            infoList.add(lostFound);
        }
        //查询完毕，关闭数据库游标
        cursor.close();
        return infoList;
    }
}

