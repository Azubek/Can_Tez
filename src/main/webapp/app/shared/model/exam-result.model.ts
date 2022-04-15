import { IExam } from 'app/shared/model/exam.model';
import { IStudent } from 'app/shared/model/student.model';
import { ICourse } from 'app/shared/model/course.model';

export interface IExamResult {
  id?: number;
  examResultID?: number | null;
  finalResult?: number;
  finalStatus?: string | null;
  examID?: IExam | null;
  studentId?: IStudent | null;
  courseID?: ICourse | null;
}

export const defaultValue: Readonly<IExamResult> = {};
