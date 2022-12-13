package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }
    private Transaction transaction=null;
    private SessionFactory factory=Util.getSessionFactory();

    @Override
    public void createUsersTable() {
    try (Session session =factory.getCurrentSession()){
     transaction=session.beginTransaction();
        session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                "name VARCHAR(45), lastName VARCHAR(45), age INT(3))").addEntity(User.class).executeUpdate();
        session.getTransaction().commit();
        System.out.println(" Таблица создана");
    }
    catch (Exception e){

    }

    }

    @Override
    public void dropUsersTable() {
        try (Session session =factory.getCurrentSession()){
            transaction=session.beginTransaction();
            session.createSQLQuery(" DROP TABLE IF EXISTS users").addEntity(User.class).executeUpdate();
            transaction.commit();
        }
        catch (Exception e){

        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        try (Session session =factory.getCurrentSession()){
            transaction=session.beginTransaction();
            session.save(user);
            transaction.commit();
            System.out.println("User  с именем " + name + " добавлен в базу");
        }
        catch (Exception e){
            rollbackQuietly(transaction);
        }

    }

    @Override
    public void removeUserById(long id) {
        User user = new User();
        try (Session session =factory.getCurrentSession()){
            transaction=session.beginTransaction();
            user=session.load(User.class,id);
            session.delete(user);
            transaction.commit();
        }
        catch (Exception e){
            rollbackQuietly(transaction);
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users= new ArrayList<>();
        try (Session session =factory.getCurrentSession()){
            transaction=session.beginTransaction();
            users=session.createQuery("from User ").getResultList();
            transaction.commit();
        }
        catch (Exception e){
            rollbackQuietly(transaction);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session =factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            rollbackQuietly(transaction);

        }

    }
    private void rollbackQuietly(Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
            } catch (Exception e) {
            }
        }
    }



}
