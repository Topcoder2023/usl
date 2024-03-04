let server = http_listen(10086);
let host = server.host;
logger_info(host);
server.handler('/test', 'route/test.js');
server.start();