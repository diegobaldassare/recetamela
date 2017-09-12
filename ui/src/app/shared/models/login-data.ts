//Poor choice of name, but this Model is later mapped to LoginData on backend.
export class LoginData {
  public static from(jsonObject: any): LoginData {
    if (!jsonObject
      || !jsonObject.accessToken
      || !jsonObject.email
      || !jsonObject.name
      || !jsonObject.gender
      || !jsonObject.id
      || !jsonObject.url) {
      throw new Error('Failed to instantiate LoginData from given jsonObject');
    }
    return new LoginData(
      jsonObject.accessToken,
      jsonObject.email,
      jsonObject.name,
      jsonObject.gender,
      jsonObject.id,
      jsonObject.url);
  }

  constructor(public accessToken: string,
              public email: string,
              public name: string,
              public gender: string,
              public id: string,
              public url: string) {
  }

  public get _accessToken(): string {
    return this.accessToken;
  }

  public get _email(): string {
    return this.email;
  }

  public get _name(): string {
    return this.name;
  }

  public get _gender(): string {
    return this.gender;
  }

  public get _id(): string {
    return this.id;
  }

  public get _url(): string {
    return this.url;
  }

}
