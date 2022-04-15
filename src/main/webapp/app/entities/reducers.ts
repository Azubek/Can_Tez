import student from 'app/entities/student/student.reducer';
import course from 'app/entities/course/course.reducer';
import lecturer from 'app/entities/lecturer/lecturer.reducer';
import classroom from 'app/entities/classroom/classroom.reducer';
import grade from 'app/entities/grade/grade.reducer';
import exam from 'app/entities/exam/exam.reducer';
import examResult from 'app/entities/exam-result/exam-result.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  student,
  course,
  lecturer,
  classroom,
  grade,
  exam,
  examResult,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
