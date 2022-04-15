import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { ILecturer } from 'app/shared/model/lecturer.model';
import { getEntities as getLecturers } from 'app/entities/lecturer/lecturer.reducer';
import { IClassroom } from 'app/shared/model/classroom.model';
import { getEntity, updateEntity, createEntity, reset } from './classroom.reducer';

export const ClassroomUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const students = useAppSelector(state => state.student.entities);
  const lecturers = useAppSelector(state => state.lecturer.entities);
  const classroomEntity = useAppSelector(state => state.classroom.entity);
  const loading = useAppSelector(state => state.classroom.loading);
  const updating = useAppSelector(state => state.classroom.updating);
  const updateSuccess = useAppSelector(state => state.classroom.updateSuccess);
  const handleClose = () => {
    props.history.push('/classroom');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getStudents({}));
    dispatch(getLecturers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...classroomEntity,
      ...values,
      studentIDS: mapIdList(values.studentIDS),
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
          ...classroomEntity,
          studentIDS: classroomEntity?.studentIDS?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tezApp.classroom.home.createOrEditLabel" data-cy="ClassroomCreateUpdateHeading">
            Create or edit a Classroom
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="classroom-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Classroom ID"
                id="classroom-classroomID"
                name="classroomID"
                data-cy="classroomID"
                type="text"
                validate={{
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Year"
                id="classroom-year"
                name="year"
                data-cy="year"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Section" id="classroom-section" name="section" data-cy="section" check type="checkbox" />
              <ValidatedField label="Student ID" id="classroom-studentID" data-cy="studentID" type="select" multiple name="studentIDS">
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/classroom" replace color="info">
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

export default ClassroomUpdate;
