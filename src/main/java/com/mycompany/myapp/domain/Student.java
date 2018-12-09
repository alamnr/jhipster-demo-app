package com.mycompany.myapp.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "enrollment_id",nullable = false)
    private String enrollmentId;

    private String name;
    // Test Lazy Fetching
    //@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.Lazy)
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "guide_id")
    private Guide guide;

    public Student(){}

    public Student(String enrollmentId, String name) {
        this.enrollmentId = enrollmentId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return !(student.getId() == null || getId() == null) && Objects.equals(id, student.id) ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", enrollmentId='" + enrollmentId + '\'' +
            ", name='" + name + '\'' +
            ", guide=" + guide +
            '}';
    }
}
