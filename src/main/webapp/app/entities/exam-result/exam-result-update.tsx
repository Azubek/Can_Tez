import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExam } from 'app/shared/model/exam.model';
import { getEntities as getExams } from 'app/entities/exam/exam.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { IExamResult } from 'app/shared/model/exam-result.model';
import { getEntity, updateEntity, createEntity, reset } from './exam-result.reducer';

export const ExamResultUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const exams = useAppSelector(state => state.exam.entities);
  const students = useAppSelector(state => state.student.entities);
  const courses = useAppSelector(state => state.course.entities);
  const examResultEntity = useAppSelector(state => state.examResult.entity);
  const loading = useAppSelector(state => state.examResult.loading);
  const updating = useAppSelector(state => state.examResult.updating);
  const updateSuccess = useAppSelector(state => state.examResult.updateSuccess);
  const handleClose = () => {
    props.history.push('/exam-result');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExams({}));
    dispatch(getStudents({}));
    dispatch(getCourses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...examResultEntity,
      ...values,
      examID: exams.find(it => it.id.toString() === values.examID.toString()),
      studentId: students.find(it => it.id.toString() === values.studentId.toString()),
      courseID: courses.find(it => it.id.toString() === values.courseID.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...examResultEntity,
          examID: examResultEntity?.examID?.id,
          studentId: examResultEntity?.studentId?.id,
          courseID: examResultEntity?.courseID?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tezApp.examResult.home.createOrEditLabel" data-cy="ExamResultCreateUpdateHeading">
            Create or edit a ExamResult
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="exam-result-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Exam Result ID"
                id="exam-result-examResultID"
                name="examResultID"
                data-cy="examResultID"
                type="text"
                validate={{
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Final Result"
                id="exam-result-finalResult"
                name="finalResult"
                data-cy="finalResult"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Final Status" id="exam-result-finalStatus" name="finalStatus" data-cy="finalStatus" type="text" />
              <ValidatedField id="exam-result-examID" name="examID" data-cy="examID" label="Exam ID" type="select">
                <option value="" key="0" />
                {exams
                  ? exams.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="exam-result-studentId" name="studentId" data-cy="studentId" label="Student Id" type="select">
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="exam-result-courseID" name="courseID" data-cy="courseID" label="Course ID" type="select">
                <option value="" key="0" />
                {courses
                  ? courses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exam-result" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExamResultUpdate;
