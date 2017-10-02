import {Injectable} from "@angular/core";

@Injectable()
export class FormatService {

  public isAlphaNumSpaceNotEmpty(s: string): boolean {
    return /^[A-Za-zñ\d\s]+$/.test(s);
  }

  public capitalizeFirstChar(s: string): string {
    if (s.length == 0) return s;
    if (s.length == 1) return s.charAt(0).toUpperCase();
    return s.charAt(0).toUpperCase() + s.slice(1);
  }
}
