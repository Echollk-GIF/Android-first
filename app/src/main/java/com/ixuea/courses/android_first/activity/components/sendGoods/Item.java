package com.ixuea.courses.android_first.activity.components.sendGoods;

public class Item {
  private String id;
  private String name;

  public Item(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
