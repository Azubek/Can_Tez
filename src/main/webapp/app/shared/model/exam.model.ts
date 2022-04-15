import dayjs from 'dayjs';

export interface IExam {
  id?: number;
  examID?: number | null;
  name?: string;
  date?: string;
  examType?: number;
  description?: string | null;
}

export const defaultValue: Readonly<IExam> = {};
