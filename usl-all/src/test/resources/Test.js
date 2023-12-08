let a = 10;

let list1 = list();

list_add(list1, a);

function f() {
    logger_info('hello {}', 'world');
}

for (let i in list1) {
    logger_info('遍历：{}', i);
    f();
}

await list1;




