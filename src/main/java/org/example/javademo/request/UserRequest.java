package org.example.javademo.request;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String userName;

    private String password;

    private String email;

    private String fullName;

    private boolean active;
}
