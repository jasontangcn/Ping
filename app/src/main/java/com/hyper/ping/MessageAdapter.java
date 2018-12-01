package com.hyper.ping;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyper.ping.utils.CommonsUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
  List<AudioRecord> audioRecords;
  Context context;
  List<AnimationDrawable> animations;
  int pos = -1;//标记当前录音索引，默认没有播放任何一个

  public MessageAdapter(Context context, List<AudioRecord> records) {
    this.context = context;
    this.audioRecords = records;
    this.animations = new ArrayList<>();
  }


  @Override
  public int getCount() {
    return audioRecords.size();
  }

  @Override
  public Object getItem(int position) {
    return audioRecords.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.chat_message, null);
      viewHolder = new ViewHolder();
      viewHolder.avator = (ImageView) convertView.findViewById(R.id.messageAvatorIV);
      viewHolder.voiceLine = (ImageView) convertView.findViewById(R.id.messageVoiceLineIV);
      viewHolder.singer = (LinearLayout) convertView.findViewById(R.id.messageVoiceSingerLayout);
      viewHolder.duration = (TextView) convertView.findViewById(R.id.messageVoiceDurationTV);
      viewHolder.read = (ImageView) convertView.findViewById(R.id.mesageVoiceReadIV);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }


    final AudioRecord record = audioRecords.get(position);
    //设置显示时长
    viewHolder.duration.setText(record.getDuration() <= 0 ? 1 + "''" : record.getDuration() + "''");
    if (!record.isPlayed()) {
      viewHolder.read.setVisibility(View.VISIBLE);
    } else {
      viewHolder.read.setVisibility(View.GONE);
    }
    //更改并显示录音条长度
    RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) viewHolder.voiceLine.getLayoutParams();
    ps.width = CommonsUtils.getVoiceLineWight(context, record.getDuration());
    viewHolder.voiceLine.setLayoutParams(ps); //更改语音长条长度

    //开始设置监听
    final LinearLayout ieaLlSinger = viewHolder.singer;
    viewHolder.voiceLine.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //只要点击就设置为已播放状态（隐藏小红点）
        record.setPlayed(true);
        notifyDataSetChanged();
        //这里更新数据库小红点。这里不知道为什么可以强转建议复习复习基础~
        ((ChatActivity) context).getDatabase().updateAudioRecord(record);

        final AnimationDrawable anim = (AnimationDrawable) ieaLlSinger.getBackground();
        //重置动画
        resetAnimation(anim);
        anim.start();

        //处理点击正在播放的语音时，可以停止；再次点击时重新播放。
        if (pos == position) {
          if (record.isPlaying()) {
            record.setPlaying(false);
            MediaManager.release();
            anim.stop();
            anim.selectDrawable(0);//reset
            return;
          } else {
            record.setPlaying(true);
          }
        }
        //记录当前位置正在播放。
        pos = position;
        record.setPlaying(true);

        //播放前重置。
        MediaManager.release();
        //开始实质播放
        MediaManager.playSound(record.getPath(), new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mp) {
            anim.selectDrawable(0);//显示动画第一帧
            anim.stop();

            // 播放完毕，当前播放索引置为-1。
            pos = -1;
          }
        });
      }
    });
    return convertView;
  }

  private void resetData() {
    for (AudioRecord record : audioRecords) {
      record.setPlaying(false);//保证在第二次点击该语音栏时当作没有“不是在播放状态”。
    }
  }

  private void resetAnimation(AnimationDrawable animation) {
    if (!animations.contains(animation)) {
      animations.add(animation);
    }
    for (AnimationDrawable anim : animations) {
      anim.selectDrawable(0);
      anim.stop();
    }
  }

  class ViewHolder {
    ImageView avator;
    ImageView voiceLine;
    LinearLayout singer;
    TextView duration;
    ImageView read;
  }
}
