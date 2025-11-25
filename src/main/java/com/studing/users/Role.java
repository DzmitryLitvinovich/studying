package com.studing.users;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Role {
    private Long id;
    private String name;
    private String description;
}