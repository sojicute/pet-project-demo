package com.sojicute.petprojectdemo.dao;

import com.sojicute.petprojectdemo.domain.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {


    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user = (User) sessionFactory.getCurrentSession()
                .createQuery("select distinct u from User u where u.username = :username")
                .setParameter("username", username)
                .uniqueResult();

        return Optional.ofNullable(user);

    }

    @Override
    public void save(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
    }


}
