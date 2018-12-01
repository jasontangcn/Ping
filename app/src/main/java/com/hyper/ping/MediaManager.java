package com.hyper.ping;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

import java.io.IOException;

public class MediaManager {
  private static final String TAG = MediaManager.class.getSimpleName();
  private static MediaPlayer mediaPlayer;
  private static boolean paused;
  static SensorManager sensorManager;
  static Sensor sensor;
  private static Context context;
  static AudioManager audioManager;

  public static void playSound(String filePath, OnCompletionListener completionListener) {
    if (mediaPlayer == null) {
      mediaPlayer = new MediaPlayer();
      //保险起见，设置报错监听
      mediaPlayer.setOnErrorListener(new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
          mediaPlayer.reset();
          return false;
        }
      });
    } else {
      mediaPlayer.reset();
    }

    try {
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mediaPlayer.setOnCompletionListener(completionListener);
      mediaPlayer.setDataSource(filePath);
      mediaPlayer.prepare();
      mediaPlayer.start();
    } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }

  public static void playSound(String filePath, OnCompletionListener completionListener, Context context) {
    if (mediaPlayer == null) {
      mediaPlayer = new MediaPlayer();
      //保险起见，设置报错监听
      mediaPlayer.setOnErrorListener(new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
          mediaPlayer.reset();
          return false;
        }
      });
    } else {
      mediaPlayer.reset();
    }

    try {
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mediaPlayer.setOnCompletionListener(completionListener);
      mediaPlayer.setDataSource(filePath);
      mediaPlayer.prepare();
      mediaPlayer.start();
      registerProximitySensor(context);
    } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }

  private static void registerProximitySensor(Context context) {
    MediaManager.context = context;
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    sensorManager.registerListener(new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        try {
          float mProximiny = event.values[0];
          if (mProximiny >= sensor.getMaximumRange()) {
            changeAdapterType(true);
          } else {
            changeAdapterType(false);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {

      }
    }, sensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  public static void pause() {
    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
      paused = true;
    }
  }

  public static void resume() {
    if (mediaPlayer != null && paused) {
      mediaPlayer.start();
      paused = false;
    }
  }

  public static void release() {
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

  //切换声筒或听筒
  private static void changeAdapterType(boolean on) {
    ((Activity) context).setVolumeControlStream(AudioManager.STREAM_SYSTEM);
    pause();
    if (on) {
      //扩音声筒
      audioManager.setMicrophoneMute(false);
      audioManager.setSpeakerphoneOn(true);
      audioManager.setMode(AudioManager.MODE_NORMAL);
      Log.e(TAG, "changeAdapterType: 当前为扩音模式");
    } else {
      //耳麦听筒
      audioManager.setSpeakerphoneOn(false);
      audioManager.setMicrophoneMute(true);
      audioManager.setMode(AudioManager.MODE_NORMAL);
      audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
      Log.e(TAG, "changeAdapterType: 当前为耳麦模式");
    }
    resume();
  }
}
