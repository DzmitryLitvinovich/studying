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
public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    private Boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;
}