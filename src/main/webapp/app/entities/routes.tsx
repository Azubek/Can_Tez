import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Student from './student';
import Course from './course';
import Lecturer from './lecturer';
import Classroom from './classroom';
import Grade from './grade';
import Exam from './exam';
import ExamResult from './exam-result';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}student`} component={Student} />
        <ErrorBoundaryRoute path={`${match.url}course`} component={Course} />
        <ErrorBoundaryRoute path={`${match.url}lecturer`} component={Lecturer} />
        <ErrorBoundaryRoute path={`${match.url}classroom`} component={Classroom} />
        <ErrorBoundaryRoute path={`${match.url}grade`} component={Grade} />
        <ErrorBoundaryRoute path={`${match.url}exam`} component={Exam} />
        <ErrorBoundaryRoute path={`${match.url}exam-result`} component={ExamResult} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
