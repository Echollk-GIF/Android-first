package com.ixuea.courses.android_first.components.sendGoods.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ixuea.courses.android_first.R;
import com.ixuea.courses.android_first.components.sendGoods.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
  private List<Item> itemList;

  public ItemAdapter(List<Item> itemList) {
    this.itemList = itemList;
  }

  @NonNull
  @Override
  public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_list, parent, false);
    return new ItemViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
    Item item = itemList.get(position);
    holder.idText.setText(item.getId());
    holder.nameText.setText(item.getName());

    // 设置点击事件监听器
    holder.setItemClickListener(item);
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }

  public static class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView idText, nameText;

    public ItemViewHolder(View view) {
      super(view);
      idText = view.findViewById(R.id.id_text);
      nameText = view.findViewById(R.id.name_text);
    }

    // 在ViewHolder中添加一个方法，用于设置点击事件监听器
    public void setItemClickListener(final Item data) {
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // 获取当前Item的ID，并通过Toast显示
          Toast.makeText(itemView.getContext(), "ID: " + data.getId(), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
