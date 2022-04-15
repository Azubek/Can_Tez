import dayjs from 'dayjs';
import { IClassroom } from 'app/shared/model/classroom.model';

export interface IStudent {
  id?: number;
  studentID?: number | null;
  studentNo?: string;
  email?: string;
  password?: string;
  name?: string;
  surname?: string;
  dateOfBirth?: string;
  mobile?: string | null;
  dateOfJoin?: string;
  status?: boolean;
  clasroomIDS?: IClassroom[] | null;
}

export const defaultValue: Readonly<IStudent> = {
  status: false,
};
