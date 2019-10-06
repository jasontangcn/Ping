package com.fruits.ping.backup;

public class ChatMessage2 {
  private static final String TAG = ChatMessage2.class.getSimpleName();

  private String name;
  private String sendTime;
  private String text;
  private String duration;

  public ChatMessage2(String name, String date, String text, boolean isIncomingMsg) {
    super();
    this.name = name;
    this.sendTime = date;
    this.text = text;
    this.isIncomingMsg = isIncomingMsg;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  private boolean isIncomingMsg = true;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSendTime() {
    return sendTime;
  }

  public void setSendTime(String sendTime) {
    this.sendTime = sendTime;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public boolean isIncomingMsg() {
    return isIncomingMsg;
  }

  public void isIncomingMsg(boolean isIncomingMsg) {
    this.isIncomingMsg = isIncomingMsg;
  }

  public ChatMessage2() {
  }
}
