package com.bitcamp.board.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.bitcamp.board.domain.Member;
import com.google.gson.Gson;

// 회원 목록을 관리하는 역할
//
public class MemberDao {

  List<Member> list = new LinkedList<>();

  String filename;


  public MemberDao(String filename) {
    this.filename = filename; 
  }

  public void load() throws Exception {
    try (BufferedReader in = new BufferedReader(new FileReader(filename))) {

      // 파일에서 JSON 문자열을 모두 읽어 StringBuilder에 담는다.
      StringBuilder strBuilder = new StringBuilder();
      String str;
      while ((str = in.readLine()) != null) {
        strBuilder.append(str);
      }

      // StringBuilder에 보관된 JSON 문자열을 가지고 Board[]을 생성한다.
      Member[] arr = new Gson().fromJson(strBuilder.toString(), Member[].class);

      // Board[] 배열의 저장된 객체를 List로 복사한다.
      for (int i = 0; i < arr.length; i++) {
        list.add(arr[i]);
      }
    }
  }

  public void save() throws Exception {
    try (FileWriter out = new FileWriter(filename)) {
      Member[] members = list.toArray(new Member[0]); // 리스트에서 Board 배열을 리턴받고 리스트에 들어있는 크기만큼 보드 객체만 뽑아낸다
      //      String json = new Gson().toJson(boards);
      out.write(new Gson().toJson(members)); // 임시변수 쓸 자리에 그냥 메소드 호출 코드 넣어라
    }
  }

  public void insert(Member member) {
    list.add(member);
  }

  public Member findByEmail(String email) {
    for (int i = 0; i < list.size(); i++) {
      Member member = list.get(i);
      if (member.email.equals(email)) {
        return member;
      }
    }
    return null;
  }

  public boolean delete(String email) {
    for (int i = 0; i < list.size(); i++) {
      Member member = list.get(i);
      if (member.email.equals(email)) {
        return list.remove(i) != null;
      }
    }
    return false;
  }

  public Member[] findAll() {
    Iterator<Member> iterator = list.iterator();

    Member[] arr = new Member[list.size()];

    int i = 0;
    while (iterator.hasNext()) {
      arr[i++] = iterator.next();
    }
    return arr;
  }
}