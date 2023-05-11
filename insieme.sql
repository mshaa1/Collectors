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
  nome varchar(25) unique not null,
  flag boolean
);

create table disco(
  ID integer primary key auto_increment not null,
  titolo varchar(35) not null,
  anno_uscita char(4) not null,
  barcode varchar(128), /*lunghezza massima barcode esistenti*/
  fomato enum('vinile', 'cd', 'digitale'),
  stato_conservazione enum ('nuovo', 'come nuovo', 'ottimo', 'buono', 'accettabile'),
  descrizione_conservazione varchar(255)
);

create table genere(
  ID integer primary key auto_increment not null,
  nome varchar(25) unique not null
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
  didascalia varchar(1000)
);


create table etichetta(
  ID integer primary key auto_increment not null,
  nome varchar(25) not null,
  sede_legale varchar(255) not null,
  email varchar(320) not null
);



/*-------- aggiungere on update --------*/

create table condivide(
  ID_collezione integer,
  foreign key (ID_collezione) references collezione(ID), /*collezione da condividere*/
  ID_collezionista integer,
  foreign key (ID_collezionista) references collezionista(ID) /*collezionista a cui è condivisa la collezione */

);

create table possiede_collezioni(
  ID_collezionista integer,
  ID_collezione integer,
  foreign key (ID_collezionista) references collezionista(ID),
  foreign key (ID_collezione) references collezione(ID)
);

create table colleziona_dischi(
  ID_collezionista integer,
  ID_disco integer,
  numero_duplicati integer not null,
  foreign key (ID_collezionista) references collezionista(ID),
  foreign key (ID_disco) references disco(ID)
);

create table comprende_dischi(
  ID_collezione integer,
  ID_disco integer,
  foreign key (ID_collezione) references collezione(ID),
  foreign key (ID_disco) references disco(ID)
);

create table inciso(
  ID_disco integer,
  ID_etichetta integer,
  foreign key (ID_disco) references disco(ID),
  foreign key (ID_etichetta) references etichetta(ID)
);

create table contiene_tracce(
  ID_disco integer,
  ID_traccia integer not null,
  foreign key (ID_disco) references disco(ID),
  foreign key (ID_traccia) references traccia(ID)
);

create table raffigurato(
  ID_disco integer,
  ID_immagine integer,
  foreign key (ID_disco) references disco(ID),
  foreign key (ID_immagine) references immagine(ID)
);

create table produce_disco(
  ID_disco integer,
  ID_autore integer,
  foreign key (ID_disco) references disco(ID),
  foreign key (ID_autore) references autore(ID)
);

create table realizza_traccia(
  ID_traccia integer,
  ID_autore integer,
  foreign key (ID_traccia) references traccia(ID),
  foreign key (ID_autore) references autore(ID)
);

create table genere_associato(
  ID_genere integer,
  ID_disco integer unique, /*il disco ha (1,1) generi, quindi non posso avere un disco con più generi ??*/
  foreign key (ID_genere) references genere(ID),
  foreign key (ID_disco) references disco(ID)
);