package com.thoughtworks.rslist.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventPatchRequest {
    private String newName;
    private String newKey;
    @NotNull
    private int userId;
}
