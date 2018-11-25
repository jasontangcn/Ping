package com.hyper.ping;

public class TreeViewNode {
  private String text;
  private int level;
  private int id;
  private int parendId;
  private boolean hasChildren;
  private boolean isExpanded;
  public static final int NO_PARENT = -1;
  public static final int TOP_LEVEL = 0;

  public TreeViewNode(int id, int parendId, String text, int level, boolean hasChildren, boolean isExpanded) {
    this.id = id;
    this.parendId = parendId;
    this.text = text;
    this.level = level;
    this.hasChildren = hasChildren;
    this.isExpanded = isExpanded;
  }

  public boolean isExpanded() {
    return isExpanded;
  }

  public void setExpanded(boolean isExpanded) {
    this.isExpanded = isExpanded;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getParendId() {
    return parendId;
  }

  public void setParendId(int parendId) {
    this.parendId = parendId;
  }

  public boolean setHasChildren() {
    return hasChildren;
  }

  public void setHasChildren(boolean hasChildren) {
    this.hasChildren = hasChildren;
  }

  public boolean hasChildren() {
    return this.hasChildren;
  }
}
