package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsEvent {
    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyWord;
//    private UserDto userDto;

}
