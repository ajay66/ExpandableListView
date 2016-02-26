package com.example.explv_webservice5;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends DatabaseHelper {

	private final String TABLE_CATEGORIES = "Categories";

	public DBHelper(Context context) throws IOException {
		super(context);
	}

	public void insertCategory(String id, String data) throws Exception {
		// deleteAllData(WHERE_TO_STAY);
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues cv = new ContentValues();

			cv.put("cat_id", id);
			cv.put("response", data);

			db.insert(TABLE_CATEGORIES, null, cv);
			db.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void updateCategory(String id, String data) throws Exception {
		// deleteAllData(WHERE_TO_STAY);
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues cv = new ContentValues();

			cv.put("cat_id", id);
			cv.put("response", data);

			// db.insert(table, nullColumnHack, values)
			// db.insert(TABLE_CATEGORIES, null, cv);
			// db.update(table, values, whereClause, whereArgs)
			db.update(TABLE_CATEGORIES, cv, "cat_id='" + id + "'", null);
			db.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void insertSlideImagesContent(String lang_id, String loc_id,
			String data) throws Exception {
	}

	public void insertDestinationCategory1(String id, String data,
			String language_id) throws Exception {
	}

	public void updateDestinationCategory(String uniqueName, String data)
			throws Exception {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("response", data);
		db.update(TABLE_CATEGORIES, cv, "cat_id" + "=?",
				new String[] { uniqueName });
	}

	public void deleteAll(String tablename) {
		SQLiteDatabase db = this.getWritableDatabase();
		// return db.delete(tablename, null, null);

		Log.d("query", tablename);
		db.execSQL(tablename);
	}

	public Boolean checkDB(String cat) {

		Boolean flag = false;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query("Categories", null, "cat_id='" + cat + "'", null,
				null, null, null);
		// db.query(table, columns, selection, selectionArgs, groupBy, having,
		// orderBy)

		// Cursor
		// c=db.rawQuery("Select cat_id,response from Categories where cat_id='"+cat+"'",
		// null);

		// String id2 = c.getString( c.getColumnIndex(0));
		// System.out.println("id"+id);
		System.out.println("bb" + flag);
		// System.out.println("id2"+id2);
		int row = c.getCount();
		System.out.println("no of rows" + row);
		// if(row!=0)
		// {
		// String id = c.getString( c.getColumnIndex("0") );
		// System.out.println("id"+id);
		// }
		// for(int i=0;i<row;i++)
		c.moveToFirst();
		if (row != 0) {
			for (int i = 0; i < row; i++) {
				System.out.println("for loop");
				String id = c.getString(0);
				System.out.println("id" + id);
				if (id.equals(cat)) {
					flag = true;
				}
			}
		}
		// if (c != null) {
		// flag = true;
		// System.out.println("bb"+flag);
		// }
		return flag;
	}

}