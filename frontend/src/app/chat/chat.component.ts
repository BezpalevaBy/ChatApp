import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router'; 
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  email = 'user@example.com';
  recipient: string = '';
  content = '';
  messages: any[] = [];
  isLoggedIn = true;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const savedEmail = localStorage.getItem('currentEmail');
    if (savedEmail) {
      this.email = savedEmail;
    }
  
    this.route.paramMap.subscribe(params => {
      this.recipient = params.get('email')!;
      this.loadMessagesSpecificPerson(this.recipient);
    });
  }  

  loadMessagesSpecificPerson(targetEmail: string) {
    this.http.get<any[]>(`${environment.apiUrl}/api/messages/${this.email}/${targetEmail}`)
      .subscribe({
        next: (data) => {
          this.messages = data;
          setTimeout(() => this.scrollToBottom(), 100);
        },
        error: (error) => console.error('Ошибка загрузки сообщений:', error)
      });
  }
  
  scrollToBottom() {
    const element = document.querySelector('.message-list');
    if (element) {
      element.scrollTop = element.scrollHeight;
    }
  }    

  sendMessage() {
    this.http.post(`${environment.apiUrl}/api/messages`, {
      sender: this.email,
      recipient: this.recipient,
      content: this.content
    }).subscribe(() => {
      this.content = '';
      this.loadMessagesSpecificPerson(this.recipient);
    });
  }

  exit() {
    this.router.navigate(['/chats']);
  }
}
