import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './classroom.reducer';

export const ClassroomDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const classroomEntity = useAppSelector(state => state.classroom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="classroomDetailsHeading">Classroom</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{classroomEntity.id}</dd>
          <dt>
            <span id="classroomID">Classroom ID</span>
          </dt>
          <dd>{classroomEntity.classroomID}</dd>
          <dt>
            <span id="year">Year</span>
          </dt>
          <dd>{classroomEntity.year ? <TextFormat value={classroomEntity.year} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="section">Section</span>
          </dt>
          <dd>{classroomEntity.section ? 'true' : 'false'}</dd>
          <dt>Student ID</dt>
          <dd>
            {classroomEntity.studentIDS
              ? classroomEntity.studentIDS.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {classroomEntity.studentIDS && i === classroomEntity.studentIDS.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/classroom" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/classroom/${classroomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClassroomDetail;
