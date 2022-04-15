import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grade.reducer';

export const GradeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const gradeEntity = useAppSelector(state => state.grade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gradeDetailsHeading">Grade</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{gradeEntity.id}</dd>
          <dt>
            <span id="gradeID">Grade ID</span>
          </dt>
          <dd>{gradeEntity.gradeID}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{gradeEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{gradeEntity.description}</dd>
          <dt>Classroom</dt>
          <dd>{gradeEntity.classroom ? gradeEntity.classroom.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/grade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grade/${gradeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GradeDetail;
