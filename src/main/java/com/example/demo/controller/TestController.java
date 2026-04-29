package com.example.demo.controller;

import com.example.demo.entity.users;
import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TestController {

    @Autowired
    private userRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/signup")
    public String signup(@RequestBody Map<String, Object> formData) {

        String firstname = (String) formData.get("firstname");
        String lastname = (String) formData.get("lastname");
        String countryCode = (String) formData.get("countryCode");
        String mobile = String.valueOf(formData.get("mobile"));
        String password = (String) formData.get("password");
        String email = (String) formData.get("email");
        String gender = (String) formData.get("gender");

        try {

            if (firstname == null || firstname.isEmpty() ||
                    mobile == null || mobile.isEmpty() ||
                    countryCode == null || countryCode.isEmpty() ||
                    password == null || password.isEmpty() ||
                    gender == null || gender.isEmpty()) {

                return "Required fields missing ❌";
            }

            String mobileCheckSql = "SELECT COUNT(*) FROM users WHERE mobile_number = ?";
            Integer mobileCount = jdbcTemplate.queryForObject(mobileCheckSql, Integer.class, mobile);

            if (mobileCount != null && mobileCount > 0) {
                return "Mobile number already registered ❌";
            }

            if (email != null && !email.trim().isEmpty()) {

                String emailCheckSql = "SELECT COUNT(*) FROM users WHERE email = ?";
                Integer emailCount = jdbcTemplate.queryForObject(emailCheckSql, Integer.class, email);

                if (emailCount != null && emailCount > 0) {
                    return "Email already registered ❌";
                }

            } else {
                email = null;
            }

            String insertSql = "INSERT INTO users " +
                    "(first_name, last_name, country_code, mobile_number, email, password, gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(insertSql,
                    firstname,
                    lastname,
                    countryCode,
                    mobile,
                    email,
                    password,
                    gender
            );

            return "Signup Successful ✅";

        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong";
        }
    }

    @PostMapping("/send-otp")
    public String sendOtp() {
        System.out.println("OTP  called");
        return "success";
    }

    @PostMapping("/otpemail")
    public String otpemail() {
        System.out.println("OTP  called");
        return "success";
    }


    @PostMapping("/login")
    public String login(@RequestBody Map<String, Object> formData) {

        String email = (String) formData.get("email");
        String password = (String) formData.get("password");

        try {

            users user = userRepository.findByEmail(email);

            if (user == null) {
                return "User Not Found";
            }

            if (user.getPassword().equals(password)) {
                return "Welcome "+user.getName();
            } else {
                return "Wrong Password";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong";
        }
    }
}