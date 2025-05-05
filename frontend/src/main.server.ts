import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { config } from './app/app.config.server';

const bootstrap = () => bootstrapApplication(AppComponent, config);

export default bootstrap;

export function getPrerenderParams() {
    return [
      { email: 'user1@example.com' },
      { email: 'user2@example.com' }
    ];
  }
