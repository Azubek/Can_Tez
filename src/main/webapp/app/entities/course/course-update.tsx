import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGrade } from 'app/shared/model/grade.model';
import { getEntities as getGrades } from 'app/entities/grade/grade.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntity, updateEntity, createEntity, reset } from './course.reducer';

export const CourseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const grades = useAppSelector(state => state.grade.entities);
  const courseEntity = useAppSelector(state => state.course.entity);
  const loading = useAppSelector(state => state.course.loading);
  const updating = useAppSelector(state => state.course.updating);
  const updateSuccess = useAppSelector(state => state.course.updateSuccess);
  const handleClose = () => {
    props.history.push('/course');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGrades({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...courseEntity,
      ...values,
      gradeID: grades.find(it => it.id.toString() === values.gradeID.toString()),
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
          ...courseEntity,
          gradeID: courseEntity?.gradeID?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tezApp.course.home.createOrEditLabel" data-cy="CourseCreateUpdateHeading">
            Create or edit a Course
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="course-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Course ID"
                id="course-courseID"
                name="courseID"
                data-cy="courseID"
                type="text"
                validate={{
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Name"
                id="course-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="course-description" name="description" data-cy="description" type="text" />
              <ValidatedField id="course-gradeID" name="gradeID" data-cy="gradeID" label="Grade ID" type="select">
                <option value="" key="0" />
                {grades
                  ? grades.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/course" replace color="info">
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

export default CourseUpdate;
