package com.hyper.ping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = View.inflate(getActivity(), R.layout.contacts_fragment, null);
    return view;
  }
}
