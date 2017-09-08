export class UserToken {
  public static from(jsonObject: any): UserToken {
    if (!jsonObject
      || !jsonObject.token
      || !jsonObject.date
      || !jsonObject.userId
      || !jsonObject.valid) {
      throw new Error('Failed to instantiate UserToken from given jsonObject');
    }
    return new UserToken(
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

  public get _id(): string {
    return this.userId;
  }

  public get _valid(): boolean {
    return this.valid;
  }

}
