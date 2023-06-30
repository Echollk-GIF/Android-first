package com.ixuea.courses.android_first;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.ixuea.courses.android_first.activity.BaseActivity;
import com.ixuea.courses.android_first.activity.components.dialog.DialogPageActivity;
import com.ixuea.courses.android_first.activity.components.sendGoods.SendGoodsActivity;

public class MainActivity extends BaseActivity {

  private Button dialog_page_button;
  private Button sendGoods_page_button;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void initViews() {
    super.initViews();
    dialog_page_button = findViewById(R.id.dialog_page_button);
    sendGoods_page_button = findViewById(R.id.sendGoods_page_button);
  }

  @Override
  protected void initListeners() {
    super.initListeners();
    dialog_page_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, DialogPageActivity.class);
        startActivity(intent);
      }
    });

    sendGoods_page_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SendGoodsActivity.class);
        startActivity(intent);
      }
    });
  }
}
