import { IClassroom } from 'app/shared/model/classroom.model';

export interface IGrade {
  id?: number;
  gradeID?: number | null;
  name?: string;
  description?: string | null;
  classroom?: IClassroom | null;
}

export const defaultValue: Readonly<IGrade> = {};
