package com.ixuea.courses.android_first.components.sendGoods.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ixuea.courses.android_first.R;
import com.ixuea.courses.android_first.components.sendGoods.Item;
import com.ixuea.courses.android_first.components.sendGoods.adapter.ItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class Page1 extends Fragment {
  private RecyclerView recyclerView;
  private List<Item> itemList;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sendgoods_page1, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    itemList = generateData();
    ItemAdapter adapter = new ItemAdapter(itemList);
    recyclerView.setAdapter(adapter);

    return view;
  }

  private List<Item> generateData() {
    List<Item> itemList = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      Item item = new Item("ID " + i, "Name " + i);
      itemList.add(item);
    }
    return itemList;
  }
}
