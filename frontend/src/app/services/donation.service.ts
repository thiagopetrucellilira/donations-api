import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Donation, DonationRequest, DonationFilters, FoodCategory, FOOD_CATEGORY_LABELS, PageResponse } from '../models/donation.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DonationService {
  private readonly API_URL = `${environment.apiUrl}/donations`;

  constructor(private http: HttpClient) {}

  getAllDonations(filters?: DonationFilters): Observable<PageResponse<Donation>> {
    let params = new HttpParams();
    
    if (filters) {
      if (filters.category) params = params.set('category', filters.category);
      if (filters.city) params = params.set('city', filters.city);
      if (filters.state) params = params.set('state', filters.state);
      if (filters.status) params = params.set('status', filters.status);
      if (filters.search) params = params.set('search', filters.search);
      if (filters.page !== undefined) params = params.set('page', filters.page.toString());
      if (filters.size !== undefined) params = params.set('size', filters.size.toString());
      if (filters.sortBy) params = params.set('sortBy', filters.sortBy);
      if (filters.sortDir) params = params.set('sortDir', filters.sortDir);
    }

    return this.http.get<PageResponse<Donation>>(this.API_URL, { params });
  }

  getDonationById(id: number): Observable<Donation> {
    return this.http.get<Donation>(`${this.API_URL}/${id}`);
  }

  createDonation(donation: DonationRequest): Observable<Donation> {
    return this.http.post<Donation>(this.API_URL, donation);
  }

  updateDonation(id: number, donation: Partial<DonationRequest>): Observable<Donation> {
    return this.http.put<Donation>(`${this.API_URL}/${id}`, donation);
  }

  deleteDonation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  getUserDonations(): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.API_URL}/my`);
  }

  getCategories(): { value: FoodCategory; label: string }[] {
    return Object.values(FoodCategory).map(value => ({
      value,
      label: FOOD_CATEGORY_LABELS[value]
    }));
  }
}
