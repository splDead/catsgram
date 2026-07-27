package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.ImageRepository;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.NewImageRequest;
import ru.yandex.practicum.catsgram.exception.NotFoundException; // Предполагается наличие кастомного или стандартного RuntimeException
import ru.yandex.practicum.catsgram.mapper.ImageMapper;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.ImageData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.upload-dir:./uploads/posts}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;

    public ImageService(ImageRepository imageRepository, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
    }

    public ImageDto savePostImage(long postId, ImageData imageData) throws IOException {
        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id " + postId + " не найден"));

        String uniqueFilename = UUID.randomUUID().toString() + getExtension(imageData.getName());
        Path filePath = Paths.get(uploadDir).resolve(uniqueFilename);
        Files.write(filePath, imageData.getData());

        NewImageRequest request = new NewImageRequest(postId, imageData.getName(), filePath.toString());
        Image image = ImageMapper.mapToImage(request);

        Image savedImage = imageRepository.save(image);

        return ImageMapper.mapToImageDto(savedImage, baseUrl);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
