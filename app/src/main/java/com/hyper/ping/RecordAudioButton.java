package com.hyper.ping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hyper.ping.utils.FileUtils;

public class RecordAudioButton extends android.support.v7.widget.AppCompatButton implements AudioManager.AudioManagerListener {
  private static final int STATE_NORMAL = 1;
  private static final int STATE_RECORDING = 2;
  private static final int STATE_WANT_TO_CANCEL = 3;
  //取消录音的状态值
  private static final int MSG_VOICE_STOP = 4;

  //垂直方向滑动取消的临界距离
  private static final int Y_DISTANCE_CANCEL = 50;
  private int state = STATE_NORMAL;
  // 正在录音标记
  private boolean isRecording = false;
  //录音对话框
  private PromptManager promptManager;

  private AudioManager audioManager;

  private float duration = 0;
  //最大录音时长（单位:s），default: 60s
  private int maxDuration = 60;
  // 是否触发了onlongclick，准备好了
  private boolean ready;
  //标记是否强制终止
  private boolean timeout = false;
  Context context;

  private Vibrator vibrator;
  //提醒倒计时
  private int countDownTime = 10;
  //设置是否允许录音，是否有录音权限
  private boolean hasRecordPermission = true;

  private RecordAudioListener recordAudioListener;

  private static final int MSG_AUDIO_PREPARED = 0X110;
  private static final int MSG_VOICE_CHANGE = 0X111;
  private static final int MSG_DIALOG_DIMISS = 0X112;

  public RecordAudioButton(Context context) {
    this(context, null);
  }

  public RecordAudioButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    promptManager = new PromptManager(getContext());
    String dir = FileUtils.getRecordDir(this.context).toString();
    audioManager = AudioManager.getInstance(dir);
    audioManager.setAudioManagerListener(this);

    //有同学反应长按响应时间反馈不及时，我们将这一块动作放到DOWN事件中。去onTouchEvent方法查看
    setOnLongClickListener(v -> {
      if (hasRecordPermission()) {
        ready = true;
        audioManager.prepare();
        changeState(STATE_RECORDING);
        return false;
      } else {
        return true;
      }
    });

  }

  public boolean hasRecordPermission() {
    return hasRecordPermission;
  }

  public void setHasRecordPermission(boolean hasRecordPermission) {
    this.hasRecordPermission = hasRecordPermission;
  }

  public interface RecordAudioListener {
    void onCompleted(float duration, String filePath);
  }

  public void setRecordAudioListener(RecordAudioListener recordAudioListener) {
    this.recordAudioListener = recordAudioListener;
  }

  // 获取音量大小
  private Runnable getVoiceLevel = new Runnable() {
    @Override
    public void run() {
      while (isRecording) {
        try {
          //最长maxRecordTimes
          if (duration > maxDuration) {
            stateHandler.sendEmptyMessage(MSG_VOICE_STOP);
            return;
          }
          Thread.sleep(100);
          duration += 0.1f;
          stateHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  };

  @SuppressLint("HandlerLeak")
  private final Handler stateHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_AUDIO_PREPARED:
          // 显示应该是在audio end prepare之后回调
          promptManager.showRecordingDialog();
          isRecording = true;
          new Thread(getVoiceLevel).start();
          // 需要开启一个线程来变换音量
          break;
        case MSG_VOICE_CHANGE:
          // 剩余10s
          showRemainingTime();
          //promptManager.updateVoiceLevel(audioManager.getVoiceLevel(7));
          break;
        case MSG_DIALOG_DIMISS:
          break;
        case MSG_VOICE_STOP:
          timeout = true;//超时
          promptManager.dismiss();
          audioManager.release();// release释放MediaRecorder
          recordAudioListener.onCompleted(duration, audioManager.getFilePath());
          reset();// 恢复标志位
          break;

      }
    }
  };
  //是否触发过震动
  boolean isVibrated;

  private void showRemainingTime() {
    //倒计时
    int remainingTime = (int) (maxDuration - duration);
    if (remainingTime < countDownTime) {
      if (!isVibrated) {
        isVibrated = true;
        vibrate();
      }
      promptManager.getPromptMoveUpCancelTV().setText("还可以说" + remainingTime + "秒  ");
    }
  }

  /*
   * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
   * */
  private void vibrate() {
    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
    vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
  }

  // 发送一个handler的消息
  @Override
  public void prepared() {
    stateHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
  }

  //手指滑动监听
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction();
    int x = (int) event.getX();
    int y = (int) event.getY();

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_MOVE:
        if (isRecording) {
          // 根据x，y来判断用户是否想要取消
          if (wantToCancel(x, y)) {
            changeState(STATE_WANT_TO_CANCEL);
          } else {
            if (!timeout) changeState(STATE_RECORDING);
          }

        }

        break;
      case MotionEvent.ACTION_UP:
        // 首先判断是否有触发onlongclick事件，没有的话直接返回reset
        if (!ready) {
          reset();
          return super.onTouchEvent(event);
        }
        // 如果按的时间太短，还没准备好或者录制时间太短，就离开了，则显示这个dialog
        if (!isRecording || duration < 0.8f) {
          promptManager.tooShort();
          audioManager.cancel();
          stateHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 持续1.3s
        } else if (state == STATE_RECORDING) {//正常录制结束
          if (timeout) return super.onTouchEvent(event);//超时
          promptManager.dismiss();
          audioManager.release();// release释放MediaRecorder

          if (recordAudioListener != null) {// 并且callbackActivity，保存录音
            recordAudioListener.onCompleted(duration, audioManager.getFilePath());
          }
        } else if (state == STATE_WANT_TO_CANCEL) {
          audioManager.cancel();
          promptManager.dismiss();
        }
        reset();// 恢复标志位
        break;

    }

    return super.onTouchEvent(event);
  }

  /**
   * 回复标志位以及状态
   */
  private void reset() {
    isRecording = false;
    changeState(STATE_NORMAL);
    ready = false;
    duration = 0;
    timeout = false;
    isVibrated = false;
  }

  private boolean wantToCancel(int x, int y) {
    if (x < 0 || x > getWidth()) {// 判断是否在左边，右边，上边，下边
      return true;
    }
    if (y < -Y_DISTANCE_CANCEL || y > getHeight() + Y_DISTANCE_CANCEL) {
      return true;
    }
    return false;
  }

  private void changeState(int state) {
    if (this.state != state) {
      this.state = state;
      switch (this.state) {
        case STATE_NORMAL:
          setText(context.getString(R.string.long_click_record));//长按录音
          break;
        case STATE_RECORDING:
          //setBackgroundColor(Color.rgb(0xcd, 0xcd, 0xcd));
          setText(R.string.hang_up_finsh);//松开结束
          setTextColor(Color.WHITE);
          //setBackgroundResource(R.drawable.chat_voice_btn_pressed);
          if (isRecording) {
            // 复写dialog.recording();
            promptManager.recording();
          }
          break;

        case STATE_WANT_TO_CANCEL:
          setText(R.string.release_cancel);//松开取消
          promptManager.wantToCancel();
          break;

      }
    }
  }

  @Override
  public boolean onPreDraw() {
    return false;
  }

  public int getMaxDuration() {
    return maxDuration;
  }

  public void setMaxDuration(int maxDuration) {
    this.maxDuration = maxDuration;
  }

  @Override
  public boolean isInEditMode() {
    return true;
  }
}
