package ru.yandex.practicum.catsgram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private long postId;
    private String originalFileName;
    private String url;
}
