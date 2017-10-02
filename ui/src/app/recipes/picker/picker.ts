import {Input} from "@angular/core";

export abstract class Picker {
  @Input() protected label;
  @Input() protected picked;
  protected unpicked = {};
  protected name: string = '';

  protected abstract pick();
  protected abstract unpick(e);
}
