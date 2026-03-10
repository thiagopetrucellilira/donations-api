import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { CommonModule } from '@angular/common';
import { DonationService } from '../../services/donation.service';
import { MatchService } from '../../services/match.service';
import { AuthService } from '../../services/auth.service';
import { Donation, DonationStatus, FoodCategory, FOOD_CATEGORY_LABELS } from '../../models/donation.model';
import { Match, MatchStatus, UpdateMatchStatusRequest } from '../../models/match.model';
import { User } from '../../models/user.model';
import { Subscription, filter } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatChipsModule,
    MatMenuModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit, OnDestroy {
  currentUser: User | null = null;
  myDonations: Donation[] = [];
  receivedRequests: Match[] = [];
  myRequests: Match[] = [];
  loading = true;
  private routerSubscription?: Subscription;

  // Enum references for template
  MatchStatus = MatchStatus;
  DonationStatus = DonationStatus;

  constructor(
    private donationService: DonationService,
    private matchService: MatchService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadDashboardData();
    
    // Listen for navigation events to refresh data when returning from create-donation
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/dashboard') {
          console.log('Returned to dashboard, refreshing data...');
          this.loadDashboardData();
        }
      });
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  loadDashboardData() {
    this.loading = true;
    console.log('Loading dashboard data...');
    
    let completedRequests = 0;
    const totalRequests = 3;
    
    const checkComplete = () => {
      completedRequests++;
      if (completedRequests >= totalRequests) {
        this.loading = false;
        console.log('Dashboard data loaded successfully');
      }
    };
    
    // Load user's donations
    this.donationService.getUserDonations().subscribe({
      next: (donations) => {
        this.myDonations = donations;
        console.log('Loaded user donations:', donations.length);
        checkComplete();
      },
      error: (error) => {
        console.error('Error loading user donations:', error);
        checkComplete();
      }
    });

    // Load received requests (for user's donations)
    this.matchService.getReceivedRequests().subscribe({
      next: (requests) => {
        console.log('Raw API response for received requests:', requests);
        console.log('Type of received requests:', typeof requests);
        console.log('Is array:', Array.isArray(requests));
        if (Array.isArray(requests) && requests.length > 0) {
          console.log('First received request structure:', requests[0]);
        }
        this.receivedRequests = requests;
        console.log('Loaded received requests:', requests.length);
        checkComplete();
      },
      error: (error) => {
        console.error('Error loading received requests:', error);
        checkComplete();
      }
    });

    // Load user's requests (for other donations)
    this.matchService.getMyRequests().subscribe({
      next: (requests) => {
        console.log('✅ My requests loaded:', requests?.length || 0, 'items');
        this.myRequests = requests || [];
        checkComplete();
      },
      error: (error) => {
        console.error('❌ Error loading my requests:', error);
        this.myRequests = [];
        checkComplete();
      }
    });
  }

  refreshData() {
    console.log('Manual refresh triggered');
    this.loadDashboardData();
  }

  updateMatchStatus(match: Match, status: MatchStatus, notes?: string) {
    const request: UpdateMatchStatusRequest = {
      status,
      donorNotes: notes
    };

    this.matchService.updateMatchStatus(match.id!, request).subscribe({
      next: (updatedMatch) => {
        // Update the match in the list
        const index = this.receivedRequests.findIndex(r => r.id === match.id);
        if (index !== -1) {
          this.receivedRequests[index] = updatedMatch;
        }
        
        const statusText = this.getStatusText(status);
        alert(`Solicitação ${statusText.toLowerCase()} com sucesso!`);
      },
      error: (error) => {
        console.error('Error updating match status:', error);
        alert('Erro ao atualizar status da solicitação');
      }
    });
  }

  deleteDonation(donation: Donation) {
    if (confirm(`Tem certeza que deseja excluir a doação "${donation.title}"?`)) {
      this.donationService.deleteDonation(donation.id!).subscribe({
        next: () => {
          this.myDonations = this.myDonations.filter(d => d.id !== donation.id);
          alert('Doação excluída com sucesso!');
        },
        error: (error) => {
          console.error('Error deleting donation:', error);
          alert('Erro ao excluir doação');
        }
      });
    }
  }

  getStatusColor(status?: DonationStatus | MatchStatus): string {
    switch (status) {
      case DonationStatus.AVAILABLE:
      case MatchStatus.APPROVED: 
        return 'primary';
      case DonationStatus.RESERVED:
      case DonationStatus.PENDING:
      case MatchStatus.PENDING:
      case MatchStatus.IN_PROGRESS: 
        return 'warn';
      case DonationStatus.COMPLETED:
      case MatchStatus.COMPLETED: 
        return 'accent';
      case MatchStatus.REJECTED: 
        return '';
      default: 
        return '';
    }
  }

  getStatusText(status?: DonationStatus | MatchStatus): string {
    switch (status) {
      case DonationStatus.AVAILABLE: return 'Disponível';
      case DonationStatus.RESERVED: return 'Reservada';
      case DonationStatus.PENDING: return 'Pendente';
      case DonationStatus.COMPLETED: return 'Concluída';
      case DonationStatus.EXPIRED: return 'Expirada';
      case DonationStatus.CANCELLED: return 'Cancelada';
      case MatchStatus.PENDING: return 'Aguardando';
      case MatchStatus.APPROVED: return 'Aprovada';
      case MatchStatus.IN_PROGRESS: return 'Em andamento';
      case MatchStatus.REJECTED: return 'Rejeitada';
      case MatchStatus.COMPLETED: return 'Concluída';
      default: return 'Desconhecido';
    }
  }

  getCategoryLabel(category?: FoodCategory): string {
    if (!category) return '';
    return FOOD_CATEGORY_LABELS[category] || String(category);
  }

  canUpdateDonation(donation: Donation): boolean {
    return donation.status === DonationStatus.AVAILABLE;
  }

  navigateToEdit(donation: Donation) {
    // This would navigate to an edit form - for now just show message
    alert('Funcionalidade de edição será implementada em breve!');
  }

  getDonationStats() {
    const available = this.myDonations.filter(d => d.status === DonationStatus.AVAILABLE).length;
    const completed = this.myDonations.filter(d => d.status === DonationStatus.COMPLETED).length;
    const pending = this.myDonations.filter(d => d.status === DonationStatus.PENDING).length;
    
    return { available, completed, pending, total: this.myDonations.length };
  }

  getRequestStats() {
    const pending = this.receivedRequests.filter(r => r.status === MatchStatus.PENDING).length;
    const approved = this.receivedRequests.filter(r => r.status === MatchStatus.APPROVED).length;
    
    return { pending, approved, total: this.receivedRequests.length };
  }
}
