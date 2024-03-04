declare function db_clear(a: string): any;

declare function db_connect(a: string): any;

declare function db_proxy(a: any): any;

declare function db_path(a: any): any;

declare function db_connect_path(a: string, b: string): any;

declare function db_clear_path(a: string, b: string): any;

declare function abs(a: number): any;

declare function sqrt(a: number): any;

declare function min(...a: any[]): any;

declare function max(...a: any[]): any;

declare function floor(a: number): any;

declare function ceil(a: number): any;

declare function round(a: number, b: number): any;

declare function number_word(a: number): any;

declare function digit_chinese(a: number): any;

declare function json_toList(a: string): any;

declare function json_toMap(a: string): any;

declare function json_toJson(a: any): any;

declare function lock(a: string): any;

declare function lock_release(a: string): any;

declare function lock_acquire(a: string): any;

declare function lock_with(a: string, b: any): any;

declare function contains(a: any, b: any): any;

declare function contains_all(a: any, b: any): any;

declare function contains_any(a: any, b: any): any;

declare function contains_none(a: any, b: any): any;

declare function file_list(a: string): any;

declare function file(a: string): any;

declare function file_delete(a: string): any;

declare function file_list_loop(a: string): any;

declare function file_exists(a: string): any;

declare function file_is_directory(a: string): any;

declare function script_execute(a: any): any;

declare function script_result(a: any): any;

declare function script_file(a: any): any;

declare function script_resource(a: string): any;

declare function http_listen_host(a: number, b: string): any;

declare function http_listen(a: number): any;

/*
 * HTTP-Server
 */
declare var server: HttpServer;

/*
 * USL-HTTP请求体变量
 * 存放当前请求的所有信息
 */
declare var request: HttpRequestWrapper;

/*
 * USL-HTTP响应体变量
 * 存放当前响应的所有信息
 */
declare var response: HttpResponseWrapper;

declare function list_intersectionDistinct(a: any, b: any): any;

declare function list_group(a: any, b: any): any;

declare function list_remove(a: any, b: number): any;

declare function list_size(a: any): any;

declare function list_get(a: any, b: number): any;

declare function list_indexOf(a: any, b: any): any;

declare function list_convert(a: any, b: any): any;

declare function list_clear(a: any): any;

declare function list_lastIndexOf(a: any, b: any): any;

declare function list_add(a: any, b: any): any;

declare function list_join(a: any, b: string): any;

declare function list_of(...a: any[]): any;

declare function list_contains(a: any, b: any): any;

declare function list(): any;

declare function list_from(a: any): any;

declare function list_addAll(a: any, b: any): any;

declare function list_filter(a: any, b: any, ): any;

declare function list_anyMatch(a: any, b: any): any;

declare function list_toMap(a: any, b: any, c: any): any;

declare function list_set(a: any, b: number, c: any): any;

declare function list_sort(a: any): any;

declare function list_removeIf(a: any, b: any, ): any;

declare function list_containsAll(a: any, b: any): any;

declare function list_distinct(a: any): any;

declare function list_allMatch(a: any, b: any): any;

declare function list_sub(a: any, b: number, c: number): any;

declare function list_addTo(a: any, b: number, c: any): any;

declare function list_containsAny(a: any, b: any): any;

declare function list_union(a: any, b: any): any;

declare function list_intersection(a: any, b: any): any;

declare function list_unionDistinct(a: any, b: any): any;

declare function list_disjunction(a: any, b: any): any;

declare function list_unionAll(a: any, b: any): any;

declare function list_resort(a: any): any;

declare function list_foreach(a: any, b: any): any;

declare function list_sortBy(a: any, b: any, ): any;

declare function list_resortBy(a: any, b: any, ): any;

declare function string_length(a: string): any;

declare function string_substring(a: string, b: number, c: number): any;

declare function string_replace(a: string, b: string, c: string): any;

declare function string_trim(a: string): any;

declare function string_regex(a: string, b: string): any;

declare function string_contains(a: string, b: string): any;

declare function string_starts_with(a: string, b: string): any;

declare function string_to_lower_case(a: string): any;

declare function string_index_of(a: any, b: any): any;

declare function string_trim_end(a: string): any;

declare function string_url_encode(a: string): any;

declare function string_trim_start(a: string): any;

declare function string_url_decode(a: string): any;

declare function string_ends_with(a: string, b: string): any;

declare function invoke(a: any, b: string, ...c: any[]): any;

declare function get(a: any, b: string): any;

declare function find(a: any, b: string): any;

declare function set(a: any, b: string, c: any): any;

declare function env(): any;

declare function get_env(a: string, ): any;

declare function set_env(a: string, b: any, ): any;

declare function is_true(a: any): any;

declare function is_empty(a: any): any;

declare function is_null(a: any): any;

declare function is_hex(a: string): any;

declare function is_false(a: any): any;

