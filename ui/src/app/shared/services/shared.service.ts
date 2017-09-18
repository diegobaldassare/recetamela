import {Injectable, NgZone} from '@angular/core';
import {Subject} from "rxjs/Subject";

@Injectable()
export class SharedService {

  private notify = new Subject<any>();
  notifyObservable$ = this.notify.asObservable();

  constructor(private zone: NgZone) { }

  public notifyOther(data: any) {
    this.zone.run(() => {
      if(data){
        this.notify.next(data);
      }
    });
  }

}
