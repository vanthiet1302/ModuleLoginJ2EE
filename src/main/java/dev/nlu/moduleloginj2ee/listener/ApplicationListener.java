package dev.nlu.moduleloginj2ee.listener;

import dev.nlu.moduleloginj2ee.util.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

@Slf4j
@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        log.info("Hibernate SessionFactory initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        log.info("Hibernate SessionFactory destroyed.");
    }
}
