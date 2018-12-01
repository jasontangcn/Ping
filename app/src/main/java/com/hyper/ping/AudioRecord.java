package com.hyper.ping;

public class AudioRecord {
  private String id;
  private String path;
  private int duration;
  private boolean isPlayed;//是否已经播放过
  private boolean isPlaying;//是否正在播放

  public boolean isPlaying() {
    return isPlaying;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setPlaying(boolean playing) {
    isPlaying = playing;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public boolean isPlayed() {
    return isPlayed;
  }

  public void setPlayed(boolean played) {
    isPlayed = played;
  }

  @Override
  public String toString() {
    return "AudioRecord{" + "id='" + id + '\'' + ", path='" + path + '\'' + ", duration=" + duration + ", isPlayed=" + isPlayed + ", isPlaying=" + isPlaying + '}';
  }
}
