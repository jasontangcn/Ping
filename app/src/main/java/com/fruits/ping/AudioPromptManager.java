package com.fruits.ping;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class AudioPromptManager {
  private Dialog dialog;
  private RelativeLayout promptMoveUpLayout;
  private TextView promptMoveUpCancelTV;
  private RelativeLayout promptReleaseLayout;
  private TextView promptReleaseCancelTV;
  private Context context;

  public AudioPromptManager(Context context) {
    this.context = context;
  }

  public void showRecordingDialog() {
    dialog = new Dialog(context, R.style.audioPrompt);
    // 用LayoutInflater来引用布局
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.chat_prompt, null);
    dialog.setContentView(view);
    promptMoveUpLayout = (RelativeLayout) view.findViewById(R.id.promptMoveUpLayout);
    promptMoveUpCancelTV = (TextView) view.findViewById(R.id.promptMoveUpCancelTV);
    promptReleaseLayout = (RelativeLayout) view.findViewById(R.id.chatPromptReleaseLayout);
    promptReleaseCancelTV = (TextView) view.findViewById(R.id.promptReleaseCancelTV);
    //dialog.hide();
    dialog.show();
  }

  /**
   * 设置正在录音时的界面
   */
  public void recording() {
    if (dialog != null && dialog.isShowing()) {
      promptReleaseLayout.setVisibility(View.GONE);
      promptReleaseCancelTV.setVisibility(View.GONE);

      promptMoveUpLayout.setVisibility(View.VISIBLE);
      promptMoveUpCancelTV.setVisibility(View.VISIBLE);
      promptMoveUpLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.voice_volume_1));
      promptMoveUpCancelTV.setText(R.string.up_for_cancel);
    }
  }

  /**
   * 取消界面
   */
  public void wantToCancel() {
    if (dialog != null && dialog.isShowing()) {
      promptMoveUpLayout.setVisibility(View.GONE);
      promptMoveUpCancelTV.setVisibility(View.GONE);
      promptReleaseLayout.setVisibility(View.VISIBLE);
      promptReleaseCancelTV.setVisibility(View.VISIBLE);
      promptReleaseLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.voice_cancel));
      promptReleaseCancelTV.setText(R.string.want_to_cancle);
      //promptReleaseCancelTV.setBackgroundColor(context.getResources().getColor(R.color.colorRedBg));
    }
  }

  // 时间过短
  public void tooShort() {
    if (dialog != null && dialog.isShowing()) {
      promptReleaseLayout.setVisibility(View.VISIBLE);
      promptReleaseCancelTV.setVisibility(View.VISIBLE);
      promptMoveUpLayout.setVisibility(View.GONE);
      promptMoveUpCancelTV.setVisibility(View.GONE);
      promptReleaseLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.voice_sigh));
      promptReleaseCancelTV.setText(R.string.time_too_short);
    }

  }

  // 隐藏dialog
  public void dismiss() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }

  public void updateVoiceLevel(int level) {
    if (level > 0 && level < 6) {
    } else {
      level = 5;
    }
    if (dialog != null && dialog.isShowing()) {
      //通过level来找到图片的id，也可以用switch来寻址，但是代码可能会比较长
      int imgId = context.getResources().getIdentifier("yuyin_voice_" + level, "drawable", context.getPackageName());
      promptMoveUpLayout.setBackgroundResource(imgId);
    }

  }

  public TextView getPromptMoveUpCancelTV() {
    return promptMoveUpCancelTV;
  }
}
