package com.hyper.ping;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TreeViewAdapter extends BaseAdapter {
  public static final int DEFAULT_INDENT = 30; /* dp */

  private List<TreeViewNode> topNodes;
  private List<TreeViewNode> allNodes;
  private LayoutInflater inflater;
  private int indent;

  public TreeViewAdapter(List<TreeViewNode> topNodes, List<TreeViewNode> allNodes, LayoutInflater inflater) {
    this(topNodes, allNodes, inflater, DEFAULT_INDENT);
  }

  public TreeViewAdapter(List<TreeViewNode> topNodes, List<TreeViewNode> allNodes, LayoutInflater inflater, int indent) {
    this.topNodes = topNodes;
    this.allNodes = allNodes;
    this.inflater = inflater;
    this.indent = indent;
  }

  public List<TreeViewNode> getTopNodes() {
    return topNodes;
  }

  public List<TreeViewNode> getAllNodes() {
    return allNodes;
  }

  @Override
  public int getCount() {
    return topNodes.size();
  }

  @Override
  public Object getItem(int position) {
    return topNodes.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = inflater.inflate(R.layout.contact_item, null);
      holder.avator = (ImageView) convertView.findViewById(R.id.contact_avator);
      holder.text = (TextView) convertView.findViewById(R.id.contact_name);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    TreeViewNode node = topNodes.get(position);
    int level = node.getLevel();
    holder.avator.setPadding(indent * (level + 1), holder.avator.getPaddingTop(), holder.avator.getPaddingRight(), holder.avator.getPaddingBottom());
    holder.text.setText(node.getText());
    if (node.hasChildren() && !node.isExpanded()) {
      holder.avator.setImageResource(R.drawable.cat_128px);
      // Need to set it visible, because convertView may be a *reused* view that is invisible.
      holder.avator.setVisibility(View.VISIBLE);
    } else if (node.hasChildren() && node.isExpanded()) {
      holder.avator.setImageResource(R.drawable.cat_128px);
      holder.avator.setVisibility(View.VISIBLE);
    } else if (!node.hasChildren()) {
      holder.avator.setImageResource(R.drawable.cat_128px);
      holder.avator.setVisibility(View.VISIBLE);
    }
    return convertView;
  }

  static class ViewHolder {
    ImageView avator;
    TextView text;
  }
}
