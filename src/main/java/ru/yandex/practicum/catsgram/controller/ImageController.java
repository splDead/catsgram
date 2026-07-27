package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.model.ImageData;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{postId}/images")
    @ResponseStatus(HttpStatus.OK)
    public ImageDto uploadImage(
            @PathVariable long postId,
            @RequestParam("file") MultipartFile file) throws IOException {

        ImageData imageData = new ImageData(file.getBytes(), file.getOriginalFilename());

        return imageService.savePostImage(postId, imageData);
    }
}
