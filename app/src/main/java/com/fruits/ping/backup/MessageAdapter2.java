package com.fruits.ping.backup;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fruits.ping.R;

import java.util.List;

public class MessageAdapter2 extends BaseAdapter {

  public static interface IMsgViewType {
    int IMVT_INCOMING_MSG = 0;
    int IMVT_OUTGOING_MSG = 1;
  }

  private static final String TAG = MessageAdapter2.class.getSimpleName();

  private List<ChatMessage2> messages;

  private Context context;

  private LayoutInflater inflater;
  private MediaPlayer mediaPlayer = new MediaPlayer();

  public MessageAdapter2(Context context, List<ChatMessage2> messages) {
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
    ChatMessage2 entity = messages.get(position);

    if (entity.isIncomingMsg()) {
      return IMsgViewType.IMVT_INCOMING_MSG;
    } else {
      return IMsgViewType.IMVT_OUTGOING_MSG;
    }

  }

  public int getViewTypeCount() {
    return 2;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    final ChatMessage2 entity = messages.get(position);
    boolean isIncomingMsg = entity.isIncomingMsg();
    ViewHolder viewHolder;
    if (convertView == null) {
      if (isIncomingMsg) {
        convertView = inflater.inflate(R.layout.chat_message_left, null);
      } else {
        convertView = inflater.inflate(R.layout.chat_message_right, null);
      }

      viewHolder = new ViewHolder();
      viewHolder.sendTime = (TextView) convertView.findViewById(R.id.msgSendTime);
      viewHolder.sender = (TextView) convertView.findViewById(R.id.msgSender);
      viewHolder.content = (TextView) convertView.findViewById(R.id.msgContent);
      viewHolder.duration = (TextView) convertView.findViewById(R.id.msgDuration);
      viewHolder.isIncoming = isIncomingMsg;

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
    viewHolder.sender.setText(entity.getName());

    return convertView;
  }

  static class ViewHolder {
    public TextView sendTime;
    public TextView sender;
    public TextView content;
    public TextView duration;
    public boolean isIncoming;
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
