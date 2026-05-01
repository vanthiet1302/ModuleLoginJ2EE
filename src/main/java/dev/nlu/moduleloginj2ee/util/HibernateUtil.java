package dev.nlu.moduleloginj2ee.util;

import dev.nlu.moduleloginj2ee.entity.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    @Getter
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration config = new Configuration();
            config.configure("hibernate.cfg.xml");

            config.addAnnotatedClass(User.class);
            config.addAnnotatedClass(Session.class);
            config.addAnnotatedClass(UserCredential.class);
            config.addAnnotatedClass(LoginAttempt.class);
            config.addAnnotatedClass(OAuthAccount.class);

            config.addAnnotatedClass(Role.class);
            config.addAnnotatedClass(Permission.class);
            config.addAnnotatedClass(UserPermissionGrant.class);
            config.addAnnotatedClass(PasswordResetRequest.class);


            sessionFactory = config.buildSessionFactory();
        } catch (Exception e) {
            log.error("Lỗi khởi tạo SessionFactory", e);
        }
    }

    public static void shutdown() {
        if (getSessionFactory() != null)
            getSessionFactory().close();
    }
}
