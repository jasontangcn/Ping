package com.hyper.ping;

public class ChatMessageX {
  private static final String TAG = ChatMessageX.class.getSimpleName();
  private int id;
  private boolean isVoice;
  private boolean isIncoming;

  private String sender;
  private String sendTime;
  private String content;

  private String duration;
  private String filePath;
  private boolean isPlayed;
  private boolean isPlaying;

  public ChatMessageX() {
  }

  public ChatMessageX(int id, boolean isVoice, boolean isIncoming, String sender, String sendTime, String content, String duration, String filePath, boolean isPlayed, boolean isPlaying) {
    this.id = id;
    this.isVoice = isVoice;
    this.isIncoming = isIncoming;

    this.sender = sender;
    this.sendTime = sendTime;
    this.content = content;
    this.duration = duration;
    this.filePath = filePath;
    this.isPlayed = isPlayed;
    this.isPlaying = isPlaying;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getSendTime() {
    return sendTime;
  }

  public void setSendTime(String sendTime) {
    this.sendTime = sendTime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public boolean isVoice() {
    return isVoice;
  }

  public void setVoice(boolean voice) {
    isVoice = voice;
  }

  public boolean isIncoming() {
    return isIncoming;
  }

  public void setIncoming(boolean incoming) {
    isIncoming = incoming;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public boolean isPlayed() {
    return isPlayed;
  }

  public void setPlayed(boolean played) {
    isPlayed = played;
  }

  public boolean isPlaying() {
    return isPlaying;
  }

  public void setPlaying(boolean playing) {
    isPlaying = playing;
  }
}
