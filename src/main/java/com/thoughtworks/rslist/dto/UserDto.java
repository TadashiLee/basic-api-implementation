package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
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

    @Builder.Default
    private Integer vote = 10;

    public UserDto(@NotEmpty @Size(max = 8) String name, @NotEmpty String gender, @NotNull @Max(100) @Min(18) Integer age, @NotEmpty @Email String email, @NotEmpty @Pattern(regexp = "^1\\d{10}$") String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
