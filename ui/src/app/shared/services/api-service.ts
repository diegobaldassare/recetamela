import {environment} from "../../../environments/environment";
export abstract class ApiService {
  public static API_URL: string = environment.production ? "http://recetamela.herokuapp.com/api" : "http://localhost:9000/api";
}
