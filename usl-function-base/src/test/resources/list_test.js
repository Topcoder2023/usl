// 创建List
let list = list_of('a', 'b', 'c', 'd');

for (let element in list) {
    let number = list_indexOf(list, list_get(list, 1)) * 20;
    logger_info(to_string(number));
    logger_info(element);
}

let resort = list_resort(list);

logger_info('逆排序后 - {}', to_json(resort));

let mf = function matchFilter(ch) {
    return ch === 'a' || ch === 'b' || ch === 'c' || ch === 'd';
};

let allMatch = list_allMatch(list, mf);
logger_info(allMatch);

