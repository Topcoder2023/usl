let server = http_listen(10086);
let host = server.host;
logger_info(host);
http_handler(server, '/test', 'route/test.js');
server.start();