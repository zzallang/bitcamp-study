package com.bitcamp.board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import com.bitcamp.board.servlet.BoardServlet;
import com.bitcamp.board.servlet.MemberServlet;
import com.bitcamp.servlet.Servlet;

public class ServerApp {

  public static void main(String[] args) {

    // 클라이언트 요청을 처리할 객체 준비
    Hashtable<String,Servlet> servletMap = new Hashtable<>();
    servletMap.put("board",new BoardServlet("board"));
    servletMap.put("reading",new BoardServlet("reading"));
    servletMap.put("visit",new BoardServlet("visit"));
    servletMap.put("notice",new BoardServlet("notice"));
    servletMap.put("diary",new BoardServlet("diary"));
    servletMap.put("member",new MemberServlet("member"));

    // 로컬클래스
    class RequestThread extends Thread {

      private Socket socket;

      public RequestThread(Socket socket) {
        this.socket = socket;
      }

      // 별도의 실행 흐름에서 수행할 작업 정의
      @Override
      public void run() {

        try (Socket socket = this.socket;
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());) {

          System.out.println("클라이언트와 연결 되었음!");

          // 클라이언트와 서버 사이에 정해진 규칙(protocol)에 따라 데이터를 주고 받는다.
          String dataName = in.readUTF();

          // 로컬클래스는 바깥 메소드의 로 컬 변수를 자신의 멤버인 것 처럼 사용할 수 있다.
          // 어떻게? => 컴파일러가 그것이 가능하도록 필드와 생성자에 파라미터를 자동으로 선언한다.
          // 
          Servlet servlet = servletMap.get(dataName);
          if (servlet != null) {
            servlet.service(in, out);
          } else {
            out.writeUTF("fail");
          }

          System.out.println("클라이언트와 연결을 끊었음!");

        } catch (Exception e) {
          System.out.println("클라이언트 요청 처리 중 오류 발생!");
          e.printStackTrace();
        }
      }
    } // 로컬클래스

    System.out.println();
    System.out.println("[게시글 데이터 관리 서버]");

    try (ServerSocket serverSocket = new ServerSocket(8888);) {

      System.out.println("서버 소켓 준비 완료!");
      System.out.println();

      while (true) {
        // 클라이언트가 연결 되면,
        Socket socket = serverSocket.accept();

        // 클라이언트 요청을 처리할 스레드를 만든다.
        RequestThread t = new RequestThread(socket);

        // main 실행 흐름에서 분리하여 별도의 실행 흐름으로 작업을 수행시킨다.
        t.start();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } // 바깥 쪽 try 

    System.out.println("서버 종료!");
  }
}
