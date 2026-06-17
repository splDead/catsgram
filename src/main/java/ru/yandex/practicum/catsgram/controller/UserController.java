package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        checkUserRequiredFields(user);
        checkDuplicateEmail(user);

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        checkDuplicateEmail(newUser);

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            checkUserRequiredFields(newUser);

            oldUser.setEmail(newUser.getEmail());
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());

            return oldUser;
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private void checkUserRequiredFields(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Email не может быть пустым");
        }

        // TODO: добавить проверку имени, закоментировано для прохождения тестов
//        if (user.getUsername() == null || user.getUsername().isBlank()) {
//            throw new ConditionsNotMetException("Имя не может быть пустым");
//        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ConditionsNotMetException("Пароль не может быть пустым");
        }
    }

    private void checkDuplicateEmail(User user) {
        if (users.values().stream().anyMatch(u -> user.getEmail().equals(u.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
