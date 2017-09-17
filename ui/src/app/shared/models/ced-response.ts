import {User} from "./user-model";

export class CheckExpirationDateResponse {
  user: User;
  expired: boolean;
}
