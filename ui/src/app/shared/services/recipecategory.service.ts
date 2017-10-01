import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";

export class RecipeCategoryService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipe`;

  constructor(private http: HttpClient) {
    super();
  }

  create(category: string) : Promise<string> {
    return this.http.post<string>(`${this.URL}/category/create`, category).toPromise();
  }

  update(categoryId: string, category: string) : Promise<string> {
    return this.http.put<string>(`${this.URL}/category/${categoryId}/update`, category).toPromise();
  }

  get(categoryId: string) : Promise<string> {
    return this.http.get<string>(`${this.URL}/category/${categoryId}`).toPromise();
  }

  getAll() : Promise<string[]> {
    return this.http.get<string[]>(`${this.URL}/categories/all`).toPromise();
  }

  delete(categoryId: string) : Promise<any> {
    return this.http.delete<any>(`${this.URL}/category/${categoryId}/delete`).toPromise();
  }
}
