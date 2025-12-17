import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-orders-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders-table.html',
  styleUrls: ['./orders-table.scss']
})
export class OrdersTableComponent {
  @Input() orders: any[] = [];
}
