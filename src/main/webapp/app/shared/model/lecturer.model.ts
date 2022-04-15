import dayjs from 'dayjs';
import { IClassroom } from 'app/shared/model/classroom.model';

export interface ILecturer {
  id?: number;
  lecturerID?: number | null;
  email?: string;
  password?: string;
  name?: string;
  surname?: string;
  dateOfBirth?: string;
  mobile?: string | null;
  clasroomIDS?: IClassroom[] | null;
}

export const defaultValue: Readonly<ILecturer> = {};
