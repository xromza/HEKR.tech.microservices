package ru.xromza.user.interfaces;

import ru.xromza.user.model.User;

public interface UserProvider {
    User getApprovedUserByLogin(String login);
    User getSystem();
}
