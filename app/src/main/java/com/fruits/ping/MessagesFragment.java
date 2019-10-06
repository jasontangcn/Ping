package com.fruits.ping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesFragment extends Fragment {
  private ListView messagesLV;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = View.inflate(getActivity(), R.layout.messages_fragment, null);

    messagesLV = (ListView) view.findViewById(R.id.messages);
    final List<MessageAbstract> messages = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
    String date = format.format(new Date());
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messages.add(new MessageAbstract("Tom", date, "Hello Tom!"));
    messages.add(new MessageAbstract("Jason", date, "Hello Jason!"));
    messages.add(new MessageAbstract("Louis", date, "Hello Louis!"));
    messagesLV.setAdapter(new MessagesListViewAdapter(messages));

    return view;
  }

  public class MessagesListViewAdapter extends BaseAdapter {
    private List<MessageAbstract> senders;

    public MessagesListViewAdapter(List<MessageAbstract> senders) {
      this.senders = senders;
    }

    @Override
    public int getCount() {
      return senders.size();
    }

    @Override
    public Object getItem(int position) {
      return senders.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;

      if (convertView == null) {
        viewHolder = new ViewHolder();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        convertView = inflater.inflate(R.layout.message_item, null);
        viewHolder.sender = (TextView) convertView.findViewById(R.id.message_sender);
        viewHolder.time = (TextView) convertView.findViewById(R.id.message_sent_time);
        viewHolder.message = (TextView) convertView.findViewById(R.id.message_abstract);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.sender.setText(senders.get(position).sender);
      viewHolder.time.setText(senders.get(position).time);
      viewHolder.message.setText(senders.get(position).content);

      return convertView;
    }
  }

  static class ViewHolder {
    TextView sender;
    TextView time;
    TextView message;
  }

  public class MessageAbstract {
    private String sender;
    private String time;
    private String content;

    public MessageAbstract(String sender, String time, String content) {
      this.sender = sender;
      this.time = time;
      this.content = content;
    }
  }
}
