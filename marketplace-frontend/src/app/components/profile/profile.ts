import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../services/profile';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss']
})
export class ProfileComponent {
  profileId: number | null = null;
  profileData: any = null;
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private profileService: ProfileService
  ) {
    // Prende l'id dalla rotta
    this.profileId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.profileId) {
      this.fetchProfile(this.profileId);
    } else {
      this.errorMessage = 'ID profilo non valido';
    }
  }

  fetchProfile(id: number) {
    this.loading = true;
    this.profileService.getProfile(id).subscribe({
      next: res => {
        this.profileData = res.data;
        this.loading = false;
        console.log('Dati profilo:', this.profileData); 
      },
      error: err => {
        console.error('Errore fetching profile', err);
        this.errorMessage = 'Errore nel caricamento del profilo';
        this.loading = false;
      }
    });
  }
}
