import { Component } from '@angular/core';
import { TokenService } from '../../services/token';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class HomeComponent {
  role: string | null;
  profileId: number | null;

  constructor(private tokenService: TokenService) {
    this.role = this.tokenService.getUserRole();
    this.profileId = this.tokenService.getProfileId();
  }
}
