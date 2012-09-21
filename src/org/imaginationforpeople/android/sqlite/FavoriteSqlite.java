package org.imaginationforpeople.android.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteSqlite extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "i4p.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "favorites";
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_NAME + " ( " +
			"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"language_code TEXT NOT NULL, " +
			"slug TEXT NOT NULL," +
			"title TEXT NOT NULL);";

	public FavoriteSqlite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	public void addFavorite(I4pProjectTranslation project) {
		ContentValues values = new ContentValues();
		values.put("language_code", project.getLanguageCode());
		values.put("slug", project.getSlug());
		values.put("title", project.getTitle());
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	public void removeFavorite(I4pProjectTranslation project) {
		String[] data = {project.getLanguageCode(), project.getSlug()};
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "language_code = ? AND slug = ?", data);
		db.close();
	}
	
	public boolean isFavorite(I4pProjectTranslation project) {
		String[] data = {project.getLanguageCode(), project.getSlug()};
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, "language_code = ? AND slug = ?", data, null, null, null);
		boolean isFavorite = (c.getCount() == 1);
		db.close();
		return isFavorite;
	}
	
	public boolean hasFavorites() {
		String[] columns = {"id"};
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
		boolean hasFavorites = (c.getCount() >= 1);
		db.close();
		return hasFavorites;
	}
	
	public List<I4pProjectTranslation> getFavorites() {
		ArrayList<I4pProjectTranslation> projects = new ArrayList<I4pProjectTranslation>();
		
		String[] columns = {"language_code", "slug", "title"};
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				I4pProjectTranslation project = new I4pProjectTranslation();
				project.setLanguageCode(c.getString(0));
				project.setSlug(c.getString(1));
				project.setTitle(c.getString(2));
				projects.add(project);
				c.moveToNext();
			}
		}
		db.close();
		
		return projects;
	}
}
