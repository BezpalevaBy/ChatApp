import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./chat/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'chats',
    loadComponent: () => import('./chat/chat-list.component').then(m => m.ChatListComponent)
  },
  {
    path: 'chat/:email',
    loadComponent: () => import('./chat/chat.component').then(m => m.ChatComponent)
  }
];
