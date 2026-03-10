import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { DonationService } from '../../services/donation.service';
import { MatchService } from '../../services/match.service';
import { AuthService } from '../../services/auth.service';
import { Donation, DonationFilters, DonationStatus, FoodCategory, FOOD_CATEGORY_LABELS } from '../../models/donation.model';
import { MatchRequest } from '../../models/match.model';

@Component({
  selector: 'app-donations',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatChipsModule,
    MatPaginatorModule
  ],
  templateUrl: './donations.html',
  styleUrl: './donations.scss'
})
export class Donations implements OnInit {
  donations: Donation[] = [];
  filterForm!: FormGroup;
  loading = false;
  
  // Pagination
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  
  categories: { value: FoodCategory; label: string }[] = [];
  states = [
    'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA',
    'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN',
    'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
  ];

  constructor(
    private fb: FormBuilder,
    private donationService: DonationService,
    private matchService: MatchService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.categories = this.donationService.getCategories();
    
    this.filterForm = this.fb.group({
      search: [''],
      category: [''],
      city: [''],
      state: ['']
    });

    this.loadDonations();
    
    // Listen to form changes for real-time filtering
    this.filterForm.valueChanges.subscribe(() => {
      this.currentPage = 0;
      this.loadDonations();
    });
  }

  loadDonations() {
    this.loading = true;
    
    const filters: DonationFilters = {
      ...this.filterForm.value,
      status: DonationStatus.AVAILABLE,
      page: this.currentPage,
      size: this.pageSize,
      sortBy: 'createdAt',
      sortDir: 'desc'
    };

    // Remove empty filters
    Object.keys(filters).forEach(key => {
      if (!filters[key as keyof DonationFilters]) {
        delete filters[key as keyof DonationFilters];
      }
    });

    this.donationService.getAllDonations(filters).subscribe({
      next: (response) => {
        this.donations = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading donations:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadDonations();
  }

  requestDonation(donation: Donation) {
    if (!this.authService.isLoggedIn()) {
      // Save current URL to return here after login
      this.authService.setReturnUrl('/donations');
      this.router.navigate(['/login']);
      return;
    }

    const request: MatchRequest = {
      message: `Olá! Tenho interesse em receber a doação: ${donation.title}`
    };

    this.matchService.requestDonation(donation.id!, request).subscribe({
      next: (response) => {
        alert('Solicitação enviada com sucesso! O doador será notificado.');
      },
      error: (error) => {
        console.error('Error requesting donation:', error);
        alert('Erro ao solicitar doação. Tente novamente.');
      }
    });
  }

  clearFilters() {
    this.filterForm.reset();
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  getCategoryLabel(category?: FoodCategory): string {
    if (!category) return '';
    return FOOD_CATEGORY_LABELS[category] || category;
  }

  getStatusColor(status?: DonationStatus): string {
    switch (status) {
      case DonationStatus.AVAILABLE: return 'primary';
      case DonationStatus.RESERVED: return 'warn';
      case DonationStatus.PENDING: return 'warn';
      case DonationStatus.COMPLETED: return 'accent';
      case DonationStatus.EXPIRED: return '';
      case DonationStatus.CANCELLED: return '';
      default: return '';
    }
  }

  getStatusText(status?: DonationStatus): string {
    switch (status) {
      case DonationStatus.AVAILABLE: return 'Disponível';
      case DonationStatus.RESERVED: return 'Em andamento';
      case DonationStatus.PENDING: return 'Pendente';
      case DonationStatus.COMPLETED: return 'Concluída';
      case DonationStatus.EXPIRED: return 'Expirada';
      case DonationStatus.CANCELLED: return 'Cancelada';
      default: return 'Desconhecido';
    }
  }
}
