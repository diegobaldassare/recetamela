export class RecipeInput {
  name; description; steps; videoUrl; difficulty; imageId: String;
  ingredientNames; categoryNames: String[];

  constructor() {
    this.difficulty = "3";
    this.ingredientNames = [];
    this.categoryNames = [];
  }
}
