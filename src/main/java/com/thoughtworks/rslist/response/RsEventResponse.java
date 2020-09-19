package com.thoughtworks.rslist.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventResponse {
    private String newName;
    private String newKey;
    @NotNull
    private int userId;
}
