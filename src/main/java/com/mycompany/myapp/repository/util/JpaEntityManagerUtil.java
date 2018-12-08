/**
 * @author DELL
 */

package com.mycompany.myapp.repository.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaEntityManagerUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    private static class SingletonHolder {
        public static final EntityManagerFactory entityManagerFactory  = buildEntityManagerFactory();

        private static EntityManagerFactory buildEntityManagerFactory() {
            try {
                // Create the session  factory from  hibernate.cfg.xml
                return Persistence.createEntityManagerFactory("hello-world-jpa");
            } catch (Throwable ex) {
                log.error("Error building hibernate session factory, details at- {}", ex.getStackTrace());
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

    private JpaEntityManagerUtil(){}

    public static EntityManagerFactory getEntityManagerFactory() {
        return SingletonHolder.entityManagerFactory;
    }

}
