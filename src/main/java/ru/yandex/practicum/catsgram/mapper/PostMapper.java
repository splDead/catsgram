package ru.yandex.practicum.catsgram.mapper;

import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;

public class PostMapper {
    public static Post mapToPost(NewPostRequest request) {
        Post post = new Post();
        post.setAuthorId(request.getAuthorId());
        post.setDescription(request.getDescription());
        post.setPostDate(Instant.now());

        return post;
    }

    public static PostDto mapToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setAuthorId(post.getAuthorId());
        postDto.setDescription(post.getDescription());
        postDto.setPostDate(Instant.now());

        return postDto;
    }

    public static Post updatePostFields(Post post, UpdatePostRequest request) {
        if (request.hasDescription()) {
            post.setDescription(request.getDescription());
        }

        return post;
    }
}
