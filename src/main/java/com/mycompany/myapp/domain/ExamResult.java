package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExamResult.
 */
@Entity
@Table(name = "exam_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExamResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "exam_result_id", unique = true)
    private Integer examResultID;

    @NotNull
    @Column(name = "final_result", nullable = false)
    private Float finalResult;

    @Column(name = "final_status")
    private String finalStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private Exam examID;

    @ManyToOne
    @JsonIgnoreProperties(value = { "clasroomIDS" }, allowSetters = true)
    private Student studentId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gradeID" }, allowSetters = true)
    private Course courseID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExamResultID() {
        return this.examResultID;
    }

    public ExamResult examResultID(Integer examResultID) {
        this.setExamResultID(examResultID);
        return this;
    }

    public void setExamResultID(Integer examResultID) {
        this.examResultID = examResultID;
    }

    public Float getFinalResult() {
        return this.finalResult;
    }

    public ExamResult finalResult(Float finalResult) {
        this.setFinalResult(finalResult);
        return this;
    }

    public void setFinalResult(Float finalResult) {
        this.finalResult = finalResult;
    }

    public String getFinalStatus() {
        return this.finalStatus;
    }

    public ExamResult finalStatus(String finalStatus) {
        this.setFinalStatus(finalStatus);
        return this;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Exam getExamID() {
        return this.examID;
    }

    public void setExamID(Exam exam) {
        this.examID = exam;
    }

    public ExamResult examID(Exam exam) {
        this.setExamID(exam);
        return this;
    }

    public Student getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Student student) {
        this.studentId = student;
    }

    public ExamResult studentId(Student student) {
        this.setStudentId(student);
        return this;
    }

    public Course getCourseID() {
        return this.courseID;
    }

    public void setCourseID(Course course) {
        this.courseID = course;
    }

    public ExamResult courseID(Course course) {
        this.setCourseID(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamResult)) {
            return false;
        }
        return id != null && id.equals(((ExamResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamResult{" +
            "id=" + getId() +
            ", examResultID=" + getExamResultID() +
            ", finalResult=" + getFinalResult() +
            ", finalStatus='" + getFinalStatus() + "'" +
            "}";
    }
}
