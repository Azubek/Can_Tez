import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILecturer } from 'app/shared/model/lecturer.model';
import { getEntities } from './lecturer.reducer';

export const Lecturer = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const lecturerList = useAppSelector(state => state.lecturer.entities);
  const loading = useAppSelector(state => state.lecturer.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="lecturer-heading" data-cy="LecturerHeading">
        Lecturers
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/lecturer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Lecturer
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {lecturerList && lecturerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Lecturer ID</th>
                <th>Email</th>
                <th>Password</th>
                <th>Name</th>
                <th>Surname</th>
                <th>Date Of Birth</th>
                <th>Mobile</th>
                <th>Clasroom ID</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lecturerList.map((lecturer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/lecturer/${lecturer.id}`} color="link" size="sm">
                      {lecturer.id}
                    </Button>
                  </td>
                  <td>{lecturer.lecturerID}</td>
                  <td>{lecturer.email}</td>
                  <td>{lecturer.password}</td>
                  <td>{lecturer.name}</td>
                  <td>{lecturer.surname}</td>
                  <td>
                    {lecturer.dateOfBirth ? <TextFormat type="date" value={lecturer.dateOfBirth} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{lecturer.mobile}</td>
                  <td>
                    {lecturer.clasroomIDS
                      ? lecturer.clasroomIDS.map((val, j) => (
                          <span key={j}>
                            <Link to={`/classroom/${val.id}`}>{val.id}</Link>
                            {j === lecturer.clasroomIDS.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/lecturer/${lecturer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/lecturer/${lecturer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/lecturer/${lecturer.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Lecturers found</div>
        )}
      </div>
    </div>
  );
};

export default Lecturer;
