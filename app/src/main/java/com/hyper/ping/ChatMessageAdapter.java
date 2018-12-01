package com.hyper.ping;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

  public static interface IMsgViewType {
    int IMVT_COM_MSG = 0;
    int IMVT_TO_MSG = 1;
  }

  private static final String TAG = ChatMessageAdapter.class.getSimpleName();

  private List<ChatMessage> messages;

  private Context context;

  private LayoutInflater inflater;
  private MediaPlayer mediaPlayer = new MediaPlayer();

  public ChatMessageAdapter(Context context, List<ChatMessage> messages) {
    this.context = context;
    this.messages = messages;
    this.inflater = LayoutInflater.from(context);
  }

  public int getCount() {
    return messages.size();
  }

  public Object getItem(int position) {
    return messages.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public int getItemViewType(int position) {
    ChatMessage entity = messages.get(position);

    if (entity.getMsgType()) {
      return IMsgViewType.IMVT_COM_MSG;
    } else {
      return IMsgViewType.IMVT_TO_MSG;
    }

  }

  public int getViewTypeCount() {
    return 2;
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    final ChatMessage entity = messages.get(position);
    boolean isComMsg = entity.getMsgType();

    ViewHolder viewHolder = null;
    if (convertView == null) {
      if (isComMsg) {
        convertView = inflater.inflate(R.layout.chat_message_left, null);
      } else {
        convertView = inflater.inflate(R.layout.chat_message_right, null);
      }

      viewHolder = new ViewHolder();
      viewHolder.sendTime = (TextView) convertView.findViewById(R.id.msgSendTime);
      viewHolder.username = (TextView) convertView.findViewById(R.id.msgUsername);
      viewHolder.content = (TextView) convertView.findViewById(R.id.msgContent);
      viewHolder.duration = (TextView) convertView.findViewById(R.id.msgDuration);
      viewHolder.isComMsg = isComMsg;

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.sendTime.setText(entity.getSendTime());

    if (entity.getText().contains(".amr")) {
      viewHolder.content.setText("");
      viewHolder.content.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chat_to_voice_playing, 0);
      viewHolder.duration.setText(entity.getDuration());
    } else {
      viewHolder.content.setText(entity.getText());
      viewHolder.content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      viewHolder.duration.setText("");
    }
    viewHolder.content.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        if (entity.getText().contains(".amr")) {
          playMusic(android.os.Environment.getExternalStorageDirectory() + "/" + entity.getText());
        }
      }
    });
    viewHolder.username.setText(entity.getName());

    return convertView;
  }

  static class ViewHolder {
    public TextView sendTime;
    public TextView username;
    public TextView content;
    public TextView duration;
    public boolean isComMsg = true;
  }

  /**
   * @param name
   * @Description
   */
  private void playMusic(String name) {
    try {
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
      }
      mediaPlayer.reset();
      mediaPlayer.setDataSource(name);
      mediaPlayer.prepare();
      mediaPlayer.start();
      mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {

        }
      });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void stop() {
  }
}
