package com.ixuea.courses.android_first.activity.components.sendGoods;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ixuea.courses.android_first.R;
import com.ixuea.courses.android_first.activity.BaseActivity;
import com.ixuea.courses.android_first.activity.components.sendGoods.adapter.PagerAdapter;

public class SendGoodsActivity extends BaseActivity {

  private TabLayout tabLayout;
  private ViewPager viewPager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sendgoods_page);
  }

  @Override
  protected void initViews() {
    super.initViews();
    // 初始化 TabLayout
    tabLayout = findViewById(R.id.tabLayout);
    // 初始化 ViewPager
    viewPager = findViewById(R.id.viewPager);

  }

  @Override
  protected void initDatum() {
    super.initDatum();
    // 创建一个适配器，用于提供 ViewPager 的内容
    PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager()); // 这里需要根据你的需求自定义 PagerAdapter
    // 设置 ViewPager 的适配器
    viewPager.setAdapter(pagerAdapter);
    // 将 TabLayout 和 ViewPager 关联起来
    tabLayout.setupWithViewPager(viewPager);
  }
}
