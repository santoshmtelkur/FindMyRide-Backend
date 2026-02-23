package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/signup")
    public String signup(@RequestBody Map<String, Object> formData) {

        String firstname = (String) formData.get("firstname");
        String lastname = (String) formData.get("lastname"); // optional
        String countryCode = (String) formData.get("countryCode");
        String mobile = (String) formData.get("mobile");
        String password = (String) formData.get("password");
        String email = (String) formData.get("email"); // optional
        String gender = (String) formData.get("gender");

        try {

            // ✅ Check required fields
            if (firstname == null || firstname.isEmpty() ||
                    mobile == null || mobile.isEmpty() ||
                    countryCode == null || countryCode.isEmpty() ||
                    password == null || password.isEmpty() ||
                    gender == null || gender.isEmpty()) {

                return "Required fields missing ❌";
            }

            // ✅ Check mobile already exists
            String mobileCheckSql = "SELECT COUNT(*) FROM register WHERE mobile_number = ?";
            Integer mobileCount = jdbcTemplate.queryForObject(mobileCheckSql, Integer.class, mobile);

            if (mobileCount != null && mobileCount > 0) {
                return "Mobile number already registered ❌";
            }

            // ✅ Check email only if provided
            if (email != null && !email.trim().isEmpty()) {

                String emailCheckSql = "SELECT COUNT(*) FROM register WHERE email = ?";
                Integer emailCount = jdbcTemplate.queryForObject(emailCheckSql, Integer.class, email);

                if (emailCount != null && emailCount > 0) {
                    return "Email already registered ❌";
                }

            } else {
                email = null; // store as NULL in DB if empty
            }

            // ✅ Insert data
            String insertSql = "INSERT INTO register " +
                    "(first_name, last_name, country_code, mobile_number, password, email, gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(insertSql,
                    firstname,
                    lastname,     // can be null
                    countryCode,
                    mobile,
                    password,
                    email,        // can be null
                    gender
            );

            return "Signup Successful ✅";

        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong ❌";
        }
    }
}