import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ChatApp';

  email = '';
  password = '';

  constructor(private http: HttpClient) {}

  login() {
    this.http.post(`${environment.apiUrl}/api/login`, {
      email: this.email,
      password: this.password
    }, { withCredentials: true }).subscribe({
      next: (response) => {
        console.log('Login successful', response);
        alert('Успешный вход');
      },
      error: (error) => {
        console.error('Login failed', error);
        alert(`Ошибка авторизации: ${error.status} ${error.statusText}`);
      }
    });
  }  

  register() {
    this.http.post(`${environment.apiUrl}/api/register`, {
      email: this.email,
      password: this.password
    }, { withCredentials: true }).subscribe({
      next: (response) => {
        console.log('Registration successful', response);
        alert('Регистрация успешна');
      },
      error: (error) => {
        console.error('Registration failed', error);
        alert('Ошибка регистрации: ${error.status} ${error.statusText}');
      }
    });
  }
}
