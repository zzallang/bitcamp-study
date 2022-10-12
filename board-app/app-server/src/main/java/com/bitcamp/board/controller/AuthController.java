package com.bitcamp.board.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bitcamp.board.domain.Member;
import com.bitcamp.board.service.MemberService;

@Controller
@RequestMapping("/auth/")
public class AuthController {

  MemberService memberService;
  public AuthController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping("form")
  public String form(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return "/auth/form.jsp";
  }

  @PostMapping("login")
  public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    Member member = memberService.get(email, password);

    if (member != null) {
      HttpSession session = request.getSession();
      session.setAttribute("loginMember", member);
    }

    Cookie cookie = new Cookie("email", email);
    if (request.getParameter("saveEmail") == null) {
      cookie.setMaxAge(0);
    } else {
      cookie.setMaxAge(60 * 60 * 24 * 7);
    }
    response.addCookie(cookie); 

    request.setAttribute("member", member);
    return "/auth/loginResult.jsp";
  }

  @GetMapping("logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpSession session = request.getSession();
    session.invalidate();
    return "redirect:../../"; 
  }
}
