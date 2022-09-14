package com.bitcamp.util;

import java.util.Stack;

public class BreadCrumb {

  public Stack<String> menuStack = new Stack<>();

  // Thread마다 BreadCrumb 객체를 따로 관리해주는 관리자를 준비한다.
  static ThreadLocal<BreadCrumb> localManager = new ThreadLocal<>(); // 로컬 매니저 객체는 한 개임

  public static BreadCrumb getBreadCrumbOfCrrentThread() {
    // 스레드 로컬 관리자를 통해 현재 스레드 보관소에 저장되어 있는
    // Breadcrumb 객체를 달라고 요청한다.
    return localManager.get();
  }

  public BreadCrumb() {
    // 스레드 로컬 관리자에게 현재 스레드 전용 보관소에
    // Breadcrumb 객체를 보관해 달라고 요청한다.
    localManager.set(this); // 메소드 요청할 ㄸㅐ 마다 현재 스레드 이름으로 저장
  }


  public void put (String menu) {
    menuStack.push(menu);
  }

  public void pickUp () {
    menuStack.pop();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (String title : menuStack) {
      if (!builder.isEmpty()) {
        builder.append(" > ");
      }
      builder.append(title);
    }
    return builder.toString();
  }
}

