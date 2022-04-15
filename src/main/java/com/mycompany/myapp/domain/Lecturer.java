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
 * A Lecturer.
 */
@Entity
@Table(name = "lecturer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lecturer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lecturer_id", unique = true)
    private Integer lecturerID;

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

    @ManyToMany
    @JoinTable(
        name = "rel_lecturer__clasroomid",
        joinColumns = @JoinColumn(name = "lecturer_id"),
        inverseJoinColumns = @JoinColumn(name = "clasroomid_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classroomIDS", "studentIDS", "lecturerIDS" }, allowSetters = true)
    private Set<Classroom> clasroomIDS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lecturer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLecturerID() {
        return this.lecturerID;
    }

    public Lecturer lecturerID(Integer lecturerID) {
        this.setLecturerID(lecturerID);
        return this;
    }

    public void setLecturerID(Integer lecturerID) {
        this.lecturerID = lecturerID;
    }

    public String getEmail() {
        return this.email;
    }

    public Lecturer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public Lecturer password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public Lecturer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Lecturer surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Lecturer dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Lecturer mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Set<Classroom> getClasroomIDS() {
        return this.clasroomIDS;
    }

    public void setClasroomIDS(Set<Classroom> classrooms) {
        this.clasroomIDS = classrooms;
    }

    public Lecturer clasroomIDS(Set<Classroom> classrooms) {
        this.setClasroomIDS(classrooms);
        return this;
    }

    public Lecturer addClasroomID(Classroom classroom) {
        this.clasroomIDS.add(classroom);
        classroom.getLecturerIDS().add(this);
        return this;
    }

    public Lecturer removeClasroomID(Classroom classroom) {
        this.clasroomIDS.remove(classroom);
        classroom.getLecturerIDS().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lecturer)) {
            return false;
        }
        return id != null && id.equals(((Lecturer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lecturer{" +
            "id=" + getId() +
            ", lecturerID=" + getLecturerID() +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", mobile='" + getMobile() + "'" +
            "}";
    }
}
