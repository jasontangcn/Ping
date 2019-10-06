package com.fruits.ping;

import android.media.MediaRecorder;
import android.os.Environment;

public class SoundMeter {
  static final private double EMA_FILTER = 0.6;

  private MediaRecorder mediaRecorder = null;
  private double EMA = 0.0;

  public void start(String name) {
    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      return;
    }
    if (mediaRecorder == null) {
      mediaRecorder = new MediaRecorder();
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/" + name);
      try {
        mediaRecorder.prepare();
        mediaRecorder.start();
        EMA = 0.0;
      } catch (Exception e) {
        e.printStackTrace();
        ;
      }
    }
  }

  public void stop() {
    if (mediaRecorder != null) {
      mediaRecorder.stop();
      mediaRecorder.release();
      mediaRecorder = null;
    }
  }

  public void pause() {
    if (mediaRecorder != null) {
      mediaRecorder.stop();
    }
  }

  public void start() {
    if (mediaRecorder != null) {
      mediaRecorder.start();
    }
  }

  public double getAmplitude() {
    if (mediaRecorder != null) return (mediaRecorder.getMaxAmplitude() / 2700.0);
    else return 0;

  }

  public double getAmplitudeEMA() {
    double amp = getAmplitude();
    EMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * EMA;
    return EMA;
  }
}
