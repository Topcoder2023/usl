let list = db_query_list('select * from test.user_profile');

for (let item in list) {
    logger_debug(get(item, 'id'));
    logger_debug(get(item, 'device_id'));
}