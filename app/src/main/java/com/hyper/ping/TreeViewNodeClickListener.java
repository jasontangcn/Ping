package com.hyper.ping;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class TreeViewNodeClickListener implements AdapterView.OnItemClickListener {
  private TreeViewAdapter treeViewAdapter;

  public TreeViewNodeClickListener(TreeViewAdapter treeViewAdapter) {
    this.treeViewAdapter = treeViewAdapter;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    TreeViewNode treeNode = (TreeViewNode) treeViewAdapter.getItem(position);
    List<TreeViewNode> topNodes = treeViewAdapter.getTopNodes();
    List<TreeViewNode> allNodes = treeViewAdapter.getAllNodes();

    if (!treeNode.hasChildren()) {
      return;
    }

    if (treeNode.isExpanded()) {
      treeNode.setExpanded(false);
      List<TreeViewNode> deleted = new ArrayList<TreeViewNode>();
      for (int i = position + 1; i < topNodes.size(); i++) {
        if (treeNode.getLevel() >= topNodes.get(i).getLevel())
          break;
        deleted.add(topNodes.get(i));
      }
      topNodes.removeAll(deleted);
      treeViewAdapter.notifyDataSetChanged();
    } else {
      treeNode.setExpanded(true);
      int n = 1;
      for (TreeViewNode node : allNodes) {
        if (node.getParendId() == treeNode.getId()) {
          node.setExpanded(false);
          topNodes.add(position + n, node);
          n++;
        }
      }
      treeViewAdapter.notifyDataSetChanged();
    }
  }

}
