import dayjs from 'dayjs';
import { IGrade } from 'app/shared/model/grade.model';
import { IStudent } from 'app/shared/model/student.model';
import { ILecturer } from 'app/shared/model/lecturer.model';

export interface IClassroom {
  id?: number;
  classroomID?: number | null;
  year?: string;
  section?: boolean;
  classroomIDS?: IGrade[] | null;
  studentIDS?: IStudent[] | null;
  lecturerIDS?: ILecturer[] | null;
}

export const defaultValue: Readonly<IClassroom> = {
  section: false,
};
