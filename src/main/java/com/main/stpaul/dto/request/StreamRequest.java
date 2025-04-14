package com.main.stpaul.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamRequest {
    private String stream;
    private String subStream;
    private String medium="English";
    private List<String> subjects;
}
