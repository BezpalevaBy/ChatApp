import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email = '';
  password = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.http.post<any>('http://localhost:8080/api/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        localStorage.setItem('currentEmail', this.email);
        this.router.navigate(['/chats']);
      },
      error: () => alert('Ошибка авторизации')
    });
  }
  

  register() {
    this.http.post<any>('http://localhost:8080/api/register', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        alert('Регистрация прошла успешно.');
        this.router.navigate(['/']);
      },
      error: (err) => alert(err.error.message || 'Ошибка регистрации')
    });
  }
}
