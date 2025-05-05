import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { mergeApplicationConfig, ApplicationConfig } from '@angular/core';
import { provideServerRendering } from '@angular/platform-server';
import { provideHttpClient, withFetch } from '@angular/common/http';

export const appConfigServer = {
  apiUrl: 'http://192.168.0.100:8080',
};

const serverConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(),
    provideRouter(appRoutes),
    provideHttpClient(withFetch()),
  ],
};

export const config = mergeApplicationConfig(serverConfig);
