package com.example.a2tuckj82.myshoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Creating the database with the use of open helper
public class DatabaseHelper extends SQLiteOpenHelper {
//DataBase Schema
    private static final String DB_NAME = "Items.db";
    private static final String DB_TABLE = "Items_Table";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String CREATE_TABLE = "CREATE TABLE "+ DB_TABLE+" ("+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME+ " TEXT "+ ")";

    public DatabaseHelper(Context context){
       super(context, DB_NAME, null, 1);
   }

   @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
   }

   @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DB_TABLE);
//simply throws away the data and starts over
        onCreate(sqLiteDatabase);
   }
//when on click add item is passed this will insert item name to the database
   public boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(NAME, name);
       long result = db.insert(DB_TABLE, null, contentValues);
       return result != -1;
   }
//when view data is passed this should collect and display the name of items from DB_table
   public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
   }
// when delete on click is passed this should delete item by id name from the db_table
   public Integer DeleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE, "NAME = ?",new String[] {id});
   }
//empty sting to add the first sql entry
    public boolean EmptyData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, "Your Item List:                         ");
        long result = db.insert(DB_TABLE, null, contentValues);
        return result != -1;
    }
}
