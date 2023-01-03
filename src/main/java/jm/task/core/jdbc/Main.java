package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("John", "Doe", (byte) 30);
        userService.saveUser("Jane", "Doe", (byte) 28);
        userService.saveUser("Ivan", "Ivanov", (byte) 45);
        userService.saveUser("Petr", "Petrov", (byte) 30);
        userService.getAllUsers().forEach(x -> System.out.println(x.toString()));
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeSessionFactory();
    }
}
