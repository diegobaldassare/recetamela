import {User} from "../user-model";

export class RecipeCommentary {
  id: string;
  text: string;
  author: User;
  date: Date;
}
