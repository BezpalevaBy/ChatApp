import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit {
  users: any[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  currentEmail: string = '';

  ngOnInit(): void {
    const savedEmail = localStorage.getItem('currentEmail');
    if (savedEmail) {
      this.currentEmail = savedEmail;
    }

    this.loadConnectedUsers();
  }

  loadUsers() {
    this.http.get<any[]>(`${environment.apiUrl}/api/users`).subscribe(data => {
     this.users = data;
    });
  }

  loadConnectedUsers()
  {
    this.http.get<any[]>(`${environment.apiUrl}/api/users/${this.currentEmail}`)
    .subscribe(data => this.users = data);
  }

  newEmail: string = '';

  startNewChat() {
    if (this.newEmail && this.newEmail.trim() !== '') {
      this.router.navigate(['/chat', this.newEmail.trim()]);
    }
  }

  selectUser(email: string) {
    this.router.navigate(['/chat', email]);
  }

  exit() {
    this.router.navigate(['']);
  }
}
