package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;


public class UserServiceTest extends AbstractTest {

    @Test
    public void create() throws Exception {
        User created = userService.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                userService.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() throws Exception {
        userService.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> userService.delete(NOT_FOUND));
    }

    @Test
    public void get() throws Exception {
        User user = userService.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> userService.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() throws Exception {
        User user = userService.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, ADMIN);
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated();
        userService.update(updated);
        USER_MATCHER.assertMatch(userService.get(USER_ID), getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = userService.getAll();
        USER_MATCHER.assertMatch(all, ADMIN, USER);
    }
}