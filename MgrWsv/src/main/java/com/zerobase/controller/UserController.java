package com.zerobase.controller;

import com.zerobase.application.service.UserService;
import com.zerobase.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/joinForm")
    public String joinForm(){

            /* 회원가입 페이지로 다시 리턴 */
            return "/user/joinForm";

    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/user/listForm")
    public String dataList(Model model, @PageableDefault(size = 5, sort = "userid", direction = Sort.Direction.DESC)
    Pageable pageable){
        Page<User> user = userService.list(pageable);
        model.addAttribute("users", user);
        return "user/listForm";
    }

}
