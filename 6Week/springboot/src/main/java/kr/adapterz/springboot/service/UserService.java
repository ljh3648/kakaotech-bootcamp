package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public UserService() {
        // 0번 유저 : admin / admin
        users.add(new User("admin", "admin"));

        // 1~99번 유저 더미 생성
        for (int i = 1; i < 100; i++) {
            users.add(new User("user" + i, "pw" + i));
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    // 로그인 검증 메서드
    public boolean validateUser(String id, String password) {
        return users.stream()
                .anyMatch(u -> u.getId().equals(id) && u.getPassword().equals(password));
    }
}