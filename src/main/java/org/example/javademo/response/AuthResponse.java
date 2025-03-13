package org.example.javademo.response;

import java.util.Map;

public record AuthResponse(String username, String email, Map<String, Object> roles, Map<String, Object> resourceAccess) { }

