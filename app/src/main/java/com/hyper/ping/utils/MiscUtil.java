package com.hyper.ping.utils;

import android.content.Context;
import android.os.Environment;

import com.hyper.ping.Constants;

import java.io.File;

public class MiscUtil {
  //根据时间长短计算语音条宽度:220dp
  public synchronized static int getVoiceLineWight(Context context, int seconds) {
    // 1-2s是最短的。
    // 2-10s每秒增加一个单位。
    // 10-60s每10s增加一个单位。
    if (seconds <= 2) {
      return dip2px(context, 90);
    } else if (seconds <= 10) {
      //90~170
      return dip2px(context, 90 + 8 * seconds);
    } else {
      //170~220
      return dip2px(context, 170 + 10 * (seconds / 10));
    }
  }

  /**
   * 根据手机的分辨率从dp单位转成为px(像素)
   */
  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * 根据手机的分辨率从px(像素)的单位转成为dp
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

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
