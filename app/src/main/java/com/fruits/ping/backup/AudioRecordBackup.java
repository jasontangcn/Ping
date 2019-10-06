package com.fruits.ping.backup;

public class AudioRecordBackup {
  private String id;
  private String filePath;
  private int duration;
  private boolean isPlayed;
  private boolean isPlaying;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFilePath() {
    return this.filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public int getDuration() {
    return this.duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public boolean isPlayed() {
    return this.isPlayed;
  }

  public boolean isPlaying() {
    return this.isPlaying;
  }

  public void setPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }

  public void setPlayed(boolean isPlayed) {
    this.isPlayed = isPlayed;
  }

  @Override
  public String toString() {
    return "AudioRecordBackup{" + "id='" + id + '\'' + ", filePath='" + filePath + '\'' + ", duration=" + duration + ", isPlayed=" + isPlayed + ", isPlaying=" + isPlaying + '}';
  }
}
