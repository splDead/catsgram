package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final Comparator<Post> postDateComparator = Comparator.comparing(Post::getPostDate);

    public PostService(UserService userService,
                       PostRepository postRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public List<PostDto> findAll(SortOrder sort, int from, int size) {
        return postRepository.findAll()
                .stream()
                .sorted(sort.equals(SortOrder.ASCENDING) ?
                        postDateComparator : postDateComparator.reversed())
                .map(PostMapper::mapToPostDto)
                .skip(from)
                .limit(size)
                .toList();
    }

    public PostDto create(NewPostRequest request) {
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        long authorId = request.getAuthorId();
        userService.getUserById(authorId);

        Post post = PostMapper.mapToPost(request);

        post = postRepository.save(post);

        return PostMapper.mapToPostDto(post);
    }

    public PostDto update(long postId, UpdatePostRequest request) {
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(post, request))
                .orElseThrow(() -> new NotFoundException("Пост не найден"));

        updatedPost = postRepository.update(updatedPost);

        return PostMapper.mapToPostDto(updatedPost);
    }

    public PostDto findById(long id) {
        return postRepository.findById(id)
                .map(PostMapper::mapToPostDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }
}