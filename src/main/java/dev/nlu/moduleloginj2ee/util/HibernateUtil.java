package dev.nlu.moduleloginj2ee.util;

import dev.nlu.moduleloginj2ee.entity.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    static {
        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");

        config.addAnnotatedClass(User.class);


        sessionFactory = config.buildSessionFactory();
    }

    public static void shutdown() {
        if (getSessionFactory() != null)
            getSessionFactory().close();
    }
}
