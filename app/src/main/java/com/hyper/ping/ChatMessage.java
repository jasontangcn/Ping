package com.hyper.ping;

public class ChatMessage {
  private static final String TAG = ChatMessage.class.getSimpleName();

  private String name;
  private String sendTime;
  private String text;
  private String duration;

  public ChatMessage(String name, String date, String text, boolean isComMsg) {
    super();
    this.name = name;
    this.sendTime = date;
    this.text = text;
    this.isIncomingMeg = isComMsg;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  private boolean isIncomingMeg = true;

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

  public boolean getMsgType() {
    return isIncomingMeg;
  }

  public void setMsgType(boolean isIncomingMeg) {
    isIncomingMeg = isIncomingMeg;
  }

  public ChatMessage() {
  }
}
