export class RecipeInput {
  name: string = '';
  description :string = '';
  videoUrl: string = '';
  difficulty: string = '3';
  imageId: string = '';
  steps: string[] = [];
  ingredientNames: string[] = [];
  categoryNames: string[] = [];
}
