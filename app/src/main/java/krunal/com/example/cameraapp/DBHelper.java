package krunal.com.example.cameraapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mycamera.db";
    public static final String IMAGES_TABLE_NAME = "images";
    public static final String IMAGES_COLUMN_ID = "id";
    public static final String IMAGES_COLUMN_FILEPATH = "filePath";
    public static final String IMAGES_COLUMN_COMMENT = "comment";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table images " +
                        "(id integer primary key, filePath text,comment text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS images");
        onCreate(db);
    }

    public boolean insertContact (String filePath, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("filePath", filePath);
        contentValues.put("comment", comment);
        db.insert("images", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from images where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, IMAGES_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String filePath, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("filePath", filePath);
        contentValues.put("comment", comment);
        db.update("images", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("images",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Image> getAllCotacts() {
        ArrayList<Image> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from images", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Image(res.getString(res.getColumnIndex(IMAGES_COLUMN_FILEPATH)), res.getString(res.getColumnIndex(IMAGES_COLUMN_COMMENT))));
            res.moveToNext();
        }
        return array_list;
    }
}