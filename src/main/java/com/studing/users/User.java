package com.studing.users;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String username;
    private String password;
    private LocalDateTime insertedDateAtUtc;
    private LocalDateTime updatedDateAtUtc;
}