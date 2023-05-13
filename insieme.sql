DROP SCHEMA IF EXISTS collectors;

CREATE SCHEMA IF NOT EXISTS collectors;
USE collectors;

create table collezionista(
  ID integer primary key auto_increment not null,
  email varchar(320) unique not null,
  nickname varchar(25) unique not null
);

create table collezione(
  ID integer primary key auto_increment not null,
  nome varchar(25) not null,
  flag boolean,
  ID_collezionista integer not null,
  foreign key (ID_collezionista) references collezionista(ID) on update cascade on delete cascade
);

create table etichetta(
  ID integer primary key auto_increment not null,
  nome varchar(25) not null,
  sede_legale varchar(255) not null,
  email varchar(320) not null
);

create table genere(
  ID integer primary key auto_increment not null,
  nome varchar(25) unique not null
);

create table disco(
  ID integer primary key auto_increment not null,
  titolo varchar(35) not null,
  anno_uscita smallint not null,
  barcode varchar(128), /*lunghezza massima barcode esistenti*/
  fomato enum('vinile', 'cd', 'digitale'),
  stato_conservazione enum ('nuovo', 'come nuovo', 'ottimo', 'buono', 'accettabile'),
  descrizione_conservazione varchar(255),
  ID_etichetta integer, 
  foreign key (ID_etichetta) references etichetta(ID) on update cascade on delete set null,
  ID_genere integer,
  foreign key (ID_genere) references genere(id) on update cascade on delete set null
);

create table traccia(
  ID integer primary key auto_increment not null,
  titolo varchar(50) not null,
  durata time not null
);

create table autore(
  ID integer primary key auto_increment not null,
  nome varchar(20) not null,
  cognome varchar(20) not null,
  data_nascita date not null,
  nome_autore varchar(25) not null,
  info varchar(255),
  ruolo enum('esecutore', 'compositore', 'poliedrico')
);

create table immagine(
  ID integer primary key auto_increment not null,
  file varchar(255) not null,
  didascalia varchar(1000),
  ID_disco integer,
  foreign key (ID_disco) references disco(ID) on update cascade on delete cascade
);



/*-------- outer tables --------*/

create table condivide(
  ID_collezione integer not null,
  foreign key (ID_collezione) references collezione(ID) on delete cascade,           /*collezione da condividere*/
  ID_collezionista integer not null,
  foreign key (ID_collezionista) references collezionista(ID) on delete cascade       /*collezionista a cui è condivisa la collezione */
);

create table colleziona_dischi(
  numero_duplicati integer not null,
  ID_collezionista integer not null,
  foreign key (ID_collezionista) references collezionista(ID) on update cascade on delete cascade,
  ID_disco integer not null,
  foreign key (ID_disco) references disco(ID) on update cascade on delete cascade
);

create table comprende_dischi(
  ID_collezione integer not null,
  foreign key (ID_collezione) references collezione(ID) on update cascade on delete cascade,
  ID_disco integer not null,
  foreign key (ID_disco) references disco(ID) on update cascade on delete cascade
);

create table contiene_tracce(
  ID_disco integer not null,
  foreign key (ID_disco) references disco(ID) on update cascade on delete cascade,
  ID_traccia integer not null,
  foreign key (ID_traccia) references traccia(ID) on update cascade on delete cascade
);

create table produce_disco(
  ID_disco integer not null,
  foreign key (ID_disco) references disco(ID) on update cascade on delete cascade,
  ID_autore integer not null,
  foreign key (ID_autore) references autore(ID) on update cascade on delete cascade
);

create table realizza_traccia(
  ID_traccia integer not null,
  foreign key (ID_traccia) references traccia(ID) on update cascade on delete cascade,
  ID_autore integer not null,
  foreign key (ID_autore) references autore(ID) on update cascade on delete cascade
);