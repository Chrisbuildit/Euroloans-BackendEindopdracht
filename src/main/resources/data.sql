insert into roles(id) values ('ROLE_BORROWER'), ('ROLE_EMPLOYEE'), ('ROLE_LENDER');
insert into users(id, password, roles_id) values ('EMP', '$2a$12$g1QsVXqFXftyVJ5UXEjl7.6.UdgEB60vM7GCI.zyj/xhWNvh.Wsve', 'ROLE_EMPLOYEE'),
                                                 ('BOR', '$2a$12$g1QsVXqFXftyVJ5UXEjl7.6.UdgEB60vM7GCI.zyj/xhWNvh.Wsve', 'ROLE_BORROWER'),
                                                 ('LEN', '$2a$12$g1QsVXqFXftyVJ5UXEjl7.6.UdgEB60vM7GCI.zyj/xhWNvh.Wsve', 'ROLE_LENDER')


