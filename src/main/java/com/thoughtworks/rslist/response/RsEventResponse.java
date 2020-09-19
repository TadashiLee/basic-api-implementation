package com.thoughtworks.rslist.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventResponse {
    private String eventName;
    private String keyWord;
    private Integer id;
    private int votNum;
}
