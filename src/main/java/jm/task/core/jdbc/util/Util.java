package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util {
    // реализуйте настройку соеденения с БД
    private Util(){

    }

   private final static String USER = "root";
    private final static String PASSWORD = "7943579";
    private final static String URL = "jdbc:mysql://localhost:3306/MainbBase";
    private  static Connection connection;

    public static Connection getConnection()  {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            // System.out.println("Есть соединение");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // System.out.println("Нет соединения");
        }
        return connection;
    }
    public static void connectionClose(){
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
        }
    }

    //реализация hibernate

    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory(){
       if (sessionFactory==null) {
           try {
               Configuration configuration = new Configuration();
               Properties settings = new Properties();
               settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
               settings.put(Environment.URL, URL);
               settings.put(Environment.USER, USER);
               settings.put(Environment.PASS, PASSWORD);
               settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
               settings.put(Environment.SHOW_SQL, "true");
               settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
               //settings.put(Environment.HBM2DDL_AUTO, "create-drop");
               configuration.setProperties(settings);
               configuration.addAnnotatedClass(User.class);
               ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
               sessionFactory = configuration.buildSessionFactory(serviceRegistry);//сомнения
               System.out.println("Есть соединение");

           } catch (Exception e) {
               e.printStackTrace();
               System.out.println("Соединение не выполнено");
           }

       }
       return sessionFactory;

    }
    public static void FactoryClose(){
        sessionFactory.close();
    }


}
