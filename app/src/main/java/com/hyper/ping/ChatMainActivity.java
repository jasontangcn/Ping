package com.hyper.ping;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatMainActivity extends Activity implements OnClickListener {
  private static final String LOG_TAG = ChatMainActivity.class.getSimpleName();
  private Button backBTN;

  private ListView conversationLV;

  private RelativeLayout sendMessageLayout;
  private ImageView chatModeIV;
  private EditText messageET;
  private Button sendBTN;
  private TextView speakTV;

  private ChatMessageAdapter messageAdapter;
  private List<ChatMessage> messages = new ArrayList<ChatMessage>();

  private boolean isTooShort = false;
  private View chatVoiceLayout;
  private LinearLayout recordProgressLayout, voiceRecordingLayout, voiceTooShortLayout;
  private ImageView cancelVoiceHintIV, cancelVoiceIV;
  private SoundMeter soundMeter;
  private LinearLayout voiceCancelLayout;
  private ImageView volumeIV;
  private boolean voiceMode = false;
  private boolean  voiceStarted;
  private Handler handler = new Handler();
  private String recordName;
  private long startRecordTime, endRecordTime;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_main_activity);
    // do not trigger soft keyboard
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    buildView();
    prepareData();
  }

  public void buildView() {
    backBTN = (Button) findViewById(R.id.chatBackBTN);
    backBTN.setOnClickListener(this);

    conversationLV = (ListView) findViewById(R.id.chatConversationLV);

    sendMessageLayout = (RelativeLayout) findViewById(R.id.chatSendMessageLayout);
    chatModeIV = (ImageView) this.findViewById(R.id.chatModeIV);
    messageET = (EditText) findViewById(R.id.chatMessageET);
    sendBTN = (Button) findViewById(R.id.chatSendBTN);
    sendBTN.setOnClickListener(this);
    speakTV = (TextView) findViewById(R.id.chatSpeekTV);

    chatModeIV.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        if (voiceMode) {
          speakTV.setVisibility(View.GONE);
          sendMessageLayout.setVisibility(View.VISIBLE);
          chatModeIV.setImageResource(R.drawable.chat_setmode_msg_btn_selector);
          voiceMode = false;
        } else {
          speakTV.setVisibility(View.VISIBLE);
          sendMessageLayout.setVisibility(View.GONE);
          chatModeIV.setImageResource(R.drawable.chat_setmode_voice_btn_selector);
          voiceMode = true;
        }
      }
    });

    speakTV.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        return false;
      }
    });

    chatVoiceLayout = this.findViewById(R.id.chatVoiceLayout);
    voiceRecordingLayout = (LinearLayout) this.findViewById(R.id.voiceRecordingLayout);
    volumeIV = (ImageView) this.findViewById(R.id.voiceVolumeIV);
    recordProgressLayout = (LinearLayout) this.findViewById(R.id.voiceProgressLayout);
    cancelVoiceHintIV = (ImageView) this.findViewById(R.id.voiceCancelHintIV);
    voiceCancelLayout = (LinearLayout) this.findViewById(R.id.voiceCancelLayout);
    cancelVoiceIV = (ImageView) this.findViewById(R.id.voiceCancelIV);
    voiceTooShortLayout = (LinearLayout) this.findViewById(R.id.voiceTooShortLayout);
    soundMeter = new SoundMeter();

  }

  private String[] msgArray = new String[]{"Good morning, Sam.", "Good morning, Lucy.", "Did you meet Terry yesterday?", "No, actually.", "What happened?", "He was sick."};
  private String[] dataArray = new String[]{"2012-10-31 18:00", "2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20", "2012-10-31 18:30", "2012-10-31 18:35"};
  private final static int COUNT = 6;

  public void prepareData() {
    for (int i = 0; i < COUNT; i++) {
      ChatMessage entity = new ChatMessage();
      entity.setSendTime(dataArray[i]);
      if (i % 2 == 0) {
        entity.setName("Lucy");
        entity.isIncomingMsg(true);
      } else {
        entity.setName("Sam");
        entity.isIncomingMsg(false);
      }

      entity.setText(msgArray[i]);
      messages.add(entity);
    }

    messageAdapter = new ChatMessageAdapter(this, messages);
    conversationLV.setAdapter(messageAdapter);
  }

  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.chatSendBTN:
        send();
        break;
      case R.id.chatBackBTN:
        finish();
        break;
    }
  }

  private void send() {
    String contString = messageET.getText().toString();
    if (contString.length() > 0) {
      ChatMessage entity = new ChatMessage();
      entity.setSendTime(getDate());
      entity.setName("Sam");
      entity.isIncomingMsg(false);
      entity.setText(contString);
      messages.add(entity);
      messageAdapter.notifyDataSetChanged();
      messageET.setText("");
      conversationLV.setSelection(conversationLV.getCount() - 1);
    }
  }

  private String getDate() {
    Calendar c = Calendar.getInstance();

    String year = String.valueOf(c.get(Calendar.YEAR));
    String month = String.valueOf(c.get(Calendar.MONTH));
    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
    String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
    String mins = String.valueOf(c.get(Calendar.MINUTE));

    StringBuffer sbBuffer = new StringBuffer();
    sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);

    return sbBuffer.toString();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!Environment.getExternalStorageDirectory().exists()) {
      Toast.makeText(this, "No SDCard.", Toast.LENGTH_LONG).show();
      return false;
    }

    if (voiceMode) {
      int[] speakLoc = new int[2];
      speakTV.getLocationInWindow(speakLoc);
      int speakY = speakLoc[1];
      int speakX = speakLoc[0];
      int[] cancelLoc = new int[2];
      voiceCancelLayout.getLocationInWindow(cancelLoc);
      int cancelY = cancelLoc[1];
      int cancleX = cancelLoc[0];

      if (event.getAction() == MotionEvent.ACTION_DOWN && !voiceStarted) {
        Log.e(LOG_TAG,"===== 1 =====");
        if (event.getY() > speakY && event.getX() > speakX) {
          Log.e(LOG_TAG,"===== 2 =====");
          speakTV.setBackgroundResource(R.drawable.chat_voice_btn_pressed);

          chatVoiceLayout.setVisibility(View.VISIBLE);
          voiceRecordingLayout.setVisibility(View.GONE);
          recordProgressLayout.setVisibility(View.VISIBLE);
          voiceCancelLayout.setVisibility(View.GONE);
          voiceTooShortLayout.setVisibility(View.GONE);

          handler.postDelayed(new Runnable() {
            public void run() {
              if (!isTooShort) {
                recordProgressLayout.setVisibility(View.GONE);
                cancelVoiceHintIV.setVisibility(View.VISIBLE);
                voiceRecordingLayout.setVisibility(View.VISIBLE);
              }
            }
          }, 300);

          //startRecordTime = SystemClock.currentThreadTimeMillis();
          startRecordTime = System.currentTimeMillis();
          recordName = startRecordTime + ".amr";
          start(recordName);
          voiceStarted = true;
        }
      } else if (event.getAction() == MotionEvent.ACTION_UP && voiceStarted) {
        Log.e(LOG_TAG,"===== 3 =====");
        speakTV.setBackgroundResource(R.drawable.chat_voice_btn);

        if (event.getY() >= cancelY && event.getY() <= cancelY + voiceCancelLayout.getHeight() &&
            event.getX() >= cancleX && event.getX() <= cancleX + voiceCancelLayout.getWidth()) {
          Log.e(LOG_TAG,"===== 4 =====");
          chatVoiceLayout.setVisibility(View.GONE);
          cancelVoiceHintIV.setVisibility(View.VISIBLE);
          voiceCancelLayout.setVisibility(View.GONE);
          stop();
          voiceStarted = false;
          File file = new File(Environment.getExternalStorageDirectory() + "/" + recordName);
          if (file.exists()) {
            file.delete();
          }
        } else {
          Log.e(LOG_TAG,"===== 5 =====");
          voiceRecordingLayout.setVisibility(View.GONE);
          stop();
          //endRecordTime = SystemClock.currentThreadTimeMillis();
          endRecordTime = System.currentTimeMillis();
          voiceStarted = false;
          int time = (int) ((endRecordTime - startRecordTime) / 1000);
          if (time < 1) {
            isTooShort = true;
            recordProgressLayout.setVisibility(View.GONE);
            voiceRecordingLayout.setVisibility(View.GONE);
            voiceTooShortLayout.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
              public void run() {
                voiceTooShortLayout.setVisibility(View.GONE);
                chatVoiceLayout.setVisibility(View.GONE);
                isTooShort = false;
              }
            }, 500);
            return false;
          }
          ChatMessage entity = new ChatMessage();
          entity.setSendTime(getDate());
          entity.setName("Sam");
          entity.isIncomingMsg(false);
          entity.setDuration(time + "\"");
          entity.setText(recordName);
          messages.add(entity);
          messageAdapter.notifyDataSetChanged();
          conversationLV.setSelection(conversationLV.getCount() - 1);
          chatVoiceLayout.setVisibility(View.GONE);

        }
      }
      if (event.getY() < speakY) {
        Log.e(LOG_TAG,"===== 6 =====");
        Animation smallAnim = AnimationUtils.loadAnimation(this, R.anim.chat_cancel_record_small);
        Animation bigAnim = AnimationUtils.loadAnimation(this, R.anim.chat_cancel_record_big);
        cancelVoiceHintIV.setVisibility(View.GONE);
        voiceCancelLayout.setVisibility(View.VISIBLE);
        voiceCancelLayout.setBackgroundResource(R.drawable.chat_voice_cancel_bg);
        if (event.getY() >= cancelY && event.getY() <= cancelY + voiceCancelLayout.getHeight() &&
            event.getX() >= cancleX && event.getX() <= cancleX + voiceCancelLayout.getWidth()) {
          Log.e(LOG_TAG,"===== 7 =====");
          voiceCancelLayout.setBackgroundResource(R.drawable.chat_voice_cancel_bg_focused);
          cancelVoiceIV.startAnimation(smallAnim);
          cancelVoiceIV.startAnimation(bigAnim);
        }
      } else {
        Log.e(LOG_TAG,"===== 8 =====");
        cancelVoiceHintIV.setVisibility(View.VISIBLE);
        voiceCancelLayout.setVisibility(View.GONE);
        voiceCancelLayout.setBackgroundResource(0);
      }
    }
    return super.onTouchEvent(event);
  }

  private static final int POLL_INTERVAL = 300;

  private Runnable sleepTask = new Runnable() {
    public void run() {
      stop();
    }
  };

  private Runnable pollTask = new Runnable() {
    public void run() {
      double amp = soundMeter.getAmplitude();
      updateDisplay(amp);
      handler.postDelayed(pollTask, POLL_INTERVAL);
    }
  };

  private void start(String recordName) {
    soundMeter.start(recordName);
    handler.postDelayed(pollTask, POLL_INTERVAL);
  }

  private void stop() {
    handler.removeCallbacks(sleepTask);
    handler.removeCallbacks(pollTask);
    soundMeter.stop();
    volumeIV.setImageResource(R.drawable.chat_amp1);
  }

  private void updateDisplay(double signalEMA) {
    switch ((int) signalEMA) {
      case 0:
      case 1:
        volumeIV.setImageResource(R.drawable.chat_amp1);
        break;
      case 2:
      case 3:
        volumeIV.setImageResource(R.drawable.chat_amp2);

        break;
      case 4:
      case 5:
        volumeIV.setImageResource(R.drawable.chat_amp3);
        break;
      case 6:
      case 7:
        volumeIV.setImageResource(R.drawable.chat_amp4);
        break;
      case 8:
      case 9:
        volumeIV.setImageResource(R.drawable.chat_amp5);
        break;
      case 10:
      case 11:
        volumeIV.setImageResource(R.drawable.chat_amp6);
        break;
      default:
        volumeIV.setImageResource(R.drawable.chat_amp7);
        break;
    }
  }
}