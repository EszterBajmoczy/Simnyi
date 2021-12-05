import {CommentDto} from './commentDto';

export class Caff {
  id!: string;
  name!: string;
  comment!: CommentDto[];
  content?: string;
  contentLength?: number;
  mimeType?: string;
}
