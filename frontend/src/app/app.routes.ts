import { Routes } from '@angular/router';
import { LoginComponent } from './chat/login.component';
import { ChatListComponent } from './chat/chat-list.component';
import { ChatComponent } from './chat/chat.component';

export const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'chat-list', component: ChatListComponent },
  { path: 'chat/:email', component: ChatComponent },
  { path: 'chats', component: ChatListComponent },
];