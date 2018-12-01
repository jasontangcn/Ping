package com.hyper.ping;

public class ChatMessage {
  private static final String TAG = ChatMessage.class.getSimpleName();

  private String sender;
  private String sendTime;
  private String content;
  private String duration;

  public ChatMessage() {

  }

  public ChatMessage(String sender, String date, String content, boolean isIncomingMsg) {
    this.sender = sender;
    this.sendTime = date;
    this.content = content;
    this.isIncomingMsg = isIncomingMsg;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  private boolean isIncomingMsg = true;

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
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean isIncomingMsg() {
    return isIncomingMsg;
  }

  public void isIncomingMsg(boolean isIncomingMsg) {
    this.isIncomingMsg = isIncomingMsg;
  }
}
