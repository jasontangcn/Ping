package com.hyper.ping.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hyper.ping.AudioRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBManager {
  public static final String TAG = DBManager.class.getSimpleName();

  private DBHelper helper;
  private SQLiteDatabase db;

  public DBManager(Context context) {
    helper = new DBHelper(context);
    // getWritableDatabase内部调用了context.openOrCreateDatabase(name, 0, factory);
    // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
  }

  public void addAudioRecord(AudioRecord record) {
    record.setId(UUID.randomUUID().toString());
    db = helper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("INSERT INTO audiorecords VALUES(?, ?, ?, ?)", new Object[]{record.getId(), record.getFilePath(), record.getDuration(), record.isPlayed() ? 0 : 1});
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }


  public void updateAudioRecord(AudioRecord record) {
    db = helper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("isPlayed", record.isPlayed() ? 0 : 1);
    db.update("audiorecords", values, "id = ?", new String[]{record.getId()});
    db.close();

  }


  public void deleteAudioRecord(AudioRecord record) {
    db = helper.getWritableDatabase();
    db.delete("audiorecords", "id = ?", new String[]{record.getId()});
    db.close();

  }

  /**
   * retrieveAudioRecords all persons, return list
   *
   * @return List<Person>
   */
  public List<AudioRecord> retrieveAudioRecords() {
    db = helper.getWritableDatabase();
    ArrayList<AudioRecord> records = new ArrayList<AudioRecord>();
    Cursor c = db.rawQuery("SELECT * FROM audiorecords", null);
    while (c.moveToNext()) {
      AudioRecord record = new AudioRecord();
      record.setId(c.getString(c.getColumnIndex("id")));
      record.setFilePath(c.getString(c.getColumnIndex("path")));
      record.setDuration(c.getInt(c.getColumnIndex("duration")));
      record.setPlayed(c.getInt(c.getColumnIndex("isPlayed")) == 0 ? true : false);
      records.add(record);
    }
    c.close();
    return records;
  }

  public void close() {
    db.close();
  }
}
