package com.hyper.ping;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.hyper.ping.utils.DBManager;
import com.hyper.ping.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
  public static final String TAG = ChatActivity.class.getSimpleName();

  private ListView messagesLV;
  private RecordAudioButton recordAudioBTN;
  List<AudioRecord> audioRecords;
  MessageAdapter messageAdapter;
  PermissionHelper permHelper;
  private DBManager database;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_activity);
    initView();
    initData();
    initAdapter();
    initListener();
  }

  private void initView() {
    messagesLV = (ListView) findViewById(R.id.chatMessagesLV);
    recordAudioBTN = (RecordAudioButton) findViewById(R.id.chatRecordVoiceBTN);
    // 设置不可见或者不想被点击
    // recordAudioBTN.setVisibility(View.GONE);//隐藏
    // recordAudioBTN.setCanRecord(false);//重写该方法，设置为不可点击
  }

  private void initData() {
    audioRecords = new ArrayList<>();
    //初始化DBManager
    database = new DBManager(this);
  }

  private void initAdapter() {
    messageAdapter = new MessageAdapter(this, audioRecords);
    messagesLV.setAdapter(messageAdapter);

    //开始获取数据库数据
    List<AudioRecord> records = database.retrieveAudioRecords();
    if (records == null || records.isEmpty()) return;
    for (AudioRecord record : records) {
      Log.e(ChatActivity.TAG, "AudioRecord: " + record.toString());
    }
    audioRecords.addAll(records);
    messageAdapter.notifyDataSetChanged();
    messagesLV.setSelection(audioRecords.size() - 1);
  }

  private void initListener() {
    recordAudioBTN.setHasRecordPermission(false);
    // 授权处理
    permHelper = new PermissionHelper(this);

    permHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音", new PermissionHelper.PermissionListener() {
      @Override
      public void doAfterGrand(String... permission) {
        recordAudioBTN.setHasRecordPermission(true);
        recordAudioBTN.setRecordAudioListener((seconds, filePath) -> {
          AudioRecord record = new AudioRecord();
          record.setDuration((int) seconds <= 0 ? 1 : (int) seconds);
          record.setPath(filePath);
          record.setPlayed(false);
          audioRecords.add(record);
          messageAdapter.notifyDataSetChanged();
          messagesLV.setSelection(audioRecords.size() - 1);

          //添加到数据库
          database.addAudioRecord(record);
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

  @Override
  protected void onPause() {
    MediaManager.release();//保证在退出该页面时，终止语音播放
    super.onPause();
  }

  public DBManager getDatabase() {
    return database;
  }

  public void setDatabase(DBManager database) {
    this.database = database;
  }
}
