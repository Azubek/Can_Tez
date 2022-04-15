import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './exam-result.reducer';

export const ExamResultDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const examResultEntity = useAppSelector(state => state.examResult.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="examResultDetailsHeading">ExamResult</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{examResultEntity.id}</dd>
          <dt>
            <span id="examResultID">Exam Result ID</span>
          </dt>
          <dd>{examResultEntity.examResultID}</dd>
          <dt>
            <span id="finalResult">Final Result</span>
          </dt>
          <dd>{examResultEntity.finalResult}</dd>
          <dt>
            <span id="finalStatus">Final Status</span>
          </dt>
          <dd>{examResultEntity.finalStatus}</dd>
          <dt>Exam ID</dt>
          <dd>{examResultEntity.examID ? examResultEntity.examID.id : ''}</dd>
          <dt>Student Id</dt>
          <dd>{examResultEntity.studentId ? examResultEntity.studentId.id : ''}</dd>
          <dt>Course ID</dt>
          <dd>{examResultEntity.courseID ? examResultEntity.courseID.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/exam-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam-result/${examResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamResultDetail;
