package com.hyper.ping.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

//文件管理类
public class FileUtils {
  public static File getAppDir(Context context) {
    String dirPath = "";
    //SD卡是否存在
    boolean sdCardExists = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    boolean rootDirExists = Environment.getExternalStorageDirectory().exists();
    if (sdCardExists && rootDirExists) {
      dirPath = String.format("%s/%s/", Environment.getExternalStorageDirectory().getAbsolutePath(), Constants.ROOT_PATH);
    } else {
      dirPath = String.format("%s/%s/", context.getApplicationContext().getFilesDir().getAbsolutePath(), Constants.ROOT_PATH);
    }

    File appDir = new File(dirPath);
    if (!appDir.exists()) {
      appDir.mkdirs();
    }
    return appDir;
  }

  //获取录音存放路径
  public static File getRecordDir(Context context) {
    File appDir = getAppDir(context);
    File recordDir = new File(appDir.getAbsolutePath(), Constants.RECORD_DIR_NAME);
    if (!recordDir.exists()) {
      recordDir.mkdir();
    }
    return recordDir;
  }
}
