package com.ixuea.courses.android_first.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ixuea.courses.android_first.R;
import com.ixuea.courses.android_first.util.ScreenUtil;

public class TextDialogFragment extends DialogFragment {
  private String title;
  private String text;
  private View.OnClickListener positiveListener;
  private View.OnClickListener negativeListener;

  public void setTitle(String title) {
    this.title = title;
  }
  public void setText(String text) {
    this.text = text;
  }

  public void setPositiveListener(View.OnClickListener positiveListener) {
    this.positiveListener = positiveListener;
  }

  public void setNegativeListener(View.OnClickListener negativeListener) {
    this.negativeListener = negativeListener;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //点击弹窗外边不能关闭
    setCancelable(false);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_text_dialog, container, false);

    TextView textView = view.findViewById(R.id.text);
    textView.setText(text);

    TextView titleView = view.findViewById(R.id.title);
    titleView.setText(title);

    Button positiveButton = view.findViewById(R.id.positive_button);
    positiveButton.setOnClickListener(positiveListener);

    Button negativeButton = view.findViewById(R.id.negative_button);
    negativeButton.setOnClickListener(negativeListener);

    return view;
  }

  public void show(FragmentManager manager, String tag) {
    super.show(manager, tag);
  }

  @Override
  public void onResume() {
    super.onResume();
    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

    params.width = (int) (ScreenUtil.getScreenWith(getContext()) * 0.9);
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
  }
}
