import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, RouterOutlet],
  template: `
    <header>Marketplace</header>
    <router-outlet></router-outlet>
    <footer>© 2025</footer>
  `,
  styleUrls: ['./app.scss']
})
export class AppComponent {}