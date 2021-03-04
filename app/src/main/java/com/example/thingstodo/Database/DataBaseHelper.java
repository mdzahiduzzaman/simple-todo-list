package com.example.thingstodo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.thingstodo.Model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "toDoListDatabase";
    private static final String TABLE_NAME = "toDoListTable";
    private static final String ID = "id";
    private static final String LIST_ITEM = "list_item";
    private static final String CHECKED_STATUS = "checked_status";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LIST_ITEM + " TEXT, " + CHECKED_STATUS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertItem(ListModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_ITEM, model.getListItem());
        values.put(CHECKED_STATUS, 0);
        db.insert(TABLE_NAME, null, values);
    }

    public void updateItem(int id, String item) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_ITEM, item);
        db.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateCheckedStatus(int id, int checkedStatus) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CHECKED_STATUS, checkedStatus);
        db.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<ListModel> getListItems() {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ListModel> list = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    do {
                        ListModel listItem = new ListModel();
                        listItem.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        listItem.setListItem(cursor.getString(cursor.getColumnIndex(LIST_ITEM)));
                        listItem.setCheckedStatus(cursor.getInt(cursor.getColumnIndex(CHECKED_STATUS)));
                        list.add(listItem);
                    } while(cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }

        return list;
    }

}
