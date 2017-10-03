import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {Payment} from "../models/payment/payment";
import {Injectable} from "@angular/core";

@Injectable()
export class PaymentService extends ApiService {

  private URL: string = `${ApiService.API_URL}/payment`;

  constructor(private http: HttpClient){
    super();
  }

  create(payment: Payment, creditCardId: string) : Promise<Payment> {
    return this.http.post<Payment>(`${this.URL}/create/${creditCardId}`, payment).toPromise();
  }

  update(id: string, payment: Payment) : Promise<Payment> {
    return this.http.put<Payment>(`${this.URL}/${id}/update`, payment).toPromise();
  }

  get(id: string) : Promise<Payment> {
    return this.http.get<Payment>(`${this.URL}/${id}`).toPromise();
  }

  getUserPayments() : Promise<Payment[]> {
    return this.http.get<Payment[]>(`${this.URL}/all/`, {}).toPromise();
  }

  delete(id: string) : Promise<any> {
    return this.http.delete(`${this.URL}/${id}/delete`).toPromise();
  }
}
