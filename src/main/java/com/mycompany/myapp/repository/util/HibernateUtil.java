package com.mycompany.myapp.repository.util;

import com.fasterxml.classmate.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    //private static final SessionFactory sessionFactory = buildSessionFactory();

    private static class SingletonHolder {
        public static final SessionFactory sessionFactory = buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {

        try {
            // Create the session  factory from  hibernate.cfg.xml

            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure( "hibernate.cfg.xml" )
                .build();

            Metadata metadata = new MetadataSources( standardRegistry )
                .getMetadataBuilder()
                .build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            log.error("Error building hibernate session factory, details at- {}", ex.getStackTrace());
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    /*public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }*/

    public static SessionFactory getSessionFactory() {
        return SingletonHolder.sessionFactory;
    }
}
