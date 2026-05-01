package dev.nlu.moduleloginj2ee.service;

import dev.nlu.moduleloginj2ee.dao.UserDAO;
import dev.nlu.moduleloginj2ee.entity.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public void createNewUser(User user){
        userDAO.saveUser(user);
    }

    public User findByEmail(String email){
        return userDAO.findByEmail(email);
    }

     public User findById(long id){
        return userDAO.findById(id);
    }

    public boolean verifyEmailByToken(String token) {
        return userDAO.verifyEmailByToken(token);
    }

}
