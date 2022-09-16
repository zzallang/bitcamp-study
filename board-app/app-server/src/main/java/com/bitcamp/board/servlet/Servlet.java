package com.bitcamp.board.servlet;

import java.io.PrintWriter;

public interface Servlet {
  void service(PrintWriter out) throws Exception;

}
