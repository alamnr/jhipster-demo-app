package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.util.HibernateUtil;
import com.mycompany.myapp.repository.util.JpaEntityManagerUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        Student student = new Student("123212","John Smith");
        student.setGuide(guide);
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
        Student student = new Student("123212","John Smith");
        Student student_2 = new Student("64757689","John Sina");
        student.setGuide(guide);
        student_2.setGuide(guide_2);

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
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Student> query = builder.createQuery(Student.class);
            Root<Student> root = query.from(Student.class);
            query.select(root);
            Query<Student> q = session.createQuery(query);

            studentGetList = q.getResultList();

            CriteriaQuery<Guide> query_ = builder.createQuery(Guide.class);
            Root<Guide> root_ = query_.from(Guide.class);
            query_.select(root_);
            Query<Guide> q_ = session.createQuery(query_);
            guideGetList = q_.getResultList();

            System.out.println(studentGetList);
            System.out.println(guideGetList);

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

    @Test
    public void testOwnerAndInverseSideAssociationUpdate(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        Guide guide_1  =  new Guide("32424","Mike",4000);
        Guide guide_2  =  new Guide("13124","Molden",2000);
        Student student_1 = new Student("123212","John Smith");
        Student student_2 = new Student("64757689","John Sina");
        student_1.setGuide(guide_1);
        student_2.setGuide(guide_2);

        guide_1.addStudent(student_1);
        guide_2.addStudent(student_2);

        List<Student> studentGetList;
        List<Guide> guideGetList;


        try{
            transaction.begin();


            // Test Cascade Persist Guide
            session.persist(guide_1);
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
            guide_1  = (Guide)session.createQuery("from Guide").list().get(0);

            guide_1.setSalary(10000);
            guide_1.addStudent(studentGetList.get(1));



            System.out.println(studentGetList);
            //System.out.println(guideGetList);



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

        System.out.println(guide_1);
        Assertions.assertThat(studentGetList.size()).isEqualTo(2);

        Assertions.assertThat(guide_1.getSalary()).isEqualTo(10000);
        Assertions.assertThat(studentGetList.get(1).getGuide().getId()).isEqualTo(guide_1.getId());
    }

    @Test
    public void testOneToOneWithCascade(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        Passport passport;
        Customer customer;
        List<Passport> passportList;
        List<Customer> customerList;
        try{
            transaction.begin();
            passport = new Passport("Pass-12234455");
            customer = new Customer("Kasem");
            customer.setPassport(passport);

            session.persist(customer);

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
        transaction  = session.getTransaction();
        try{
            transaction.begin();
            passportList = session.createQuery("from Passport").list();
            customerList = session.createQuery("from Customer").list();

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
        /*System.out.println(passportList);
        System.out.println(customerList);*/
        Assertions.assertThat(passportList.size()).isEqualTo(1);
        Assertions.assertThat(customerList.size()).isEqualTo(1);
    }

    @Test
    public void testManyToMany(){
        Session session  =  HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();

        try{
            transaction.begin();
            Movie movie_1 = new Movie("Ganja");
            Movie movie_2  = new Movie("Baba");

            Actor actor_1 = new Actor("Jack");
            Actor actor_2 = new Actor("Daniel");

            movie_1.addActor(actor_1);

            movie_2.addActor(actor_1);
            movie_2.addActor(actor_2);

            session.persist(movie_1);
            session.persist(movie_2);

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
        transaction  = session.getTransaction();
        List<Movie> movieList;
        List<Actor> actorList;
        try{
            transaction.begin();
            movieList = session.createQuery("from Movie").list();
            actorList = session.createQuery("from Actor").list();

            actorList.get(1).addMovies(movieList.get(1));

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
        /*System.out.println(movieList);
        System.out.println(actorList);*/
        Assertions.assertThat(movieList.size()).isEqualTo(2);
        Assertions.assertThat(actorList.size()).isEqualTo(2);
    }


    @After
    public void cleanUp(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();


        List<Student> studentGetList;
        List<Guide> guideGetList;

        List<Customer> customerList;
        List<Passport> passportList;




        // Test Cascade Remove
        try{
            transaction.begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Student> query = builder.createQuery(Student.class);
            Root<Student> root = query.from(Student.class);
            query.select(root);
            Query<Student> q = session.createQuery(query);

            studentGetList = q.getResultList();

            CriteriaQuery<Guide> query_ = builder.createQuery(Guide.class);
            Root<Guide> root_ = query_.from(Guide.class);
            query_.select(root_);
            Query<Guide> q_ = session.createQuery(query_);
            guideGetList = q_.getResultList();

            passportList = session.createQuery("from Passport").list();
            customerList = session.createQuery("from Customer").list();

            List<Movie> movieList = session.createQuery("from Movie").list();
            List<Actor> actorList = session.createQuery("from Actor").list();

            List<Message> messageList = session.createQuery("from Message").list();


/*            System.out.println(studentGetList);
            System.out.println(guideGetList);

            System.out.println(passportList);
            System.out.println(customerList);*/


            for (Message msg: messageList) {
                session.delete(msg);
            }

            for (Student student: studentGetList) {
                session.delete(student);
            }

            for (Guide guide: guideGetList) {
                session.delete(guide);
            }

            for(Customer customer: customerList){
                session.delete(customer);
            }


            for(Passport passport: passportList){
                session.delete(passport);
            }

            for(Movie movie: movieList){
                session.delete(movie);
            }

            for(Actor actor: actorList){
                session.delete(actor);
            }

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
