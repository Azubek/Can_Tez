import { IGrade } from 'app/shared/model/grade.model';

export interface ICourse {
  id?: number;
  courseID?: number | null;
  name?: string;
  description?: string | null;
  gradeID?: IGrade | null;
}

export const defaultValue: Readonly<ICourse> = {};
