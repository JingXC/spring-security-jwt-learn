package com.example.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysMenu {
    private Integer id;
    private Integer pid;
    private Integer type;
    private String name;
    private String code;
}
