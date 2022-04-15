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
import { ILecturer } from 'app/shared/model/lecturer.model';
import { getEntity, updateEntity, createEntity, reset } from './lecturer.reducer';

export const LecturerUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const classrooms = useAppSelector(state => state.classroom.entities);
  const lecturerEntity = useAppSelector(state => state.lecturer.entity);
  const loading = useAppSelector(state => state.lecturer.loading);
  const updating = useAppSelector(state => state.lecturer.updating);
  const updateSuccess = useAppSelector(state => state.lecturer.updateSuccess);
  const handleClose = () => {
    props.history.push('/lecturer');
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
      ...lecturerEntity,
      ...values,
      clasroomIDS: mapIdList(values.clasroomIDS),
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
          ...lecturerEntity,
          clasroomIDS: lecturerEntity?.clasroomIDS?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tezApp.lecturer.home.createOrEditLabel" data-cy="LecturerCreateUpdateHeading">
            Create or edit a Lecturer
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="lecturer-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Lecturer ID"
                id="lecturer-lecturerID"
                name="lecturerID"
                data-cy="lecturerID"
                type="text"
                validate={{
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Email"
                id="lecturer-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Password"
                id="lecturer-password"
                name="password"
                data-cy="password"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Name"
                id="lecturer-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Surname"
                id="lecturer-surname"
                name="surname"
                data-cy="surname"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Date Of Birth"
                id="lecturer-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Mobile" id="lecturer-mobile" name="mobile" data-cy="mobile" type="text" />
              <ValidatedField label="Clasroom ID" id="lecturer-clasroomID" data-cy="clasroomID" type="select" multiple name="clasroomIDS">
                <option value="" key="0" />
                {classrooms
                  ? classrooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lecturer" replace color="info">
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

export default LecturerUpdate;
