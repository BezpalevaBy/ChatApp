import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  login() {
    if (this.form.invalid) {
      alert('Введите корректный email и пароль (минимум 3 символов).');
      return;
    }

    const { email, password } = this.form.value;

    this.http.post<any>(`${environment.apiUrl}/api/login`, { email, password }).subscribe({
      next: () => {
        localStorage.setItem('currentEmail', email);
        this.router.navigate(['/chats']);
      },
      error: () => alert('Ошибка авторизации: проверьте email и пароль.')
    });
  }

  register() {
    if (this.form.invalid) {
      alert('Введите корректный email и пароль (минимум 3 символов).');
      return;
    }

    const { email, password } = this.form.value;

    this.http.post<any>(`${environment.apiUrl}/api/register`, { email, password }).subscribe({
      next: () => {
        alert('Регистрация прошла успешно.');
        this.router.navigate(['/']);
      },
      error: (err) => {
        if (err.status === 409) {
          alert('Такой пользователь уже существует.');
        } else {
          alert('Ошибка регистрации.');
        }
      }
    });
  }
}