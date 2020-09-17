package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(max = 8)
//    @JsonProperty("user_name")
    private String name;

    @NotEmpty
    private String gender;

    @NotNull
    @Max(100)
    @Min(18)
    private Integer age;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^1\\d{10}$")
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
