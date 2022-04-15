import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/student">
        Student
      </MenuItem>
      <MenuItem icon="asterisk" to="/course">
        Course
      </MenuItem>
      <MenuItem icon="asterisk" to="/lecturer">
        Lecturer
      </MenuItem>
      <MenuItem icon="asterisk" to="/classroom">
        Classroom
      </MenuItem>
      <MenuItem icon="asterisk" to="/grade">
        Grade
      </MenuItem>
      <MenuItem icon="asterisk" to="/exam">
        Exam
      </MenuItem>
      <MenuItem icon="asterisk" to="/exam-result">
        Exam Result
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
