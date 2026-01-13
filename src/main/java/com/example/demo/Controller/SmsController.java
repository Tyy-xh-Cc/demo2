package com.example.demo.Controller;

import com.example.demo.entity.Dto.SmsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/common")
@CrossOrigin(origins = "*")
public class SmsController {
    public static HashMap<String, String> smsCodes = new HashMap<>();
    @PostMapping("/sms")
    public ResponseEntity<String> sendSmsCode(@RequestBody SmsRequest smsRequest) {
        // 验证请求参数
        if (smsRequest.getPhone() == null || smsRequest.getType() == null) {
            return ResponseEntity.badRequest().body("Phone number and type are required.");
        }

        // 模拟发送验证码逻辑
        String verificationCode = generateVerificationCode();
        // 这里可以调用短信服务提供商的 API 发送验证码
        smsCodes.put(smsRequest.getPhone(), verificationCode);
        new Thread(() -> {
            try {
                // 模拟验证码有效期为5分钟
                Thread.sleep(5 * 60 * 1000);
                smsCodes.remove(smsRequest.getPhone());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // 返回成功响应
        return ResponseEntity.ok(verificationCode);
    }

    private String generateVerificationCode() {
        // 生成6位随机验证码
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }
}