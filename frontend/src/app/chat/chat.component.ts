import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { log } from 'node:console';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './chat.component.html'
})
export class ChatComponent {
  email = '';
  password = '';
  recipient = '';
  content = '';
  messages: any[] = [];
  users: any[] = [];
  isLoggedIn = false;

  constructor(private http: HttpClient) {}

  loadUsers()
  {
    this.http.get<any[]>(`http://localhost:8080/api/users/${this.email}`)
    .subscribe(data => this.users = data);
  }

  login() {
    this.http.post<any>('http://localhost:8080/api/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.isLoggedIn = true;
      },
      error: () => alert('Ошибка авторизации')
    });

    this.loadUsers();
  }

  register() {
    this.http.post<any>('http://localhost:8080/api/register', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        alert('Регистрация прошла успешно. Теперь можно войти.');
      },
      error: (err) => {
        alert(err.error.message || 'Ошибка регистрации');
      }
    });
  }

  sendMessage() {
    this.http.post('http://localhost:8080/api/messages', {
      sender: this.email,
      recipient: this.recipient,
      content: this.content
    }).subscribe(() => {
      this.content = '';
      this.loadMessagesSpecificPerson(this.recipient);
    });
  }

  exit()
  {
    this.isLoggedIn = false;
  }

  loadMessagesSpecificPerson(targetEmail: string): void {

    console.log('HALO');
    
    this.http.get<any[]>(`http://localhost:8080/api/messages/${this.email}/${targetEmail}`)
      .subscribe({
        next: (data) => {
          this.messages = data;
        },
        error: (error) => {
          console.error('Ошибка загрузки сообщений:', error);
        }
      });
  }

  currentChatPartner(): string {
    if (!this.messages || this.messages.length === 0) {
      return 'No conversation';
    }

    const lastMsg = [...this.messages].reverse().find(msg => msg.sender !== this.email);
    return lastMsg ? lastMsg.sender : 'You';
  }
}
