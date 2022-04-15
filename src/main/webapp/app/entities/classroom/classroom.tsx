import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClassroom } from 'app/shared/model/classroom.model';
import { getEntities } from './classroom.reducer';

export const Classroom = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const classroomList = useAppSelector(state => state.classroom.entities);
  const loading = useAppSelector(state => state.classroom.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="classroom-heading" data-cy="ClassroomHeading">
        Classrooms
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/classroom/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Classroom
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {classroomList && classroomList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Classroom ID</th>
                <th>Year</th>
                <th>Section</th>
                <th>Student ID</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {classroomList.map((classroom, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/classroom/${classroom.id}`} color="link" size="sm">
                      {classroom.id}
                    </Button>
                  </td>
                  <td>{classroom.classroomID}</td>
                  <td>{classroom.year ? <TextFormat type="date" value={classroom.year} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{classroom.section ? 'true' : 'false'}</td>
                  <td>
                    {classroom.studentIDS
                      ? classroom.studentIDS.map((val, j) => (
                          <span key={j}>
                            <Link to={`/student/${val.id}`}>{val.id}</Link>
                            {j === classroom.studentIDS.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/classroom/${classroom.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/classroom/${classroom.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/classroom/${classroom.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Classrooms found</div>
        )}
      </div>
    </div>
  );
};

export default Classroom;
