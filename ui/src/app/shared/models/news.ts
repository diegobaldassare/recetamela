import {Media} from "./media";
import {User} from "./user-model";

export class News {
  id: string;
  title: string = '';
  description: string = '';
  videoUrl: string = '';
  image: Media;
  author: User;
}
