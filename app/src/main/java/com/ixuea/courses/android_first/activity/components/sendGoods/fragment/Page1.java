package com.ixuea.courses.android_first.activity.components.sendGoods.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ixuea.courses.android_first.R;

public class Page1 extends Fragment {

  public Page1() {
    // Required empty public constructor
  }

  // 在这里构建并返回 Fragment 的布局视图
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_sendgoods_page1, container, false);
  }
}
