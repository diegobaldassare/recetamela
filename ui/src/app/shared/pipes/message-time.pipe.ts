import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'messageTime'
})
export class MessageTimePipe implements PipeTransform{

  value:Date;
  timer:string;

  transform(value: any, args?: any): any {
    value = new Date(value);
    if(value instanceof Date) {
      this.value = value;

      if(!this.timer){
        this.timer = this.getObservable();
      }

      return this.timer;
    }

    return value;
  }

  private getObservable() {
    var result:string;
    // current time
    let now = new Date().getTime();

    // time since message was sent in seconds
    let delta = (now - this.value.getTime()) / 1000;

    // format string
    if(delta < 0){
      result = 'FINISHED'
    }
    else if (delta < 10)
    {
      result = 'Hace instantes';
    }
    else if (delta < 60)
    { // sent in last minute
      result = 'Hace ' + Math.floor(delta) + ' segundos';
    }
    else if (delta < 120)
    { // sent in last hour (first minute)
      result = 'Hace ' + Math.floor(delta / 60) + ' minuto';
    }
    else if (delta < 3600)
    { // sent in last hour
      result = 'Hace ' + Math.floor(delta / 60) + ' minutos';
    }
    else if (delta < 7200)
    { // sent on last day (first hour)
      result = 'Hace ' + Math.floor(delta / 3600) + ' hora';
    }
    else if (delta < 86400)
    { // sent on last day
      result = 'Hace ' + Math.floor(delta / 3600) + ' horas';
    }
    else if (delta < 172800)
    { // sent more than one day ago (first day)
      result = 'Hace ' + Math.floor(delta / 86400) + ' día';
    }
    else
    { // sent more than one day ago
      result = 'Hace ' + Math.floor(delta / 86400) + ' días';
    }
    return result;
  }

}
