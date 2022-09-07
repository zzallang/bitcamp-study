package com.bitcamp.board.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.bitcamp.board.domain.Member;
import com.google.gson.Gson;

public class MariaMemberDao {

  public int insert(Member member) throws Exception {
    try (Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb","study","1111");
        PreparedStatement pstmt = con.prepareStatement(
            "insert into app_member(name,email,pwd) values(?,?,sha2(?,256))")) {

      pstmt.setString(1, member.name);
      pstmt.setString(2, member.email);
      pstmt.setString(3, member.password);

      return pstmt.executeUpdate();

    }
  }

  public Member findByNo(int no) throws Exception {
    try (Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb","study","1111");
        PreparedStatement pstmt = con.prepareStatement(
            "select mno,name,email,cdt from app_member where mno=?")) {

      pstmt.setInt(1, no);

      ResultSet rs = pstmt.executeQuery();
      if (!rs.next()) {
        return null;
      }

      Member member = new Member();
      member.no = rs.getInt("mno");
      member.name = rs.getString("neme");
      member.email = rs.getString("eamil");
      member.createdDate = 
    }
  }


  public boolean update(Member member) throws Exception {
    try (Socket socket = new Socket(ip,port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());) {

      out.writeUTF(dataName);
      out.writeUTF("update");
      out.writeUTF(new Gson().toJson(member));
      return in.readUTF().equals("success");
    }
  }


  public boolean delete(String email) throws Exception {
    try (Socket socket = new Socket(ip,port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());) {

      out.writeUTF(dataName);
      out.writeUTF("delete");
      return in.readUTF().equals("success");
    }
  }

  public Member[] findAll() throws Exception {
    try (Socket socket = new Socket(ip,port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());) {

      out.writeUTF(dataName);
      out.writeUTF("findAll");

      if (in.readUTF().equals("fail")) {
        return null;
      }
      return new Gson().fromJson(in.readUTF(), Member[].class);
    }
  }
}
