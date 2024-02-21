a = 10;

b = 'str';

c = 20.888;

d = true;

let e = list();
list_add(e, 'a');
list_add(e, 'b');
list_add(e, 'c');
list_add(e, 'd');

for (i in e) {
    logger.info(i);
}