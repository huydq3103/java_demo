package org.example.javademo.response;

// DTO để nhận thông tin login từ FE
public record AuthRequest(String username, String password) { }

