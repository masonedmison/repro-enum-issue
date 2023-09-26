create TYPE mood AS ENUM ('sad', 'ok', 'happy');

create table person(name TEXT, age INT, mood mood);

insert into person (name, age, mood) values ('Alice', 30, 'happy'), ('Bob', 30, 'ok');
