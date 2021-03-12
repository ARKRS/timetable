insert into usr(id, active, username, password, email)
values (1, true, 'RA', '237511', 'timers.it@ya.ru');

insert into user_role(user_id, roles)
values (1, 'ADMIN'),
       (1, 'USER');
