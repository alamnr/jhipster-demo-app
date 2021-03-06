package com.mycompany.myapp.domain;


import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
// to understand batch size pls see video - https://www.youtube.com/watch?v=vTyvBKLDNv4&index=30&list=PLgzDdh90-m_TKIz4JNuqh3QarIdKiTS3q
@BatchSize(size = 3)
public class Guide {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "staff_id", nullable = false)
    private String staffId;

    private String name;
    private Integer salary;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.PERSIST)
    private Set<Student> students = new HashSet<Student>() ;

    public Guide(){}

    public Guide(String staffId, String name, Integer salary) {
        this.staffId = staffId;
        this.name = name;
        this.salary = salary;
    }

    public void addStudent(Student student){
        this.students.add(student);
        student.setGuide(this);
    }

    public Iterator<Student> getStudentIterator(){
        return this.students.iterator();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Guide{" +
            "id=" + id +
            ", staffId='" + staffId + '\'' +
            ", name='" + name + '\'' +
            ", salary=" + salary +
            '}';
    }
}
