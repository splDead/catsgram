package ru.yandex.practicum.catsgram.mapper;

import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.NewImageRequest;
import ru.yandex.practicum.catsgram.model.Image;

import java.nio.file.Paths;

public class ImageMapper {
    public static Image mapToImage(NewImageRequest request) {
        Image image = new Image();
        image.setPostId(request.getPostId());
        image.setOriginalFileName(request.getOriginalFileName());
        image.setFilePath(request.getFilePath());

        return image;
    }

    public static ImageDto mapToImageDto(Image image, String baseUrl) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setPostId(image.getPostId());
        imageDto.setOriginalFileName(image.getOriginalFileName());

        String filename = Paths.get(image.getFilePath()).getFileName().toString();

        imageDto.setUrl(baseUrl + "/api/images/" + filename);

        return imageDto;
    }
}
