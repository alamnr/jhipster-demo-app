package com.mycompany.myapp.repository.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        try{
            // Create the session  factory from  hibernate.cfg.xml

            Configuration configuration =  new Configuration().configure("hibernate.cfg.xml");
            return configuration.buildSessionFactory(new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build());
        }
        catch (Throwable ex){
            log.error("Error building hibernate session factory, details at- {}",ex.getStackTrace());
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil(){}

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
