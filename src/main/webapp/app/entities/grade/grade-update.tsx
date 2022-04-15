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
import { IGrade } from 'app/shared/model/grade.model';
import { getEntity, updateEntity, createEntity, reset } from './grade.reducer';

export const GradeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const classrooms = useAppSelector(state => state.classroom.entities);
  const gradeEntity = useAppSelector(state => state.grade.entity);
  const loading = useAppSelector(state => state.grade.loading);
  const updating = useAppSelector(state => state.grade.updating);
  const updateSuccess = useAppSelector(state => state.grade.updateSuccess);
  const handleClose = () => {
    props.history.push('/grade');
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
      ...gradeEntity,
      ...values,
      classroom: classrooms.find(it => it.id.toString() === values.classroom.toString()),
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
          ...gradeEntity,
          classroom: gradeEntity?.classroom?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tezApp.grade.home.createOrEditLabel" data-cy="GradeCreateUpdateHeading">
            Create or edit a Grade
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="grade-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Grade ID"
                id="grade-gradeID"
                name="gradeID"
                data-cy="gradeID"
                type="text"
                validate={{
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Name"
                id="grade-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="grade-description" name="description" data-cy="description" type="text" />
              <ValidatedField id="grade-classroom" name="classroom" data-cy="classroom" label="Classroom" type="select">
                <option value="" key="0" />
                {classrooms
                  ? classrooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/grade" replace color="info">
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

export default GradeUpdate;
