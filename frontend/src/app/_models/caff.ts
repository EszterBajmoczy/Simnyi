import {Comment} from './comment';

export class Caff {
  id!: string;
  name!: string;
  comment!: Comment[];
  content?: string;
  contentLength?: number;
  mimeType?: string;
}
