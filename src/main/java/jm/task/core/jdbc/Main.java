package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.SessionFactory;

public class Main {


    public static void main(String[] args) {
        // реализуйте алгоритм здесь

       UserService userService = new UserServiceImpl();
       userService.createUsersTable();
      userService.saveUser("Ivan", "Petrov", (byte) 28);
        userService.saveUser("Olga", "Ivanova", (byte) 35);
        userService.saveUser("Ksenia", "Sidorova", (byte) 25);
        userService.saveUser("Galina", "Galinkina", (byte) 45);
        System.out.println(userService.getAllUsers());
      userService.cleanUsersTable();
       userService.dropUsersTable();
        Util.FactoryClose();
        //System.out.println("Успешно");





    }
}
