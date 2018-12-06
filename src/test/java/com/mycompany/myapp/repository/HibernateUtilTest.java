package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.util.HibernateUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

public class HibernateUtilTest {

    @Test
    public void testSaveNRetrieveMessage(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Assertions.assertThat(session).isNotNull();

        session.beginTransaction();

        Message message =  new Message("Hello World with Hibernate");

        session.save(message);
        session.getTransaction().commit();
        session.close();

        System.out.println(message);

        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Message messageNew = session.get(Message.class,message.getId());

        session.close();

        Assertions.assertThat(messageNew.getText()).isEqualTo(message.getText());


    }

    @Test
    public void testPersistPerson() throws Exception{
        Session session  =  HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();

        try{
            transaction.begin();
            Address address  =  new Address("200 Main Street","Seatle","85123");
            Person person = new Person("Ruby");
            person.setBillingAddress(address);
            person.setHomeAddress(address);
            session.save(person);
            transaction.commit();

            transaction.begin();
            Person newPerson = session.get(Person.class,person.getId());
            Assertions.assertThat(newPerson).isNotNull();
            Assertions.assertThat(newPerson.getBillingAddress()).isNotNull();
            Assertions.assertThat(newPerson.getHomeAddress()).isNotNull();
            System.out.println(newPerson);
        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }


    }


    @Test
    public void testStudentNGuidePersistence() throws  Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        Guide guide  =  new Guide("32424","Mike",4000);
        Student student = new Student("123212","John Smith",guide);
        Student studentGet;
        Guide guideGet;


        try{
            transaction.begin();


            /*session.save(guide);
            session.save(student);*/

            // Test Cascade Persist
            session.persist(student);

            transaction.commit();

        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();



        try{
            transaction.begin();

            studentGet = session.get(Student.class,student.getId());



            transaction.commit();

        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }

        Assertions.assertThat(studentGet).isNotNull();
        Assertions.assertThat(studentGet.getGuide()).isNotNull();

        System.out.println(studentGet);

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();

        // Test Cascade Remove
        try{
            transaction.begin();

            studentGet = session.get(Student.class,student.getId());
            session.delete(studentGet);

            guideGet = session.get(Guide.class,1L);



            transaction.commit();

        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }


        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();



        try{
            transaction.begin();

            studentGet = session.get(Student.class,student.getId());



            transaction.commit();

        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }


    }

    @Test
    public void  persistBidirectionalGuideStudent(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        Guide guide  =  new Guide("32424","Mike",4000);
        Guide guide_2  =  new Guide("13124","Molden",2000);
        Student student = new Student("123212","John Smith",guide);
        Student student_2 = new Student("64757689","John Sina",guide_2);

        guide.addStudent(student);
        guide_2.addStudent(student_2);

        List<Student> studentGetList;
        List<Guide> guideGetList;


        try{
            transaction.begin();


            // Test Cascade Persist Guide
            session.persist(guide);
            session.persist(guide_2);

            transaction.commit();

        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();

        // Test Cascade Remove
        try{
            transaction.begin();

            studentGetList = session.createQuery("from Student").list();
            guideGetList = session.createQuery("from Guide").list();

            Assertions.assertThat(studentGetList.size()).isEqualTo(2);
            Assertions.assertThat(guideGetList.size()).isEqualTo(2);

            transaction.commit();

        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(session!=null){
                session.close();
            }
        }
    }
}
