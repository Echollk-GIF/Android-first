package com.ixuea.courses.android_first.popup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ixuea.courses.android_first.R;
import com.ixuea.courses.android_first.activity.BaseActivity;

public class PopupActivity extends BaseActivity {

  private Button diy_popup_fragment;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
    }

  @Override
  protected void initViews() {
    super.initViews();
    diy_popup_fragment = findViewById(R.id.diy_popup_fragment);
  }

  @Override
  protected void initListeners() {
    super.initListeners();
    diy_popup_fragment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showCustomBottomSheet();
      }
    });
  }

  private void showCustomBottomSheet() {
    // 创建BottomSheetDialog对象
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

    // 加载自定义布局文件
    View customView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

    // 设置自定义布局文件为BottomSheet的内容
    bottomSheetDialog.setContentView(customView);

    // 显示BottomSheet
    bottomSheetDialog.show();
  }
}
