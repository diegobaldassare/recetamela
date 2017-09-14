import {RecipeStep} from "./recipe-step";

export class RecipeInput {
  name: string = '';
  description :string = '';
  videoUrl: string = '';
  difficulty: string = '3';
  duration: string = '';
  serves: string = '';
  imageIds: string[] = [];
  steps: RecipeStep[] = [];
  ingredientNames: string[] = [];
  categoryNames: string[] = [];
}
