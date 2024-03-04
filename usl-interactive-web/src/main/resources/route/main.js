let server = http_listen(10086);

server.handler('/**', 'route/handler/static.js');

server.start();