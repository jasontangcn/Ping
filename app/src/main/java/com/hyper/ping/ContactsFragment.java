package com.hyper.ping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {
  private ArrayList<TreeViewNode> topNodes;
  private ArrayList<TreeViewNode> allNodes;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = View.inflate(getActivity(), R.layout.contacts_fragment, null);

    topNodes = new ArrayList<TreeViewNode>();
    allNodes = new ArrayList<TreeViewNode>();
    TreeViewNode node1= new TreeViewNode(0, TreeViewNode.NO_PARENT, "JiangSu", TreeViewNode.TOP_LEVEL, true, false);
    TreeViewNode node2= new TreeViewNode(1, node1.getId(), "Suzhou", TreeViewNode.TOP_LEVEL + 1, true, false);
    TreeViewNode node3= new TreeViewNode(2, node2.getId(), "SIP", TreeViewNode.TOP_LEVEL + 2, true, false);
    TreeViewNode node4= new TreeViewNode(3, TreeViewNode.NO_PARENT, "Guangdong", TreeViewNode.TOP_LEVEL , true, false);
    TreeViewNode node5= new TreeViewNode(4, node4.getId(), "Shenzhen", TreeViewNode.TOP_LEVEL + 1, true, false);
    TreeViewNode node6= new TreeViewNode(5, node5.getId(), "Nanshan", TreeViewNode.TOP_LEVEL + 2, false, false);
    topNodes.add(node1);
    topNodes.add(node4);
    allNodes.add(node1);
    allNodes.add(node2);
    allNodes.add(node3);
    allNodes.add(node4);
    allNodes.add(node5);
    allNodes.add(node6);

    //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //inflater = getActivity().getLayoutInflater();

    ListView contactsLV = (ListView) view.findViewById(R.id.contacts);
    TreeViewAdapter treeViewAdapter = new TreeViewAdapter(topNodes, allNodes, inflater);
    TreeViewNodeClickListener treeViewItemClickListener = new TreeViewNodeClickListener(treeViewAdapter);
    contactsLV.setAdapter(treeViewAdapter);
    contactsLV.setOnItemClickListener(treeViewItemClickListener);

    return view;
  }
}
