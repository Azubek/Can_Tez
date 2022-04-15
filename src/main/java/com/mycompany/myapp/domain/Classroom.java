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
 * A Classroom.
 */
@Entity
@Table(name = "classroom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "classroom_id", unique = true)
    private Integer classroomID;

    @NotNull
    @Column(name = "year", nullable = false)
    private LocalDate year;

    @NotNull
    @Column(name = "section", nullable = false)
    private Boolean section;

    @OneToMany(mappedBy = "classroom")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classroom" }, allowSetters = true)
    private Set<Grade> classroomIDS = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_classroom__studentid",
        joinColumns = @JoinColumn(name = "classroom_id"),
        inverseJoinColumns = @JoinColumn(name = "studentid_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clasroomIDS" }, allowSetters = true)
    private Set<Student> studentIDS = new HashSet<>();

    @ManyToMany(mappedBy = "clasroomIDS")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clasroomIDS" }, allowSetters = true)
    private Set<Lecturer> lecturerIDS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classroom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClassroomID() {
        return this.classroomID;
    }

    public Classroom classroomID(Integer classroomID) {
        this.setClassroomID(classroomID);
        return this;
    }

    public void setClassroomID(Integer classroomID) {
        this.classroomID = classroomID;
    }

    public LocalDate getYear() {
        return this.year;
    }

    public Classroom year(LocalDate year) {
        this.setYear(year);
        return this;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

    public Boolean getSection() {
        return this.section;
    }

    public Classroom section(Boolean section) {
        this.setSection(section);
        return this;
    }

    public void setSection(Boolean section) {
        this.section = section;
    }

    public Set<Grade> getClassroomIDS() {
        return this.classroomIDS;
    }

    public void setClassroomIDS(Set<Grade> grades) {
        if (this.classroomIDS != null) {
            this.classroomIDS.forEach(i -> i.setClassroom(null));
        }
        if (grades != null) {
            grades.forEach(i -> i.setClassroom(this));
        }
        this.classroomIDS = grades;
    }

    public Classroom classroomIDS(Set<Grade> grades) {
        this.setClassroomIDS(grades);
        return this;
    }

    public Classroom addClassroomID(Grade grade) {
        this.classroomIDS.add(grade);
        grade.setClassroom(this);
        return this;
    }

    public Classroom removeClassroomID(Grade grade) {
        this.classroomIDS.remove(grade);
        grade.setClassroom(null);
        return this;
    }

    public Set<Student> getStudentIDS() {
        return this.studentIDS;
    }

    public void setStudentIDS(Set<Student> students) {
        this.studentIDS = students;
    }

    public Classroom studentIDS(Set<Student> students) {
        this.setStudentIDS(students);
        return this;
    }

    public Classroom addStudentID(Student student) {
        this.studentIDS.add(student);
        student.getClasroomIDS().add(this);
        return this;
    }

    public Classroom removeStudentID(Student student) {
        this.studentIDS.remove(student);
        student.getClasroomIDS().remove(this);
        return this;
    }

    public Set<Lecturer> getLecturerIDS() {
        return this.lecturerIDS;
    }

    public void setLecturerIDS(Set<Lecturer> lecturers) {
        if (this.lecturerIDS != null) {
            this.lecturerIDS.forEach(i -> i.removeClasroomID(this));
        }
        if (lecturers != null) {
            lecturers.forEach(i -> i.addClasroomID(this));
        }
        this.lecturerIDS = lecturers;
    }

    public Classroom lecturerIDS(Set<Lecturer> lecturers) {
        this.setLecturerIDS(lecturers);
        return this;
    }

    public Classroom addLecturerID(Lecturer lecturer) {
        this.lecturerIDS.add(lecturer);
        lecturer.getClasroomIDS().add(this);
        return this;
    }

    public Classroom removeLecturerID(Lecturer lecturer) {
        this.lecturerIDS.remove(lecturer);
        lecturer.getClasroomIDS().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classroom)) {
            return false;
        }
        return id != null && id.equals(((Classroom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classroom{" +
            "id=" + getId() +
            ", classroomID=" + getClassroomID() +
            ", year='" + getYear() + "'" +
            ", section='" + getSection() + "'" +
            "}";
    }
}
