let path = string_substring(request.path(), 1, string_length(request.path()));

let resourceName = path + '.html';

response.set_content_type_by_resource(resourceName);
response.write_to_stream_by_resource(resourceName, 'public/404');



