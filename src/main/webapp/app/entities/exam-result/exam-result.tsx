import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExamResult } from 'app/shared/model/exam-result.model';
import { getEntities } from './exam-result.reducer';

export const ExamResult = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const examResultList = useAppSelector(state => state.examResult.entities);
  const loading = useAppSelector(state => state.examResult.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="exam-result-heading" data-cy="ExamResultHeading">
        Exam Results
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/exam-result/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Exam Result
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {examResultList && examResultList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Exam Result ID</th>
                <th>Final Result</th>
                <th>Final Status</th>
                <th>Exam ID</th>
                <th>Student Id</th>
                <th>Course ID</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {examResultList.map((examResult, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/exam-result/${examResult.id}`} color="link" size="sm">
                      {examResult.id}
                    </Button>
                  </td>
                  <td>{examResult.examResultID}</td>
                  <td>{examResult.finalResult}</td>
                  <td>{examResult.finalStatus}</td>
                  <td>{examResult.examID ? <Link to={`/exam/${examResult.examID.id}`}>{examResult.examID.id}</Link> : ''}</td>
                  <td>{examResult.studentId ? <Link to={`/student/${examResult.studentId.id}`}>{examResult.studentId.id}</Link> : ''}</td>
                  <td>{examResult.courseID ? <Link to={`/course/${examResult.courseID.id}`}>{examResult.courseID.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/exam-result/${examResult.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/exam-result/${examResult.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/exam-result/${examResult.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Exam Results found</div>
        )}
      </div>
    </div>
  );
};

export default ExamResult;