declare function is_citizenId(a: string): any;

declare function is_plateNumber(a: string): any;

declare function is_creditCode(a: string): any;

declare function is_chineseName(a: string): any;

declare function is_email(a: string): any;

declare function is_chinese(a: string): any;

declare function is_mobile(a: string): any;

declare function is_mac(a: string): any;

declare function is_birthday(a: string): any;

declare function is_ipv4(a: string): any;

declare function is_carVin(a: string): any;

declare function is_uuid(a: string): any;

declare function is_url(a: string): any;

declare function is_ipv6(a: string): any;

declare function is_carDrivingLicence(a: string): any;

declare function is_generalWithChinese(a: string): any;

declare function system_open_path(a: string): any;

declare function system_open(a: any): any;

declare function system_browse(a: string): any;

declare function to_string(a: any): any;

declare function to_boolean(a: any): any;

declare function to_bigDecimal(a: any): any;

declare function to_date(a: any): any;

declare function to_long(a: any): any;

declare function to_double(a: any): any;

declare function to_json(a: any): any;

declare function second(a: any): any;

declare function date_timestamp(): any;

declare function now(): any;

declare function year(a: any): any;

declare function date(a: number, b: number, c: number): any;

declare function date_of(a: number): any;

declare function date_format(a: any, b: string): any;

declare function month(a: any): any;

declare function minute(a: any): any;

declare function hour(a: any): any;

declare function add_days(a: any, b: number): any;

declare function is_after(a: any, b: any): any;

declare function is_before(a: any, b: any): any;

declare function day(a: any): any;

declare function add_weeks(a: any, b: number): any;

declare function add_months(a: any, b: number): any;

declare function weekday(a: any): any;

declare function add_hours(a: any, b: number): any;

declare function datediff_second(a: any, b: any): any;

declare function is_not_before(a: any, b: any): any;

declare function datediff_month(a: any, b: any): any;

declare function datediff_hour(a: any, b: any): any;

declare function datediff_year(a: any, b: any): any;

declare function datediff_day(a: any, b: any): any;

declare function datetime_value(a: string, b: string): any;

declare function datediff_minute(a: any, b: any): any;

declare function add_years(a: any, b: number): any;

declare function add_seconds(a: any, b: number): any;

declare function add_minutes(a: any, b: number): any;

declare function is_not_after(a: any, b: any): any;

declare function today(): any;

declare function date_value(a: string, b: string): any;

declare function random_chinese(): any;

declare function random_float(a: number, b: number): any;

declare function random_string(a: string, b: number): any;

declare function random_double(a: number, b: number, c: number, d: number): any;

declare function random_int(a: number, b: number): any;

declare function random_long(a: number, b: number): any;

declare function map_remove(a: any, b: any): any;

declare function map_size(a: any): any;

declare function map_get(a: any, b: any): any;

declare function map_put(a: any, b: any, c: any): any;

declare function map_values(a: any): any;

declare function map_clear(a: any): any;

declare function map_isEmpty(a: any): any;

declare function map_replace(a: any, b: any, c: any, d: any): any;

declare function map(): any;

declare function map_toList(a: any, b: any, ): any;

declare function map_of(a: any, b: any): any;

declare function map_filter(a: any, b: any, ): any;

declare function map_entrySet(a: any): any;

declare function map_putAll(a: any, b: any): any;

declare function map_putIfAbsent(a: any, b: any, c: any): any;

declare function map_containsKey(a: any, b: any): any;

declare function map_keySet(a: any): any;

declare function map_containsValue(a: any, b: any): any;

declare function map_getOrDefault(a: any, b: any, c: any): any;

declare function map_removeIf(a: any, b: any, ): any;

declare function map_filter_value(a: any, b: any, ): any;

declare function map_putIfPresent(a: any, b: any, c: any): any;

declare function map_foreach(a: any, b: any, ): any;

declare function map_filter_key(a: any, b: any, ): any;

declare function chat_100(a: string): any;

declare function db_execute(a: any, b: string): any;

declare function logger_info(a: string, ...b: any[]): any;

declare function console_info(a: string, ...b: any[]): any;

declare function logger_debug(a: string, ...b: any[]): any;

declare function console_log(a: string, ...b: any[]): any;

declare function logger_error(a: string, ...b: any[]): any;

declare function console_error(a: string, ...b: any[]): any;

declare function logger_warn(a: string, ...b: any[]): any;

declare function console_warn(a: string, ...b: any[]): any;

interface HttpServer {
    filter() : any
    start() : any
    handler() : any
    host : any
    port : any
    serverName : any
}

interface HttpRequestWrapper {
    path() : any
}

interface HttpResponseWrapper {
    set_content_type() : any
    write_to_string() : any
    write_to_json() : any
    set_content_type_by_resource() : any
    write_to_stream_by_resource() : any
}

