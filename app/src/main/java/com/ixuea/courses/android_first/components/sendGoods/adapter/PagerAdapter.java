package com.ixuea.courses.android_first.components.sendGoods.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ixuea.courses.android_first.components.sendGoods.fragment.Page1;

public class PagerAdapter extends FragmentPagerAdapter {
  private static final int NUM_PAGES = 3; // 页数

  public PagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    // 根据位置返回对应的 Fragment
    switch (position) {
      case 0:
        return new Page1();
      case 1:
        return new Page1();
      case 2:
        return new Page1();
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    // 返回总页数
    return NUM_PAGES;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    // 返回对应位置的标签名称
    switch (position) {
      case 0:
        return "Tab 1";
      case 1:
        return "Tab 2";
      case 2:
        return "Tab 3";
      default:
        return "";
    }
  }
}
