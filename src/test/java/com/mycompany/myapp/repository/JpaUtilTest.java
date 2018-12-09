/**
 * @author DELL
 */

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.util.HibernateUtil;
import com.mycompany.myapp.repository.util.JpaEntityManagerUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JpaUtilTest {

    //@Before

    @Test
    public void insertEntity(){
        EntityManager entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();

            Guide guide_1  = new Guide("s-1","Mike",3000);
            Guide guide_2  = new Guide("s-2","Tison",2000);
            Guide guide_3  = new Guide("s-3","Merry",1000);
            Guide guide_4  = new Guide("s-4","Jesus",4000);
            Guide guide_5  = new Guide("s-5","Duke",5000);

            Student student_1 = new Student("E-1","Sona");
            Student student_2 = new Student("E-2","Sunny");
            Student student_3 = new Student("E-3","Fonsi");
            Student student_4 = new Student("E-4","Sally");
            Student student_5 = new Student("E-5","Sourav");
            Student student_6 = new Student("E-6","Jovan");
            Student student_7 = new Student("E-7","Pinky");
            Student student_8 = new Student("E-8","Fiana");
            Student student_9 = new Student("E-9","Sabila");

            guide_1.addStudent(student_1);
            guide_2.addStudent(student_2);
            guide_3.addStudent(student_3);
            guide_4.addStudent(student_4);
            guide_5.addStudent(student_5);
            guide_5.addStudent(student_7);
            guide_5.addStudent(student_8);
            guide_5.addStudent(student_9);

            entityManager.persist(guide_1);
            entityManager.persist(guide_2);
            entityManager.persist(guide_3);
            entityManager.persist(guide_4);
            entityManager.persist(guide_5);

            entityManager.persist(student_6);

            transaction.commit();
        }
        catch (Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }


    }


    @Test
    public void  testJpaEntityManagerFactory() throws  Exception{
        // copy and paste  persistent.xml in to target/classes/META-INF/
        EntityManager entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();

            Message msg  = new Message("Hello World with Hibernate as JPA Provider");
            entityManager.persist(msg);

            transaction.commit();
        }
        catch (Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
        List<Message> msgFetchList = null;
        entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        transaction = entityManager.getTransaction();
        try{
            transaction.begin();

            msgFetchList   =  entityManager.createQuery("select msg from Message msg", Message.class).getResultList();

            transaction.commit();
        }
        catch (Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
        System.out.println(msgFetchList.get(0));
        Assertions.assertThat(msgFetchList.size()).isGreaterThan(0);

    }

    @Test
    public void testQueringEntities() {
        EntityManager entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();


        // Test Cascade Remove
        try {
            transaction.begin();

            Query query ;
            query = entityManager.createQuery("select guide from Guide as guide");

            List<Guide> guideList = query.getResultList();

            System.out.println("Select All");
            for(Guide guide: guideList){
                System.out.println(guide);
            }

            // Filtering result
            query = entityManager.createQuery("select guide from Guide as guide where guide.salary>2000");
            guideList = query.getResultList();
            System.out.println("Filtering Select ");
            for(Guide guide: guideList){
                System.out.println(guide);
            }


            query = entityManager.createQuery("select guide.name  from Guide as guide");
            List<String> names  =  query.getResultList();
            System.out.println("Select only Name");
            for (String name: names ) {
                System.out.println(name);

            }

            // repporting queries
            query = entityManager.createQuery("select guide.name, guide.salary  from Guide as guide");
            List<Object[]> resultList  =  query.getResultList();

            System.out.println("Report Queries");
            System.out.println("Objec[0] -  Object[1]");
            for (Object[] object: resultList ) {
                System.out.println(object[0]+" - "+ object[1]);

            }

            // Dynamic Query with named parameter
            String name = "Mike";
            query = entityManager.createQuery("select guide  from Guide as guide where guide.name= :guideName");
            query.setParameter("guideName",name);
            Guide  guide  =  (Guide)query.getSingleResult();
            System.out.println("Select by dynamic query");
            System.out.println(guide);

            // Dynamic Query with particular patter

            query = entityManager.createQuery("select guide  from Guide as guide where guide.name like 'M%'");
            guideList  =  query.getResultList();
            System.out.println("Select by dynamic query with particular pattern");
            for(Guide obj: guideList){
                System.out.println(obj);
            }

            // Native Sql Query

            query = entityManager.createNativeQuery("select *  from Guide ", Guide.class);
            guideList  =  query.getResultList();
            System.out.println("Select by native sql query");
            for(Guide obj: guideList){
                System.out.println(obj);
            }

            // Named Query

            guideList = entityManager.createNamedQuery("findByGuide").setParameter("guideName",name).getResultList();

            System.out.println("Select by named sql query");
            for(Guide obj: guideList){
                System.out.println(obj);
            }

            // Aggregate Function

            Long numOfGuides = (Long)entityManager.createQuery("select count(guide) from Guide guide").getSingleResult();
            System.out.println("Aggregate Function");
            System.out.println(numOfGuides);

            Integer maxSalary =  (Integer) entityManager.createQuery("select max(guide.salary) from Guide guide").getSingleResult();
            System.out.println(maxSalary);

            // Joining Association

            List<Student> studentList  = entityManager.createQuery("select student from Student student join student.guide guide").getResultList();
            System.out.println("Inner Join");
            for (Student student: studentList ) {
                System.out.println(student);
            }


            studentList  = entityManager.createQuery("select student from Student student left join student.guide guide").getResultList();
            System.out.println("Left Join");
            for (Student student: studentList ) {
                System.out.println(student);
            }


            List<Object[]> objList = entityManager.createQuery("select student, guide from Student student left join student.guide guide").getResultList();
            System.out.println("Left Join  Report format");
            System.out.println("Object[0]  ---   Object[1]");
            for (Object[] obj: objList ) {
                System.out.println(obj[0]+"---"+obj[1]);
            }

            studentList  = entityManager.createQuery("select student from Student student right join student.guide guide").getResultList();
            System.out.println("Right Join");
            for (Student student: studentList ) {
                System.out.println(student);
            }


            objList = entityManager.createQuery("select student, guide from Student student right join student.guide guide").getResultList();
            System.out.println("Right Join  Report format");
            System.out.println("Object[0]  ---   Object[1]");
            for (Object[] obj: objList ) {
                System.out.println(obj[0]+"---"+obj[1]);
            }


            // Eager fecth Many Association

            guideList = entityManager.createQuery("select guide from Guide guide join fetch guide.students student").getResultList();
            System.out.println("Eager fetch Many Association");
            for (Guide obj:guideList) {
                System.out.println(obj);

            }

            transaction.commit();

            Assertions.assertThat(guideList.size()).isGreaterThan(0);

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Test
    public void testLazyFetching(){

        EntityManager entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Guide guide = null;

        try {
            transaction.begin();
            guide = entityManager.find(Guide.class,3L);
            System.out.println(guide);
            Iterator<Student> iterator =  guide.getStudentIterator();
            List<Student> studentList = new ArrayList<>();
            while (iterator.hasNext()){
                studentList.add(iterator.next());
            }
            System.out.println(studentList);
            transaction.commit();
            Assertions.assertThat(studentList.size()).isNotEqualTo(0);
        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }



    @Test(expected = LazyInitializationException.class)
    public void testLazyFetchingException(){

        EntityManager entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Guide guide = null;
        Iterator<Student> iterator;
        // Test Cascade Remove
        try {
            transaction.begin();
            guide = entityManager.find(Guide.class,3L);
            System.out.println(guide);
            transaction.commit();
        }
        catch(Exception ex){
            if(transaction!=null){
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }

        // out of the scope of em , lazy fetching will throw exception named LazyInitializationException
        iterator = guide.getStudentIterator();
        iterator.hasNext();
        /*org.junit.jupiter.api.Assertions.assertThrows(LazyInitializationException.class,()->{
            iterator.hasNext();
        });*/
    }

    //@After
    @Test
    public void cleanUp(){
        EntityManager entityManager = JpaEntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();


        // Test Cascade Remove
        try{
            transaction.begin();
            List<Message> messageList = entityManager.createQuery("select msg from Message as msg").getResultList();
            List<Student> studentList = entityManager.createQuery("select student form Student as student").getResultList();
            List<Guide> guideList = entityManager.createQuery("select  guide from Guide as guide").getResultList();


/*            System.out.println(studentGetList);
            System.out.println(guideGetList);

            System.out.println(passportList);
            System.out.println(customerList);*/


            for (Message msg: messageList) {
                entityManager.remove(msg);
            }

            for (Student student: studentList) {
                entityManager.remove(student);
            }

            for (Guide guide: guideList) {
                entityManager.remove(guide);
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
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }
}
