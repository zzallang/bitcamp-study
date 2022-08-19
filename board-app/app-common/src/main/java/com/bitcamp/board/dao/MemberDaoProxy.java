package com.bitcamp.board.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.bitcamp.board.domain.Member;
import com.google.gson.Gson;

public class MemberDaoProxy {

  String dataName;
  DataInputStream in;
  DataOutputStream out;

  public MemberDaoProxy(String dataName, DataInputStream in, DataOutputStream out) {
    this.dataName = dataName;
    this.in = in;
    this.out = out;
  }

  public boolean insert(Member member) throws Exception {
    out.writeUTF(dataName);
    out.writeUTF("insert");
    out.writeUTF(new Gson().toJson(member));
    return in.readUTF().equals("success");
  }

  public boolean update(Member member) throws Exception {
    out.writeUTF(dataName);
    out.writeUTF("update");
    out.writeUTF(new Gson().toJson(member));
    return in.readUTF().equals("success");
  }

  public Member findByEmail(String email) throws Exception {
    out.writeUTF(dataName);
    out.writeUTF("findByEmail");

    if (in.readUTF().equals("fail")) {
      return null;
    }
    return new Gson().fromJson(in.readUTF(), Member.class);
  }

  public boolean delete(String email) throws Exception {
    out.writeUTF(dataName);
    out.writeUTF("delete");
    return in.readUTF().equals("success");
  }

  public Member[] findAll() throws Exception {
    out.writeUTF(dataName);
    out.writeUTF("findAll");

    if (in.readUTF().equals("fail")) {
      return null;
    }
    return new Gson().fromJson(in.readUTF(), Member[].class);
  }
}
