import {User} from "../user-model";

export class RecipeCommentary {
  id: string;
  comment: string;
  author: User;
  date: Date;
}
