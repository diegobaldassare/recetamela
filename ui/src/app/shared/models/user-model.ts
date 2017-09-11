export class User {

  public static from(jsonObject: any): User {
    if (!jsonObject
      || !jsonObject.id
      || !jsonObject.name
      || !jsonObject.lastName
      || !jsonObject.email
      || !jsonObject._birthDate) {
      throw new Error('Failed to instantiate User from given jsonObject');
    }
    return new User(
      jsonObject.id,
      jsonObject.name,
      jsonObject.lastName,
      jsonObject.email,
      jsonObject._birthDate);
  }

  constructor(public id: string,
              public name: string,
              public lastName: string,
              public email: string,
              public birthDate: Date) {
  }


  public get _id(): string {
    return this.id;
  }

  public get _name(): string {
    return this.name;
  }

  public get _lastName(): string {
    return this.lastName;
  }

  public get _email(): string {
    return this.email;
  }

  public get _birthDate(): Date {
    return this.birthDate;
  }
}
