import {Injectable} from "@angular/core";

@Injectable()
export class FormatService {

  public isAlphaNumSpaceNotEmpty(s: string): boolean {
    return /^[A-Za-zñ\d\s]+$/.test(s);
  }
}
