package com.quarkssoft.br.db;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "br.db";
   public static final String TABLE_NAME_BALCKLIST = "blacklist";
   public static final String BLACKLIST_COLUMN_ID = "id";
   public static final String BLACKLIST_COLUMN_PACKAGE_NAME= "package_name";
   
   private HashMap hp;

   public DBHelper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {

      db.execSQL(
      "create table " +TABLE_NAME_BALCKLIST+
      "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+BLACKLIST_COLUMN_PACKAGE_NAME+" text)"
      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_BALCKLIST);
      onCreate(db);
   }

   public boolean insertBlacklist(String package_name)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(BLACKLIST_COLUMN_PACKAGE_NAME, package_name);
      
      long status=db.insert(TABLE_NAME_BALCKLIST, null, contentValues);
      System.out.println("blacklist insert status:"+status);
      return true;
   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME_BALCKLIST+" where id="+id+"", null );
      return res;
   }
   public Cursor getData(){
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME_BALCKLIST+"", null );
	      return res;
	   }
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_BALCKLIST);
      return numRows;
   }
   
   public boolean updateBlacklist(Integer id, String name)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(BLACKLIST_COLUMN_PACKAGE_NAME, name);
      
      db.update(TABLE_NAME_BALCKLIST, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      return true;
   }

   public Integer deleteBlacklist(Integer id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TABLE_NAME_BALCKLIST, 
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
  
   public Integer deleteBlacklist()
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TABLE_NAME_BALCKLIST, 
      null, 
      null);
   }
   public ArrayList<String> getAllBlacklist()
   {
      ArrayList<String> array_list = new ArrayList<String>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME_BALCKLIST, null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
         array_list.add(res.getString(res.getColumnIndex(BLACKLIST_COLUMN_PACKAGE_NAME)));
         res.moveToNext();
      }
   return array_list;
   }
}