package com.ixuea.courses.android_first.activity.components.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ixuea.courses.android_first.R;
import com.ixuea.courses.android_first.dialog.TextDialogFragment;

public class DialogPageActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dialog_page);

//        textDialog
    Button textDialog = findViewById(R.id.textDialog);
    textDialog.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        TextDialogFragment dialog = new TextDialogFragment();
        dialog.setTitle("发现新版本:7.47.6.0");
        dialog.setText("1.注册登录页交互、视觉优化 2.修复部分已知问题 3.测试文字-测试文字");
        dialog.setPositiveListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // 点击确认按钮时执行的操作
          }
        });
        dialog.setNegativeListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // 点击取消按钮时执行的操作
          }
        });
        dialog.show(getSupportFragmentManager(), "textDialog");
      }
    });
  }
}
