package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEvent {
    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyWord;

    @NotNull
    @Valid
    private UserDto userDto;
    private int userId;

    @Builder.Default
    private int voteNum = 0;

    public RsEvent(@NotEmpty String eventName, @NotEmpty String keyWord, @NotNull @Valid UserDto userDto, int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userDto = userDto;
        this.userId = userId;
    }
}
