import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './exam.reducer';

export const ExamDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const examEntity = useAppSelector(state => state.exam.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="examDetailsHeading">Exam</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{examEntity.id}</dd>
          <dt>
            <span id="examID">Exam ID</span>
          </dt>
          <dd>{examEntity.examID}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{examEntity.name}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{examEntity.date ? <TextFormat value={examEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="examType">Exam Type</span>
          </dt>
          <dd>{examEntity.examType}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{examEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/exam" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam/${examEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamDetail;
