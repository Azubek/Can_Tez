import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lecturer.reducer';

export const LecturerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const lecturerEntity = useAppSelector(state => state.lecturer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lecturerDetailsHeading">Lecturer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{lecturerEntity.id}</dd>
          <dt>
            <span id="lecturerID">Lecturer ID</span>
          </dt>
          <dd>{lecturerEntity.lecturerID}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{lecturerEntity.email}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{lecturerEntity.password}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{lecturerEntity.name}</dd>
          <dt>
            <span id="surname">Surname</span>
          </dt>
          <dd>{lecturerEntity.surname}</dd>
          <dt>
            <span id="dateOfBirth">Date Of Birth</span>
          </dt>
          <dd>
            {lecturerEntity.dateOfBirth ? (
              <TextFormat value={lecturerEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="mobile">Mobile</span>
          </dt>
          <dd>{lecturerEntity.mobile}</dd>
          <dt>Clasroom ID</dt>
          <dd>
            {lecturerEntity.clasroomIDS
              ? lecturerEntity.clasroomIDS.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {lecturerEntity.clasroomIDS && i === lecturerEntity.clasroomIDS.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/lecturer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lecturer/${lecturerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LecturerDetail;
