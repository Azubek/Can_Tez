import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClassroom } from 'app/shared/model/classroom.model';
import { getEntities as getClassrooms } from 'app/entities/classroom/classroom.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntity, updateEntity, createEntity, reset } from './student.reducer';

export const StudentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const classrooms = useAppSelector(state => state.classroom.entities);
  const studentEntity = useAppSelector(state => state.student.entity);
  const loading = useAppSelector(state => state.student.loading);
  const updating = useAppSelector(state => state.student.updating);
  const updateSuccess = useAppSelector(state => state.student.updateSuccess);
  const handleClose = () => {
    props.history.push('/student');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getClassrooms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...studentEntity,
      ...values,
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
          ...studentEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tezApp.student.home.createOrEditLabel" data-cy="StudentCreateUpdateHeading">
            Create or edit a Student
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="student-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Student ID"
                id="student-studentID"
                name="studentID"
                data-cy="studentID"
                type="text"
                validate={{
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Student No"
                id="student-studentNo"
                name="studentNo"
                data-cy="studentNo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Email"
                id="student-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Password"
                id="student-password"
                name="password"
                data-cy="password"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Name"
                id="student-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Surname"
                id="student-surname"
                name="surname"
                data-cy="surname"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Date Of Birth"
                id="student-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Mobile" id="student-mobile" name="mobile" data-cy="mobile" type="text" />
              <ValidatedField
                label="Date Of Join"
                id="student-dateOfJoin"
                name="dateOfJoin"
                data-cy="dateOfJoin"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Status" id="student-status" name="status" data-cy="status" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student" replace color="info">
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

export default StudentUpdate;
