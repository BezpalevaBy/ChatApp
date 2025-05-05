export const routes = [
    '/',
    '/login',
    '/chat-list',
    '/chats',
    {
      route: '/chat/:email',
      getPrerenderParams: () => [
        { email: 'alice@example.com' },
        { email: 'bob@example.com' }
      ]
    }
  ];
  