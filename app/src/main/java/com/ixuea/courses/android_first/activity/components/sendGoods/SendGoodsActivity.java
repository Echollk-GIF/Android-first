package com.ixuea.courses.android_first.activity.components.sendGoods;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    // 自定义标签样式
    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      TabLayout.Tab tab = tabLayout.getTabAt(i);
      if (tab != null) {
        tab.setCustomView(R.layout.custom_tab);
        TextView customTab = tab.getCustomView().findViewById(R.id.tab_title);
        customTab.setText(tab.getText());
      }
    }

    // 设置第一个Tab项为选中状态
    TabLayout.Tab firstTab = tabLayout.getTabAt(0); // 根据实际情况修改索引值
    if (firstTab != null) {
      View view = firstTab.getCustomView();
      TextView customTab = view.findViewById(R.id.tab_title);
      customTab.setTextColor(Color.RED);
      customTab.setTextSize(18);
    }

    // 设置选项卡选择监听
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView customTab = view.findViewById(R.id.tab_title);
        customTab.setTextColor(Color.RED);
        customTab.setTextSize(18);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView customTab = view.findViewById(R.id.tab_title);
        customTab.setTextColor(Color.BLACK);
        customTab.setTextSize(14);
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        // 可选，当再次选中Tab项时触发
      }
    });
  }

}
