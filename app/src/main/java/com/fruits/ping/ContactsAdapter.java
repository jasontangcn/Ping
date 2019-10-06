package com.fruits.ping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter<T> extends BaseExpandableListAdapter {
  Context context;

  private List<String> groups;
  private List<List<T>> children;

  public ContactsAdapter(Context context) {
    this.context = context;
  }

  public void setData(List<String> groups, List<List<T>> children) {
    this.groups = groups;
    this.children = children;
  }

  @Override
  public int getGroupCount() {
    return groups.size();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return 0;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.contact_group, null);
    TextView txttTitle = (TextView) view.findViewById(R.id.contactGroupName);
    txttTitle.setText(getGroup(groupPosition));
    txttTitle.setPadding(30, 0, 0, 0);
    return view;
  }

  @Override
  public String getGroup(int groupPosition) {
    return groups.get(groupPosition);
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return children.get(groupPosition).size();
  }

  @Override
  public T getChild(int groupPosition, int childPosition) {
    return children.get(groupPosition).get(childPosition);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return 0;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
    TextView txtContent = (TextView) view.findViewById(R.id.contactName);
    txtContent.setText(getChild(groupPosition, childPosition).toString());
    txtContent.setPadding(30, 0, 0, 0);
    return view;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}
