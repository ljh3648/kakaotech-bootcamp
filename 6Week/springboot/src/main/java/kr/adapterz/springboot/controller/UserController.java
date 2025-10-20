package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.dto.User;
import kr.adapterz.springboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // 로그인 페이지 (GET)
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 로그인 처리 (POST)
    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String password,
                        Model model) {
        if (userService.validateUser(id, password)) {
            model.addAttribute("users", userService.getAllUsers());
            return "users"; // 성공 시 유저 목록 페이지
        } else {
            model.addAttribute("error", "아이디와 비밀번호를 확인해주세요.");
            return "login"; // 실패 시 로그인 페이지로 돌아감
        }
    }
}