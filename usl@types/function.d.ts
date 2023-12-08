declare var Usl_Request: any;

declare var Usl_Response: any;

declare var Null: any;

declare function server_start(a: any): any;

declare function server_stop(a: any): any;

declare function server_filter(a: any, b: any, c: any): any;

declare function server(a: any, b: number): any;

declare function server_resource(a: any, b: any, c: string): any;

declare function server_route(a: any, b: any, c: string, d: any): any;

declare function server_route_script(a: any, b: any, c: string, d: any): any;

declare function logger_error(a: string, b: any): any;

declare function console_error(a: string, b: any): any;

declare function logger_debug(a: string, b: any): any;

declare function console_log(a: string, b: any): any;

declare function logger_info(a: string, b: any): any;

declare function console_info(a: string, b: any): any;

declare function logger_warn(a: string, b: any): any;

declare function console_warn(a: string, b: any): any;

declare function is_empty(a: any): any;

declare function is_null(a: any): any;

declare function is_false(a: any): any;

declare function is_true(a: any): any;

declare function is_hex(a: string): any;

declare function is_creditCode(a: string): any;

declare function is_carVin(a: string): any;

declare function is_chineseName(a: string): any;

declare function is_chinese(a: string): any;

declare function is_url(a: string): any;

declare function is_uuid(a: string): any;

declare function is_citizenId(a: string): any;

declare function is_birthday(a: string): any;

declare function is_ipv4(a: string): any;

declare function is_ipv6(a: string): any;

declare function is_email(a: string): any;

declare function is_mac(a: string): any;

declare function is_plateNumber(a: string): any;

declare function is_mobile(a: string): any;

declare function is_generalWithChinese(a: string): any;

declare function is_carDrivingLicence(a: string): any;

declare function response_write_string(a: any, b: string): any;

declare function response_write_json(a: any, b: any): any;

declare function invoke(a: any, b: string, c: any): any;

declare function get(a: any, b: string): any;

declare function find(a: any, b: string): any;

declare function set(a: any, b: string, c: any): any;

declare function get_env(a: any, b: string): any;

declare function set_env(a: any, b: string, c: any): any;

declare function random_int(a: number, b: number): any;

declare function random_chinese(): any;

declare function random_double(a: number, b: number, c: number, d: number): any;

declare function random_string(a: string, b: number): any;

declare function random_float(a: number, b: number): any;

declare function random_long(a: number, b: number): any;

declare function list_group(a: any, b: any, c: any): any;

declare function list_add(a: any, b: any): any;

declare function list_remove(a: any, b: number): any;

declare function list_get(a: any, b: number): any;

declare function list_indexOf(a: any, b: any): any;

declare function list_clear(a: any): any;

declare function list_contains(a: any, b: any): any;

declare function list_join(a: any, b: string): any;

declare function list_lastIndexOf(a: any, b: any): any;

declare function list_size(a: any): any;

declare function list_addAll(a: any, b: any): any;

declare function list_toMap(a: any, b: any, c: any, d: any): any;

declare function list(): any;

declare function list_set(a: any, b: number, c: any): any;

declare function list_of(a: any): any;

declare function list_containsAll(a: any, b: any): any;

declare function list_removeIf(a: any, b: any, c: any): any;

declare function list_sort(a: any): any;

declare function list_filter(a: any, b: any, c: any): any;

declare function list_from(a: any): any;

declare function list_convert(a: any, b: any, c: any): any;

declare function list_allMatch(a: any, b: any, c: any): any;

declare function list_anyMatch(a: any, b: any, c: any): any;

declare function list_distinct(a: any): any;

declare function list_containsAny(a: any, b: any): any;

declare function list_sub(a: any, b: number, c: number): any;

declare function list_union(a: any, b: any): any;

declare function list_unionDistinct(a: any, b: any): any;

declare function list_unionAll(a: any, b: any): any;

declare function list_disjunction(a: any, b: any): any;

declare function list_intersection(a: any, b: any): any;

declare function list_intersectionDistinct(a: any, b: any): any;

declare function list_toJson(a: any): any;

declare function list_sortBy(a: any, b: any, c: any): any;

declare function list_resortBy(a: any, b: any, c: any): any;

declare function list_addTo(a: any, b: number, c: any): any;

declare function list_resort(a: any): any;

declare function list_foreach(a: any, b: any, c: any): any;

declare function to_boolean(a: any): any;

declare function to_string(a: any): any;

declare function to_json(a: any): any;

declare function to_long(a: any): any;

declare function to_double(a: any): any;

declare function to_bigDecimal(a: any): any;

declare function to_date(a: any): any;

declare function script_run(a: any, b: any): any;

declare function script_result(a: any, b: any): any;

declare function script_path(a: any): any;

declare function script(a: any, b: string): any;

declare function script_content(a: any): any;

declare function json_toMap(a: string): any;

declare function json_toList(a: string): any;

declare function json_toJson(a: any): any;

declare function map(): any;

declare function map_remove(a: any, b: any): any;

declare function map_get(a: any, b: any): any;

declare function map_put(a: any, b: any, c: any): any;

declare function map_values(a: any): any;

declare function map_clear(a: any): any;

declare function map_isEmpty(a: any): any;

declare function map_replace(a: any, b: any, c: any, d: any): any;

declare function map_size(a: any): any;

declare function map_entrySet(a: any): any;

declare function map_putAll(a: any, b: any): any;

declare function map_putIfAbsent(a: any, b: any, c: any): any;

declare function map_keySet(a: any): any;

declare function map_containsKey(a: any, b: any): any;

declare function map_containsValue(a: any, b: any): any;

declare function map_getOrDefault(a: any, b: any, c: any): any;

declare function map_of(a: any, b: any): any;

declare function map_removeIf(a: any, b: any, c: any): any;

declare function map_filter(a: any, b: any, c: any): any;

declare function map_toList(a: any, b: any, c: any): any;

declare function map_toJson(a: any): any;

declare function map_putIfPresent(a: any, b: any, c: any): any;

declare function map_filter_value(a: any, b: any, c: any): any;

declare function map_foreach(a: any, b: any, c: any): any;

declare function map_filter_key(a: any, b: any, c: any): any;

