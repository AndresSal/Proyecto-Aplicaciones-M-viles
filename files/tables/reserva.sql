create table reserva
(
   id_reserva            int not null AUTO_INCREMENT,
   username				varchar(200) not null,
   email 				varchar(200) not null,
   fecha                varchar(10) not null,
   horario              varchar(10) not null,
   motivo               varchar(200) not null,
   nombre_institucion   varchar(200) not null,
   num_personas         varchar(10) not null,
   constraint reserva_pk primary key (id_reserva)
);