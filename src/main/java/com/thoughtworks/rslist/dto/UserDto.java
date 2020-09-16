package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(max = 8)
    private String name;

    @NotEmpty
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote = 0;

    public UserDto(String name, String gender, Integer age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
