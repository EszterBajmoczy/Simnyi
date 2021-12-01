import {Comment} from './comment';

export class Caff {
  id!: string;
  name!: string;
  comment!: Comment[];
  contentId?: string;
  contentLength?: number;
  mimeType?: string;
}
