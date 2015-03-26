create table record (
id number(7),
date_appear Date,
levell varchar2(10),
source varchar2(30),
message varchar2(300),
constraint re_pk Primary Key(id)
);

insert into record(id, date_appear, levell, source, message)
values(gen_id.nextval, to_date('01-07-2014', 'DD-MM-YYYY'), 'info', 'in code', 'lalala');