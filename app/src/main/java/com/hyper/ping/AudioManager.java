package com.hyper.ping;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.util.UUID;

public class AudioManager {
  public static final String TAG = AudioManager.class.getSimpleName();

  private static AudioManager instance;

  private MediaRecorder mediaRecorder;
  private String dir;
  private String filePath;
  private boolean prepared;

  public AudioManagerListener audioManagerListener;

  private AudioManager(String dir) {
    this.dir = dir;
  }

  public static AudioManager getInstance(String dir) {
    if (instance == null) {
      synchronized (AudioManager.class) {
        if (instance == null) {
          instance = new AudioManager(dir);

        }
      }
    }
    return instance;
  }

  public void setAudioManagerListener(AudioManagerListener audioManagerListener) {
    this.audioManagerListener = audioManagerListener;
  }

  /*
  1.
   */
  public void prepare() {
    try {
      prepared = false;
      File dir = new File(this.dir);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      String fileName = genFileName();
      File file = new File(dir, fileName);
      filePath = file.getAbsolutePath();

      mediaRecorder = new MediaRecorder();
      mediaRecorder.setOutputFile(this.filePath);
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      // output format: amr
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
      // 采用AAC是为了适配IOS，保证IOS上可以正常播放
      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
      // 严格遵守google官方api给出的MediaRecorder的状态流程图
      mediaRecorder.prepare();
      mediaRecorder.start();
      prepared = true;
      if (audioManagerListener != null) {
        audioManagerListener.prepared();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String genFileName() {
    return UUID.randomUUID().toString() + ".amr";
  }

  // 获取声音的level
  public int getVoiceLevel(int maxLevel) {
    // MediaRecorder.getMaxAmplitude()是音频的振幅范围，值域是1-32767
    if (prepared) {
      try {
        // 取证+1，否则取不到7
        return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return 1;
  }

  public void release() {
    // 严格按照api流程进行
    if (mediaRecorder == null) return;

    /*
     * 这里处理异常
     */
    try {
      // 下面三个参数必须加，不加的话会奔溃，
      // mediarecorder.stop()报错RuntimeException:stop failed
      mediaRecorder.setOnErrorListener(null);
      mediaRecorder.setOnInfoListener(null);
      mediaRecorder.setPreviewDisplay(null);
      mediaRecorder.stop();
    } catch (Exception e) {
      Log.i(AudioManager.TAG, Log.getStackTraceString(e));
    }
    mediaRecorder.release();
    prepared = false;
    mediaRecorder = null;
  }

  // 取消,因为prepare时产生了一个文件，所以cancel方法应该要删除这个文件，
  // 这是与release的方法的区别
  public void cancel() {
    release();
    if (filePath != null) {
      File file = new File(filePath);
      file.delete();
      filePath = null;
    }

  }

  public String getFilePath() {
    return filePath;
  }

  public interface AudioManagerListener {
    void prepared();
  }
}
