import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lecturer from './lecturer';
import LecturerDetail from './lecturer-detail';
import LecturerUpdate from './lecturer-update';
import LecturerDeleteDialog from './lecturer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LecturerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LecturerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LecturerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lecturer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LecturerDeleteDialog} />
  </>
);

export default Routes;
