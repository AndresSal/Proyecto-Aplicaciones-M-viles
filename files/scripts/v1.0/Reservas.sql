/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     12/02/2018 13:23:17                          */
/*==============================================================*/



/*==============================================================*/
/* Table: RESERVA                                               */
/*==============================================================*/
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

/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table users
(
   id              int not null AUTO_INCREMENT,
   username             varchar(200) not null,
   email                varchar(200) not null,
   password             text not null,
   gender               varchar(10) not null,
   constraint users_pk primary key (id)
);


