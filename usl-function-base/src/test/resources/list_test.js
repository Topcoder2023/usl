// 创建List
let list = list_of('a', 'b', 'c', 'd');

for (let element in list) {
    logger_warn(env());
    logger_info(element);
}

let resort = list_resort(list);

logger_info('逆排序后 - {}', to_json(resort));

let mf = function matchFilter(ch) {
    return ch === 'a' || ch === 'b' || ch === 'c' || ch === 'd';
};

logger_warn(env());

let allMatch = list_allMatch(list, mf);

logger_warn(env());

logger_info(allMatch);

