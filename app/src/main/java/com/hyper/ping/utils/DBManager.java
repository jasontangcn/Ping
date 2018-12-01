package com.hyper.ping.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
    // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
    // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
  }

  public void addAudioRecord(AudioRecord record) {
    record.setId(UUID.randomUUID().toString());
    db = helper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("INSERT INTO Record VALUES(?, ?, ?, ?)", new Object[]{record.getId(), record.getPath(), record.getDuration(), record.isPlayed() ? 0 : 1});
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
    Log.e(DBManager.TAG, "添加数据库成功：" + record.toString());
  }


  public void updateAudioRecord(AudioRecord record) {
    db = helper.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put("isPlayed", record.isPlayed() ? 0 : 1);
    db.update("record", cv, "id = ?", new String[]{record.getId()});
    db.close();

  }


  public void deleteAudioRecord(AudioRecord record) {
    db = helper.getWritableDatabase();
    db.delete("AudioRecord", "id = ?", new String[]{record.getId()});
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
    Cursor c = db.rawQuery("SELECT * FROM record", null);
    while (c.moveToNext()) {
      AudioRecord record = new AudioRecord();
      record.setId(c.getString(c.getColumnIndex("id")));
      record.setPath(c.getString(c.getColumnIndex("path")));
      record.setDuration(c.getInt(c.getColumnIndex("second")));
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
