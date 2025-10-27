package kr.adapterz.springjwt.controller;

import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.adapterz.springjwt.entity.User;
import kr.adapterz.springjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) accessToken = cookie.getValue();
                if ("refreshToken".equals(cookie.getName())) refreshToken = cookie.getValue();
            }
        }
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);

        return "index";
    }

    @GetMapping("/users/me")
    public String userDetail(HttpServletRequest request, Model model) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.getUser(userId);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "user-detail";
    }

    @GetMapping("/login")
    public String loginForm(
            @RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String accessToken = userService.loginUser(email, password, response);

        if (accessToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디또는 비밀번호가 잘못되었습니다.");

            return "redirect:/login";
        }

        return "redirect:/index";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        userService.logoutUser(response);

        return "redirect:/login";
    }

    @PostMapping("/refresh")
    @ResponseBody
    public Map<String, String> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return Map.of("error", "Refresh token missing");
        }

        try {
            var tokenRes = userService.refreshTokens(refreshToken, response);

            if (tokenRes == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                return Map.of("error", "Refresh token invalid or expired");
            }

            return Map.of(
                    "accessToken", tokenRes.accessToken(),
                    "refreshToken", tokenRes.refreshToken()
            );
        } catch (ResponseStatusException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return Map.of("error", "Refresh token invalid or expired");
        }
    }
}