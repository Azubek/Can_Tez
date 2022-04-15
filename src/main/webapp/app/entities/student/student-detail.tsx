import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

export const StudentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">Student</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="studentID">Student ID</span>
          </dt>
          <dd>{studentEntity.studentID}</dd>
          <dt>
            <span id="studentNo">Student No</span>
          </dt>
          <dd>{studentEntity.studentNo}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{studentEntity.email}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{studentEntity.password}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{studentEntity.name}</dd>
          <dt>
            <span id="surname">Surname</span>
          </dt>
          <dd>{studentEntity.surname}</dd>
          <dt>
            <span id="dateOfBirth">Date Of Birth</span>
          </dt>
          <dd>
            {studentEntity.dateOfBirth ? <TextFormat value={studentEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="mobile">Mobile</span>
          </dt>
          <dd>{studentEntity.mobile}</dd>
          <dt>
            <span id="dateOfJoin">Date Of Join</span>
          </dt>
          <dd>
            {studentEntity.dateOfJoin ? <TextFormat value={studentEntity.dateOfJoin} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{studentEntity.status ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
