package com.fruits.ping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = View.inflate(getActivity(), R.layout.contacts_fragment, null);

    ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.contacts);
    ContactsAdapter adapter = new ContactsAdapter(getActivity());

    final List<String> groups = new ArrayList<String>();
    groups.add("Friends");
    groups.add("Colleagues");

    final List<List<String>> children = new ArrayList<List<String>>();
    List<String> item1 = new ArrayList<String>();
    item1.add("Lucy");
    item1.add("Tom");
    children.add(item1);
    List<String> item2 = new ArrayList<String>();
    item2.add("Jason");
    item2.add("Sam");
    item2.add("Lily");

    children.add(item2);

    adapter.setData(groups, children);
    expandableListView.setAdapter(adapter);


    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
      /**
       * @param parent ExpandableListView
       * @param v Groupview
       * @param groupPosition
       * @param id
       * @return
       */
      @Override
      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Toast.makeText(getActivity(), "Clicked：" + groups.get(groupPosition), Toast.LENGTH_SHORT).show();
        return false;
      }
    });

    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
      /**
       * @param parent ExpandableListView
       * @param v Groupview
       * @param groupPosition
       * @param childPosition
       * @param id
       * @return
       */
      @Override
      public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(getActivity(), "Clicked：" + children.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
        return false;
      }
    });

    return view;
  }
}
