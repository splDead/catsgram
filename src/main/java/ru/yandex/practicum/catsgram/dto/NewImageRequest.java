package ru.yandex.practicum.catsgram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewImageRequest {
    private long postId;
    private String originalFileName;
    private String filePath;
}
