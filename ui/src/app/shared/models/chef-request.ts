import {Media} from "./media";

export class ChefRequest {
  id: string;
  answered: boolean = false;
  media: Media;
  resume: string;
}
