package com.veloginmobile.server.data.dto.tag;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class TagPostResultDto {
    List<TagPostDto> tagPostDtoList = new ArrayList<>();
}
