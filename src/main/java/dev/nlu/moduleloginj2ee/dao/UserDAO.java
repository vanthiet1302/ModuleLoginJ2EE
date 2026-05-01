package dev.nlu.moduleloginj2ee.dao;

import dev.nlu.moduleloginj2ee.entity.User;
import dev.nlu.moduleloginj2ee.util.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDAO {

    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    public void saveUser(User user){
        Transaction transaction = null;
        try (var session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            session.persist(user);
            session.flush();

            session.createNativeMutationQuery(
                            "INSERT INTO user_roles(user_id, role_id) "
                                    + "SELECT :userId, r.id FROM roles r "
                                    + "WHERE r.name = :roleName "
                                    + "AND NOT EXISTS ("
                                    + "SELECT 1 FROM user_roles ur WHERE ur.user_id = :userId AND ur.role_id = r.id)")
                    .setParameter("userId", user.getId())
                    .setParameter("roleName", "CUSTOMER")
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            log.error("Lỗi save User", e);
            throw new IllegalStateException("Lỗi save User", e);
        }
    }

    public User findById(Long id){
        String hql = "SELECT DISTINCT u FROM User u " +
                "LEFT JOIN FETCH u.roles r " +
                "LEFT JOIN FETCH r.permissions " +
                "LEFT JOIN FETCH u.userPermissionGrants g " +
                "LEFT JOIN FETCH g.permission " +
                "WHERE u.id = :id";

        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        }
    }

    public User findByEmail(String email) {
        String hql = "SELECT DISTINCT u FROM User u "
                + "JOIN FETCH u.credentials "
                + "LEFT JOIN FETCH u.roles r "
                + "LEFT JOIN FETCH r.permissions "
                + "LEFT JOIN FETCH u.userPermissionGrants g "
                + "LEFT JOIN FETCH g.permission "
                + "WHERE u.email = :email";

        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }

    public List<User> findAllUsers() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM User u "
                    + "LEFT JOIN FETCH u.roles r "
                    + "LEFT JOIN FETCH r.permissions "
                    + "LEFT JOIN FETCH u.userPermissionGrants g "
                    + "LEFT JOIN FETCH g.permission "
                    + "ORDER BY u.firstName, u.email";
            return session.createQuery(hql, User.class).list();
        }
    }

    public boolean verifyEmailByToken(String token) {
        Transaction transaction = null;
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            int updated = session.createMutationQuery(
                    "UPDATE UserCredential uc SET uc.emailVerified = true WHERE uc.verificationToken = :token")
                    .setParameter("token", token)
                    .executeUpdate();

            transaction.commit();
            log.info("Email verification tokens updated: " + updated);
            return updated > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Lỗi verify email", e);
            throw new IllegalStateException("Lỗi verify email", e);
        }
    }
}
