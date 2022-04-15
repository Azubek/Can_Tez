package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id", unique = true)
    private Integer studentID;

    @NotNull
    @Column(name = "student_no", nullable = false)
    private String studentNo;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "mobile")
    private String mobile;

    @NotNull
    @Column(name = "date_of_join", nullable = false)
    private LocalDate dateOfJoin;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToMany(mappedBy = "studentIDS")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classroomIDS", "studentIDS", "lecturerIDS" }, allowSetters = true)
    private Set<Classroom> clasroomIDS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStudentID() {
        return this.studentID;
    }

    public Student studentID(Integer studentID) {
        this.setStudentID(studentID);
        return this;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getStudentNo() {
        return this.studentNo;
    }

    public Student studentNo(String studentNo) {
        this.setStudentNo(studentNo);
        return this;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getEmail() {
        return this.email;
    }

    public Student email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public Student password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Student surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Student dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Student mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDate getDateOfJoin() {
        return this.dateOfJoin;
    }

    public Student dateOfJoin(LocalDate dateOfJoin) {
        this.setDateOfJoin(dateOfJoin);
        return this;
    }

    public void setDateOfJoin(LocalDate dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Student status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<Classroom> getClasroomIDS() {
        return this.clasroomIDS;
    }

    public void setClasroomIDS(Set<Classroom> classrooms) {
        if (this.clasroomIDS != null) {
            this.clasroomIDS.forEach(i -> i.removeStudentID(this));
        }
        if (classrooms != null) {
            classrooms.forEach(i -> i.addStudentID(this));
        }
        this.clasroomIDS = classrooms;
    }

    public Student clasroomIDS(Set<Classroom> classrooms) {
        this.setClasroomIDS(classrooms);
        return this;
    }

    public Student addClasroomID(Classroom classroom) {
        this.clasroomIDS.add(classroom);
        classroom.getStudentIDS().add(this);
        return this;
    }

    public Student removeClasroomID(Classroom classroom) {
        this.clasroomIDS.remove(classroom);
        classroom.getStudentIDS().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", studentID=" + getStudentID() +
            ", studentNo='" + getStudentNo() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", dateOfJoin='" + getDateOfJoin() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
