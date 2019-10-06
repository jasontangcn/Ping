package com.fruits.ping;

public class ChatMessage {
  private static final String TAG = ChatMessage.class.getSimpleName();
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

  public ChatMessage() {
  }

  public ChatMessage(int id, boolean isVoice, boolean isIncoming, String sender, String sendTime, String content, String duration, String filePath, boolean isPlayed, boolean isPlaying) {
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
    this.isVoice = voice;
  }

  public boolean isIncoming() {
    return isIncoming;
  }

  public void setIncoming(boolean incoming) {
    this.isIncoming = incoming;
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
    this.isPlayed = played;
  }

  public boolean isPlaying() {
    return isPlaying;
  }

  public void setPlaying(boolean playing) {
    this.isPlaying = playing;
  }

  @Override
  public String toString() {
    return "ChatMessage{" + "id=" + id + ", isVoice=" + isVoice + ", isIncoming=" + isIncoming + ", sender='" + sender + '\'' + ", sendTime='" + sendTime + '\'' + ", content='" + content + '\'' + ", duration='" + duration + '\'' + ", filePath='" + filePath + '\'' + ", isPlayed=" + isPlayed + ", isPlaying=" + isPlaying + '}';
  }
}
