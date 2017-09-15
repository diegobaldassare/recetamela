export class AuthToken {

  public static from(jsonObject: any): AuthToken {
    if (!jsonObject
      || !jsonObject.token
      || !jsonObject.date
      || !jsonObject.userId
      || !jsonObject.valid) {
      throw new Error('Failed to instantiate User from given jsonObject');
    }
    return new AuthToken(
      jsonObject.token,
      jsonObject.date,
      jsonObject.userId,
      jsonObject.valid);
  }

  constructor(public token: string,
              public date: string,
              public userId: string,
              public valid: boolean) {
  }


  public get _token(): string {
    return this.token;
  }

  public get _date(): string {
    return this.date;
  }

  public get _userId(): string {
    return this.userId;
  }

  public get _valid(): boolean {
    return this.valid;
  }
}
