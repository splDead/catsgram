package ru.yandex.practicum.catsgram.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
@Validated
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<PostDto> findAll(
            @RequestParam(defaultValue = "descending") String sort,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Значение не может быть меньше 0") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Значение должно быть больше 0") int size
    ) {
        SortOrder order = SortOrder.from(sort);

        if (order == null) {
            throw new ParameterNotValidException("sort", "Значение может быть asc или desc");
        }

        return postService.findAll(order, from, size);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto findById(@PathVariable long postId) {
        return postService.findById(postId);
    }

    @PostMapping
    public PostDto create(@RequestBody NewPostRequest request) {
        return postService.create(request);
    }

    @PutMapping("/{postId}")
    public PostDto update(@PathVariable("postId") long postId, @RequestBody UpdatePostRequest request) {
        return postService.update(postId, request);
    }
}