package com.hyper.ping;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyper.ping.utils.DBManager;
import com.hyper.ping.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements OnClickListener {
  private static final String LOG_TAG = ChatActivity.class.getSimpleName();
  private Button backBTN;
  private ListView messagesLV;
  private RelativeLayout textMessageLayout;
  private ImageView chatModeIV;
  private boolean voiceMode = false;
  private EditText textMessageET;
  private Button sendTextMessageBTN;
  //private TextView speakTV;

  private MessageAdapter messageAdapter;
  private List<ChatMessage> messages = new ArrayList<ChatMessage>();

  private RecordAudioButton recordAudioBTN;

  PermissionHelper permHelper;
  private DBManager database;

  /*
  private View chatVoiceLayout;
  private LinearLayout voiceRecordingLayout, recordProgressLayout, voiceCancelLayout, voiceTooShortLayout;
  private ImageView volumeIV;
  private ImageView cancelVoiceHintIV, cancelVoiceIV;
  private SoundMeter soundMeter;

  private boolean  voiceStarted;
  private Handler handler = new Handler();
  private String recordName;
  private long startRecordTime, endRecordTime;
  private boolean isTooShort = false;
  */

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_activity);
    // do not trigger soft keyboard
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    initView();
    initData();
    initAdapter();
    initListener();
  }

  public void initView() {
    backBTN = (Button) findViewById(R.id.chatBackBTN);
    backBTN.setOnClickListener(this);

    messagesLV = (ListView) findViewById(R.id.chatMessagesLV);

    textMessageLayout = (RelativeLayout) findViewById(R.id.chatTextMessageLayout);
    chatModeIV = (ImageView) this.findViewById(R.id.chatModeIV);
    textMessageET = (EditText) findViewById(R.id.chatTextMessageET);
    sendTextMessageBTN = (Button) findViewById(R.id.chatSendTextMessageBTN);
    sendTextMessageBTN.setOnClickListener(this);
    //speakTV = (TextView) findViewById(R.id.chatSpeekTV);
    recordAudioBTN = findViewById(R.id.chatRecordAudioBTN);

    chatModeIV.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (voiceMode) {
          //speakTV.setVisibility(View.GONE);
          recordAudioBTN.setVisibility(View.GONE);
          textMessageLayout.setVisibility(View.VISIBLE);
          chatModeIV.setImageResource(R.drawable.chat_setmode_msg_btn_selector);
          voiceMode = false;
        } else {
          //speakTV.setVisibility(View.VISIBLE);
          recordAudioBTN.setVisibility(View.VISIBLE);
          textMessageLayout.setVisibility(View.GONE);
          chatModeIV.setImageResource(R.drawable.chat_setmode_voice_btn_selector);
          voiceMode = true;
        }
      }
    });

    /*
    speakTV.setOnTouchListener(new OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        return false;
      }
    });

    chatVoiceLayout = this.findViewById(R.id.chatVoiceLayout);
    recordProgressLayout = (LinearLayout) this.findViewById(R.id.voiceProgressLayout);
    voiceRecordingLayout = (LinearLayout) this.findViewById(R.id.voiceRecordingLayout);
    volumeIV = (ImageView) this.findViewById(R.id.voiceVolumeIV);
    cancelVoiceHintIV = (ImageView) this.findViewById(R.id.voiceCancelHintIV);
    voiceCancelLayout = (LinearLayout) this.findViewById(R.id.voiceCancelLayout);
    cancelVoiceIV = (ImageView) this.findViewById(R.id.voiceCancelIV);
    voiceTooShortLayout = (LinearLayout) this.findViewById(R.id.voiceTooShortLayout);
    soundMeter = new SoundMeter();
    */
  }

  private String[] msgArray = new String[]{"Good morning, Sam.", "Good morning, Lucy.", "Did you meet Terry yesterday?", "No, actually.", "What happened?", "He was sick."};
  private String[] dateArray = new String[]{"2012-10-31 18:00", "2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20", "2012-10-31 18:30", "2012-10-31 18:35"};

  public void initData() {
    for (int i = 0; i < msgArray.length; i++) {
      ChatMessage msg = new ChatMessage();
      msg.setSendTime(dateArray[i]);
      if (i % 2 == 0) {
        msg.setSender("Lucy");
        msg.setIncoming(true);
      } else {
        msg.setSender("Sam");
        msg.setIncoming(false);
      }

      msg.setContent(msgArray[i]);
      messages.add(msg);
    }

    //messageAdapter = new MessageAdapter2(this, messages);
    //messagesLV.setAdapter(messageAdapter);
  }

  private void initAdapter() {
    messageAdapter = new MessageAdapter(this, messages);
    messagesLV.setAdapter(messageAdapter);
    /*
    //开始获取数据库数据
    List<AudioRecordBackup> records = database.retrieveAudioRecords();
    if (records == null || records.isEmpty()) return;
    for (AudioRecordBackup record : records) {
      Log.e(ChatActivityBackup.TAG, "AudioRecordBackup: " + record.toString());
    }
    audioRecords.addAll(records);
    messageAdapter.notifyDataSetChanged();
    messagesLV.setSelection(audioRecords.size() - 1);
    */
  }

  private void initListener() {
    recordAudioBTN.setHasRecordPermission(false);
    // 授权处理
    permHelper = new PermissionHelper(this);
    permHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音", new PermissionHelper.PermissionListener() {
      @Override
      public void doAfterGrand(String... permission) {
        recordAudioBTN.setHasRecordPermission(true);
        recordAudioBTN.setRecordAudioListener((duration, filePath) -> {
          ChatMessage msg = new ChatMessage();
          msg.setVoice(true);
          msg.setSender("Sam");
          msg.setSendTime(getDate());
          msg.setDuration(String.valueOf((int) duration <= 0 ? 1 : (int) duration));
          msg.setFilePath(filePath);
          msg.setPlayed(false);
          messages.add(msg);
          messageAdapter.notifyDataSetChanged();
          messagesLV.setSelection(messages.size() - 1);

          //添加到数据库
          //database.addAudioRecord(record);
        });
      }

      @Override
      public void doAfterDenied(String... permission) {
        recordAudioBTN.setHasRecordPermission(false);
        Toast.makeText(ChatActivity.this, "请授权,否则无法录音", Toast.LENGTH_SHORT).show();
      }
    }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);

  }

  //直接把参数交给PermissionHelper就行了
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.chatSendTextMessageBTN:
        sendTextMessage();
        break;
      case R.id.chatBackBTN:
        finish();
        break;
    }
  }

  public DBManager getDatabase() {
    return database;
  }

  public void setDatabase(DBManager database) {
    this.database = database;
  }

  private void sendTextMessage() {
    String msgText = textMessageET.getText().toString();
    if (msgText.length() > 0) {
      ChatMessage msg = new ChatMessage();
      msg.setSender("Sam");
      msg.setSendTime(getDate());
      msg.setContent(msgText);
      messages.add(msg);
      messageAdapter.notifyDataSetChanged();
      textMessageET.setText("");
      messagesLV.setSelection(messagesLV.getCount() - 1);
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

  /*
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
          ChatMessageBackup entity = new ChatMessageBackup();
          entity.setSendTime(getDate());
          entity.setSender("Sam");
          entity.isIncomingMsg(false);
          entity.setDuration(time + "\"");
          entity.setContent(recordName);
          messages.add(entity);
          messageAdapter.notifyDataSetChanged();
          messagesLV.setSelection(messagesLV.getCount() - 1);
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
  */
}