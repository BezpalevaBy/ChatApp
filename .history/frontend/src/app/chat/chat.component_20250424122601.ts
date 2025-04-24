import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html'
})
export class ChatComponent {
  email = '';
  password = '';
  recipient = '';
  content = '';
  messages: any[] = [];
  isLoggedIn = false;

  constructor(private http: HttpClient) {}

  login() {
    this.http.post<any>('http://localhost:8080/api/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.isLoggedIn = true;
        this.loadMessages();
      },
      error: () => alert('Ошибка авторизации')
    });
  }

  sendMessage() {
    this.http.post('http://localhost:8080/api/messages', {
      sender: this.email,
      recipient: this.recipient,
      content: this.content
    }).subscribe(() => {
      this.content = '';
      this.loadMessages();
    });
  }

  loadMessages() {
    this.http.get<any[]>(`http://localhost:8080/api/messages/${this.email}`)
      .subscribe(data => this.messages = data);
  }
}
