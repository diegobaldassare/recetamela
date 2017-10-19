import {Media} from "./media";
import {User} from "./user-model";

export class ChefRequest {
  id: string;
  answered: boolean;
  accepted: boolean;
  user: User;
  certificate: Media;
  resume: string;
}
