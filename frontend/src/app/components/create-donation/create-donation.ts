import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { CommonModule } from '@angular/common';
import { DonationService } from '../../services/donation.service';
import { AuthService } from '../../services/auth.service';
import { DonationRequest, FoodCategory, FOOD_CATEGORY_LABELS } from '../../models/donation.model';

@Component({
  selector: 'app-create-donation',
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
    MatDatepickerModule,
    MatNativeDateModule,
    MatCheckboxModule
  ],
  templateUrl: './create-donation.html',
  styleUrl: './create-donation.scss'
})
export class CreateDonation implements OnInit {
  donationForm!: FormGroup;
  loading = false;
  error = '';
  
  categories: { value: FoodCategory; label: string }[] = [];
  states = [
    'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA',
    'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN',
    'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
  ];

  constructor(
    private fb: FormBuilder,
    private donationService: DonationService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.categories = this.donationService.getCategories();
    
    this.donationForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(200)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      category: [null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      expirationDate: [''],
      perishable: [false],
      storageInstructions: [''],
      location: [''],
      city: [''],
      state: [''],
      zipCode: [''],
      pickupInstructions: [''],
      expiresAt: ['']
    });
  }

  onSubmit() {
    if (this.donationForm.valid) {
      this.loading = true;
      this.error = '';
      
      // Log authentication status
      console.log('User is logged in:', this.authService.isLoggedIn());
      console.log('Token:', this.authService.getToken());
      console.log('Current user:', this.authService.getCurrentUser());
      
      const donationData: DonationRequest = this.donationForm.value;
      
      this.donationService.createDonation(donationData).subscribe({
        next: (response) => {
          this.loading = false;
          alert('Doação criada com sucesso!');
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.loading = false;
          console.error('Create donation error:', error);
          console.error('Error status:', error.status);
          console.error('Error message:', error.error);
          
          if (error.status === 401) {
            this.error = 'Sessão expirada. Faça login novamente.';
            this.authService.logout();
            this.router.navigate(['/login']);
          } else if (error.status === 403) {
            this.error = 'Você não tem permissão para criar doações.';
          } else {
            this.error = error.error?.message || 'Erro ao criar doação. Tente novamente.';
          }
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched() {
    Object.keys(this.donationForm.controls).forEach(key => {
      const control = this.donationForm.get(key);
      control?.markAsTouched();
    });
  }

  getFieldError(fieldName: string): string {
    const field = this.donationForm.get(fieldName);
    if (field?.hasError('required')) {
      return `${this.getFieldLabel(fieldName)} é obrigatório`;
    }
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength']?.requiredLength;
      return `${this.getFieldLabel(fieldName)} deve ter pelo menos ${minLength} caracteres`;
    }
    if (field?.hasError('maxlength')) {
      const maxLength = field.errors?.['maxlength']?.requiredLength;
      return `${this.getFieldLabel(fieldName)} deve ter no máximo ${maxLength} caracteres`;
    }
    if (field?.hasError('min')) {
      return `Quantidade deve ser pelo menos 1`;
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      title: 'Título',
      description: 'Descrição',
      category: 'Categoria',
      quantity: 'Quantidade',
      expirationDate: 'Data de validade',
      storageInstructions: 'Instruções de armazenamento'
    };
    return labels[fieldName] || fieldName;
  }

  hasFieldError(fieldName: string): boolean {
    const field = this.donationForm.get(fieldName);
    return !!(field?.invalid && field?.touched);
  }
}
