drop schema if exists collectors;

create schema if not exists collectors;
use collectors;

drop table if exists collezionista;
drop table if exists collezione;
drop table if exists etichetta;
drop table if exists genere;
drop table if exists disco;
drop table if exists traccia;
drop table if exists autore;
drop table if exists immagine;
drop table if exists condivide;
drop table if exists colleziona_dischi;
drop table if exists comprende_dischi;
drop table if exists produce_disco;
drop table if exists realizza_traccia;

drop user if exists 'admin'@'localhost';

create user 'admin'@'localhost' identified by 'admin';
grant all privileges on collectors.* to 'admin'@'localhost';

create table collezionista(
  ID integer primary key auto_increment not null,
  email varchar(320) unique not null check (email REGEXP '[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]+$'),
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
  email varchar(320) not null check (email REGEXP '[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]+$')
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
  formato enum('vinile', 'cd', 'digitale', 'cassetta'),
  stato_conservazione enum ('nuovo', 'come nuovo', 'ottimo', 'buono', 'accettabile'),
  descrizione_conservazione varchar(255),
  ID_etichetta integer,
  foreign key (ID_etichetta) references etichetta(ID) on update cascade on delete set null,
  ID_genere integer,
  foreign key (ID_genere) references genere(id) on update cascade on delete set null
);

create table traccia(
  ID integer primary key auto_increment not null,
  titolo varchar(50) unique not null,
  durata time not null,
  ID_disco integer,
  foreign key (ID_disco) references disco(ID) on update cascade on delete cascade
);

create table autore(
  ID integer primary key auto_increment not null,
  nome varchar(20),
  cognome varchar(20),
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
  foreign key (ID_collezione) references collezione(ID) on update cascade on delete cascade,
  ID_collezionista integer not null,
  foreign key (ID_collezionista) references collezionista(ID) on update cascade on delete cascade
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

use collectors;
drop function if exists inserisci_collezione;
drop procedure if exists inserisci_disco_collezione;
drop procedure if exists inserisci_tracce_disco;
drop procedure if exists modifica_flag_collezione;
drop procedure if exists rimozione_disco_collezione;
drop procedure if exists rimozione_collezione;
drop procedure if exists lista_dischi_collezione;
drop procedure if exists tracklist_disco;
drop procedure if exists ricerca_dischi_per_titolo_autore;
drop view if exists lista_dischi;
drop function if exists verifica_visibilita_collezione;
drop procedure if exists gestione_disco;
drop procedure if exists ricerca_collezione;
drop function if exists numero_tracce_distinte_per_autore_collezioni_pubbliche;
drop function if exists minuti_totali_musica_pubblica_per_autore;
drop procedure if exists statistiche_dischi_per_genere;
drop procedure if exists statistiche_numero_collezioni;
drop procedure if exists ricerca_dischi_per_autore_titolo;
drop view if exists lista_dischi_generale;
drop procedure if exists ricerca_dischi_per_titolo_autore;

delimiter $

-- Funzionalità 1
-- inserimento di una nuova collezione.
create function inserisci_collezione(nome varchar(25), flag boolean, ID_collezionista integer)
    returns integer
    deterministic
begin
    declare id int unsigned;

    insert into collezione (nome, flag, ID_collezionista)
    values (nome, flag, ID_collezionista);
    set id = last_insert_id();

    return id;
end$


-- Funzionalità 2
-- Aggiunta di dischi a una collezione e di tracce a un disco.

create procedure inserisci_disco_collezione(in ID_disco integer, in ID_collezione integer)
begin
    insert into comprende_dischi(ID_disco, ID_collezione) values (ID_disco, ID_collezione);
end$

create procedure inserisci_tracce_disco(in ID_disco integer, in titolo varchar(35), in durata time)
begin
    insert into traccia(titolo, durata, ID_disco) values (titolo, durata, ID_disco);
end$


-- Funzionalità 3
-- Modifica dello stato di pubblicazione di una collezione (da privata a pubblica e viceversa) e aggiunta di nuove condivisioni a una collezione.
create procedure modifica_flag_collezione(in ID_collezione integer, in flag boolean)
begin
    update collezione set collezione.flag = flag where ID_collezione = ID; -- flag = 0 - collezione privata | flag = 1 - collezione pubblica
end$


-- Funzionalità 4
-- Rimozione di un disco da una collezione.
create procedure rimozione_disco_collezione(in ID_disco integer, in ID_collezione integer)
begin
    delete from comprende_dischi c where c.ID_disco = ID_disco and c.ID_collezione = ID_collezione;
end$

-- Funzionalità 5
-- Rimozione di una collezione.
create procedure rimozione_collezione(in ID_collezione integer)
begin
    delete from collezione where ID = ID_collezione;
end$

-- Funzionalità 6
-- Lista di tutti i dischi in una collezione

-- creo la view dei dischi
create view lista_dischi as
select disco.ID,
       titolo,
       anno_uscita    as 'anno di uscita',
       genere.nome    as genere,
       formato,
       stato_conservazione,
       descrizione_conservazione,
       barcode,
       etichetta.nome as azienda,
       sede_legale,
       email
from disco
         join etichetta on disco.ID_etichetta = etichetta.ID
         join genere on disco.ID_genere = genere.ID;


create procedure lista_dischi_collezione(in ID_collezione integer)
begin
    select id,
           titolo,
           lista_dischi.`anno di uscita`,
           barcode,
           formato,
           stato_conservazione,
           descrizione_conservazione
    from comprende_dischi
             join lista_dischi on ID_disco = lista_dischi.ID
    where comprende_dischi.ID_collezione = ID_collezione;
end$

-- Funzionalità 7
-- Tracklist di un disco
create procedure tracklist_disco(in ID_disco integer)
begin
    select id, titolo, durata
    from traccia
    where traccia.ID_disco = ID_disco;
end$

-- Funzionalità 8
/* Ricerca di dischi in base a nomi di autori/compositori/interpreti e/o titoli. Si
potrà decidere di includere nella ricerca le collezioni di un certo collezionista
e/o quelle condivise con lo stesso collezionista e/o quelle pubbliche. */

create view lista_dischi_generale as
select c.nome, c1.nickname, d.*, a.nome_autore, c.flag, ID_collezionista
from disco d
         join produce_disco pd on d.ID = pd.ID_disco
         join autore a on pd.ID_autore = a.ID
         join comprende_dischi cd on d.ID = cd.ID_disco
         join collezione c on cd.ID_collezione = c.ID
         join collezionista c1 on ID_collezionista = c1.ID;

-- lasciare null qualsiasi campo eccetto ID_collezionista per non effettuare la ricerca su quel campo
create procedure ricerca_dischi_per_autore_titolo(in nome_autore varchar(25), in titolo_disco varchar(50),
                                                  in pubbliche boolean, in condivise boolean, in private boolean,
                                                  in ID_collezionista int)
begin
    select distinct -- nickname as 'proprietario collezione',
                    ID,
                    titolo,
                    anno_uscita               as 'anno di uscita',
                    barcode,
                    formato,
                    stato_conservazione       as 'stato di conservazione',
                    descrizione_conservazione as 'descrizione conservazione'
                    -- lUl.nome_autore as 'nome autore'
    from (select *
          from lista_dischi_generale l
          where (
                      lower(l.titolo) regexp lower(titolo_disco) or lower(l.nome_autore) regexp lower(nome_autore)
              )
            and l.ID_collezionista = ID_collezionista
            and private = 1
            -- ricerca nelle collezioni del collezionista

          union
          -- unione per ottenere entrambe le ricerche

          select *
          from lista_dischi_generale l
          where (
                      lower(l.titolo) regexp lower(titolo_disco) or lower(l.nome_autore) regexp lower(nome_autore)
              )
            and flag = 1
            and pubbliche = 1
            -- ricerca nelle collezioni pubbliche

          union

          select ldg.*
          from collezione c
                   join condivide cd on c.ID = cd.ID_collezione
                   join comprende_dischi d on c.ID = d.ID_collezione
                   join lista_dischi_generale ldg on d.ID_disco = ldg.ID
          where (
                      lower(ldg.titolo) regexp lower(titolo_disco) or lower(ldg.nome_autore) regexp lower(nome_autore)
              )
            and cd.ID_collezionista = ID_collezionista
            and condivise = 1 -- ricerca nelle condivise
         ) as `lUl`;
end$

-- Funzionalità 9
-- verifica della visibilità di una collezione da parte di un collezionista

create function verifica_visibilita_collezione(ID_collezione integer, ID_collezionista integer)
    returns boolean
    deterministic
begin
    if (select flag from collezione where collezione.ID = ID_collezione) = 1 then -- se la collezione ha flag impostato a true allora la collezione è visibile a tutti i collezionisti
        return true;
    end if;
    if (select ID_collezionista
        from collezione
        where collezione.ID = ID_collezione
          and collezione.ID_collezionista = ID_collezionista) then -- se la collezione appartiene al collezionista, allora può visionarla
        return true;
    end if;
    if (select count(*)
        from condivide c
        where c.ID_collezione = ID_collezione
          and c.ID_collezionista = ID_collezionista) = 1 then -- se la collezione è condivisa con il collezionista, allora può visionarla
        return true;
    end if;
    return false;
end$


-- Funzionalità 10
-- numero di tracce di dischi distinti di un certo autore presenti nelle collezioni pubbliche

create function numero_tracce_distinte_per_autore_collezioni_pubbliche(ID_autore integer)
    returns integer
    deterministic
begin
    return (select count(distinct traccia.ID)
            from traccia
                     join disco on traccia.ID_disco = disco.ID
                     join produce_disco on disco.ID = produce_disco.ID_disco
                     join autore on produce_disco.ID_autore = autore.ID
                     join comprende_dischi on disco.ID = comprende_dischi.ID_disco
                     join collezione on comprende_dischi.ID_collezione = collezione.ID
            where autore.ID = ID_autore
              and collezione.flag = 1);
end $

-- Funzionalità 11
-- Minuti totali di musica riferibili a un certo autore memorizzati nelle collezioni pubbliche
create function minuti_totali_musica_pubblica_per_autore(ID_autore integer)
    returns integer
    deterministic
begin
    return (select sum(traccia.durata) / 60
            from traccia
                     join disco on traccia.ID_disco = disco.ID
                     join produce_disco on disco.ID = produce_disco.ID_disco
                     join autore on produce_disco.ID_autore = autore.ID
                     join comprende_dischi on disco.ID = comprende_dischi.ID_disco
                     join collezione on comprende_dischi.ID_collezione = collezione.ID
            where autore.ID = ID_autore
              and collezione.flag = 1);
end$

-- Funzionalità 12

-- 12a
-- statistiche: numero collezioni di ciascun collezionista

create procedure statistiche_numero_collezioni()
begin
    select collezionista.*, count(all collezione.ID)
    from collezione
             join collezionista on collezione.ID_collezionista = collezionista.ID
    group by nickname;
end$


-- 12b
-- statistiche: numero di dischi per genere nel sistema

create procedure statistiche_dischi_per_genere()
begin
    select genere.*, count(all disco.id)
    from disco
             join genere on disco.ID_genere = genere.ID
    group by nome;
end$

-- 13
-- la funzionalità 13 è stata implementata in Java, nella classe Query_JDBC.


-- *****  Altre query per il funzionamento dell' applicazione *****

drop procedure if exists inserisci_condivisione;
drop procedure if exists aggiunta_autore;
drop procedure if exists aggiunta_genere;
drop procedure if exists rimozione_genere;
drop function if exists convalida_utente;
drop procedure if exists registrazione_utente;
drop function if exists prendi_ID_utente;
drop procedure if exists get_collezioni_utente;
drop procedure if exists get_dischi_utente_etichetta;
drop procedure if exists get_etichetta_disco;
drop function if exists statistiche_numero_collezioni_collezionista;
drop procedure if exists get_autori;
drop procedure if exists get_genere_disco;
drop procedure if exists get_dischi_utente;
drop procedure if exists get_autori_disco;
drop procedure if exists inserisci_disco;
drop procedure if exists get_collezioni_condivise_e_proprietario;
drop procedure if exists rimuovi_disco;
drop procedure if exists rimuovi_traccia;
drop procedure if exists aggiunta_etichetta;
drop procedure if exists get_numero_duplicati_dischi;
drop procedure if exists get_Collezionisti_Da_Condivisa_Collezione;
drop procedure if exists rimuovi_condivisione;
drop procedure if exists remove_immagine_disco;
drop procedure if exists add_immagine_disco;
drop procedure if exists get_immagini_disco;

-- Funzionalità 15
-- Aggiunta di nuove condivisioni a una collezione'

create procedure inserisci_condivisione(in ID_collezione integer, in ID_collezionista integer)
begin
    insert into condivide(ID_collezione, ID_collezionista) values (ID_collezione, ID_collezionista);
end$


-- Funzionalità 16
-- aggiunta di un autore

create procedure aggiunta_autore(in nome varchar(25), in cognome varchar(25), in data_nascita date,
                                 in nome_autore varchar(25), in info varchar(255), in ruolo varchar(25))
begin
    insert into autore(nome, cognome, data_nascita, nome_autore, info, ruolo)
    values (nome, cognome, data_nascita, nome_autore, info, ruolo);
end$


-- Funzionalità 17
-- aggiunta di un genere

create procedure aggiunta_genere(in nome varchar(25))
begin
    select count(nome) from genere g where lower(g.nome) = lower('syh') into @nome;
    if (@nome = 0) then
        insert into genere(nome) values (nome);
    else
        signal sqlstate '45001';
    end if;
end$


-- Funzionalità 18
-- rimozione di un genere
create procedure rimozione_genere(in ID_genere int)
begin
    delete from genere where ID_genere = ID;
end$


-- Funzionalità 19
-- convalida l'accesso di un utente

create function convalida_utente(nickname varchar(25), email varchar(25))
    returns boolean
    deterministic
begin
    return (select count(*) from collezionista where collezionista.nickname = nickname and collezionista.email = email);
end$


-- Funzionalità 20
-- Registrazione utente
create procedure registrazione_utente(in nickname varchar(25), in email varchar(25))
begin
    insert into collezionista(nickname, email) values (nickname, email);
end$


-- Funzionalità 21
-- ottenimento ID utente per l'interfacciamento con Java

create function prendi_ID_utente(nickname varchar(25), email varchar(25))
    returns integer
    deterministic
begin
    return (select collezionista.ID
            from collezionista
            where collezionista.nickname = nickname and collezionista.email = email);
end$


-- Funzionalità 22
-- prendo tutte le collezioni di un utente

create procedure get_collezioni_utente(in ID_utente integer)
begin
    select * from collezione where collezione.ID_collezionista = ID_utente;
end$


-- Funzionalità 23
-- tutti i dichi di un utente con etichetta


create procedure get_dischi_utente_etichetta(in ID_utente integer)
begin
    select disco.ID,
           titolo,
           anno_uscita,
           barcode,
           formato,
           stato_conservazione,
           descrizione_conservazione,
           etichetta.ID as etichetta_id,
           nome,
           sede_legale,
           email
    from disco
             join colleziona_dischi on disco.ID = colleziona_dischi.ID_disco
             join etichetta on disco.ID_etichetta = etichetta.ID
    where colleziona_dischi.ID_collezionista = ID_utente;
end $


-- Funzionalità 24
-- etichetta di un disco


create procedure get_etichetta_disco(in ID_disco integer)
begin
    select *
    from etichetta
             join disco on disco.ID_etichetta = etichetta.ID
    where disco.ID = ID_disco;
end $


-- Funzionalità 25
-- numero di collezioni di un collezionista
create function statistiche_numero_collezioni_collezionista(ID_collezionista integer)
    returns integer
    deterministic
begin
    return (select count(all collezione.ID) from collezione where collezione.ID_collezionista = ID_collezionista);
end $


-- Funzionalità 26
-- get tutti autori


create procedure get_autori()
begin
    select * from autore;
end $

-- Funzionalità 27
-- get genere di un disco


create procedure get_genere_disco(in ID_disco integer)
begin
    select genere.ID, genere.nome
    from genere
             join disco on disco.ID_genere = genere.ID
    where disco.ID = ID_disco;
end $


-- Funzionalità 28
-- tutti i dischi di un utente


create procedure get_dischi_utente(in ID_utente integer)
begin
    select distinct *
    from disco
             join colleziona_dischi on disco.ID = colleziona_dischi.ID_disco
    where colleziona_dischi.ID_collezionista = ID_utente;
end $

-- Funzionalità 29
-- get autore di un disco


create procedure get_autori_disco(in ID_disco integer)
begin
    select *
    from disco d
             join produce_disco on ID = produce_disco.ID_disco
             join autore a on ID_autore = a.ID
    where d.ID = ID_disco;
end $


-- Funzionalità 30

create procedure inserisci_disco(in ID_collezionista varchar(25), in titolo varchar(25), in anno_uscita int,
                                 in barcode varchar(25), in formato varchar(25), in stato_conservazione varchar(25),
                                 in descrizione_conservazione varchar(255), in ID_etichetta integer,
                                 in ID_genere integer)
begin
    select disco.ID
    from disco
    join colleziona_dischi on disco.ID = colleziona_dischi.ID_disco
    where colleziona_dischi.ID_collezionista = ID_collezionista
      and disco.titolo = titolo
      and disco.anno_uscita = anno_uscita
      and disco.barcode = barcode
      and disco.formato = formato
      and disco.stato_conservazione = stato_conservazione
      and disco.descrizione_conservazione = descrizione_conservazione
      and disco.ID_etichetta = ID_etichetta
      and disco.ID_genere = ID_genere

    into @ID_disco;

    if (@ID_disco is null) then
        insert into disco(titolo, anno_uscita, barcode, formato, stato_conservazione, descrizione_conservazione,
                          ID_etichetta, ID_genere)
        values (titolo, anno_uscita, barcode, formato, stato_conservazione, descrizione_conservazione, ID_etichetta,
                ID_genere);
        insert into colleziona_dischi (ID_collezionista, ID_disco, numero_duplicati)
        values (ID_collezionista, last_insert_id(), 0);

        else

            update colleziona_dischi
        set numero_duplicati = numero_duplicati + 1
        where  ID_collezionista = colleziona_dischi.ID_collezionista
          and colleziona_dischi.ID_disco = @ID_disco;

            end if;



end $


-- funzionalità 31
-- get collezioni condivise con un utente


create procedure get_collezioni_condivise_e_proprietario(in ID_utente integer)
begin
    select c.ID as ID_collezione, c.nome, c.flag, prop.ID as ID_propietario, prop.email, prop.nickname
    from collezione c
             join collezionista prop on c.ID_collezionista = prop.ID
             join condivide con on c.ID = con.ID_collezione
             join collezionista col on con.ID_collezionista = col.ID
    where col.ID = ID_utente;
end $

-- funzionalità 32
-- rimozione del disco in tutto il database


create procedure rimuovi_disco(in ID_disco integer)
begin
    delete from disco where disco.ID = ID_disco;
end $

-- funzionalità 33
-- rimozione di una traccia in tutto il database


create procedure rimuovi_traccia(in ID_traccia integer)
begin
    delete from traccia where traccia.ID = ID_traccia;
end $


-- funzionalità 35
-- aggiunta di una etichetta al database


create procedure aggiunta_etichetta(in nome varchar(25), in sede_legale varchar(255), in email varchar(320))
begin
    insert into etichetta(nome, sede_legale, email) values (nome, sede_legale, email);
end$

-- funzionalità 36
-- get numero duplicati dischi


create procedure get_numero_duplicati_dischi(in ID_collezionista integer)
begin
    select colleziona_dischi.numero_duplicati,
           disco.ID,
           titolo,
           anno_uscita,
           barcode,
           formato,
           stato_conservazione,
           descrizione_conservazione,
           ID_etichetta,
           ID_genere
    from colleziona_dischi
             join collezionista on colleziona_dischi.ID_collezionista = collezionista.ID
             join disco on colleziona_dischi.ID_disco = disco.ID
    where colleziona_dischi.ID_collezionista = ID_collezionista
      and colleziona_dischi.numero_duplicati > 0;
end $

-- funzionalità 37
-- get collezionisti a cui è condivisa la data collezione
create procedure get_Collezionisti_Da_Condivisa_Collezione(in ID_collezione integer)
begin
    select collezionista.ID, collezionista.email, collezionista.nickname
    from collezionista
             join condivide on collezionista.ID = condivide.ID_collezionista
    where condivide.ID_collezione = ID_collezione;
end $

-- funzionalità 38
-- rimozione condivisione collezione
create procedure rimuovi_condivisione(in ID_collezione integer, in ID_collezionista integer)
begin
    delete
    from condivide
    where condivide.ID_collezione = ID_collezione and condivide.ID_collezionista = ID_collezionista;
end $



-- funzionalità 39
-- get immagini di un disco
create procedure get_immagini_disco(in ID_disco integer)
begin
    select ID, file, didascalia from immagine where immagine.ID_disco = ID_disco;
end $



-- funzionalità 40
-- rimozione immagini disco
create procedure remove_immagine_disco(in ID_immagine integer)
begin
    delete from immagine where immagine.ID = ID_immagine;
end $


-- funzionalità 41
-- aggiunta immagine disco
create procedure add_immagine_disco(in file varchar(255), in didascalia varchar(255),in ID_disco integer)
begin
    insert into immagine(ID_disco, file, didascalia) values (ID_disco, file, didascalia);
end $

delimiter ;

DROP TRIGGER IF EXISTS Update_Duplicati_On_Insert_Comprende_Dischi;
DROP TRIGGER IF EXISTS Update_Duplicati_On_Delete_Comprende_Dischi;
DROP TRIGGER IF EXISTS Update_Duplicati_On_Update_Comprende_Dischi;
DROP TRIGGER IF EXISTS Check_Anno_Uscita_Inserimento_Disco;
DROP TRIGGER IF EXISTS Check_Anno_Uscita_Aggiornamento_Disco;



use collectors;
delimiter $

/* Sezione Update duplicati */


# create trigger Update_Duplicati_On_Insert_Comprende_Dischi
#     after insert
#     on Comprende_Dischi
#     for each row
# begin
#     call gestione_disco(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
# end$
#
# create trigger Update_Duplicati_On_Delete_Comprende_Dischi
#     after delete
#     on Comprende_Dischi
#     for each row
# begin
#     call gestione_disco(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
# end$
#
# create trigger Update_Duplicati_On_Update_Comprende_Dischi
#     after update
#     on Comprende_Dischi
#     for each row
# begin
#     if OLD.ID_collezione = NEW.ID_collezione and OLD.ID_disco <> NEW.ID_disco then
#         call gestione_disco(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
#         call gestione_disco(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
#     end if;
# end$

-- Sezione di verifica dell'anno di uscita dei dischi all'inserimento e all'aggiornamento di un disco

create trigger Check_Anno_Uscita_Inserimento_Disco
    before insert
    on disco
    for each row
begin
    if NEW.anno_uscita > year(current_date) or NEW.anno_uscita < 1900 then
        signal sqlstate '45000' set message_text = 'La data inserita non è valida';
    end if;
end$

create trigger Check_Anno_Uscita_Aggiornamento_Disco
    before update
    on disco
    for each row
begin
    if NEW.anno_uscita > year(current_date) or NEW.anno_uscita < 1900 then
        signal sqlstate '45000' set message_text = 'La data inserita non è valida';
    end if;
end$

delimiter ;

use collectors;


insert into collezionista(ID, email, nickname)
VALUES
    (1,'rossidiana@gmail.com', 'Diana'),
    (2,'napoliteam@outlook.com', 'TeamNapoli'),
    (3,'loacker@temp232.it', 'SupaHacka'),
    (4,'AloneGotou@gmail.jp','GuitarHero');

insert into collezione(ID, nome, flag, ID_collezionista)
VALUES
    (1,'Dr.Stone OST', 1, 1), /* Mr.Why -> Dr.Stone OST*/
    (5,'Main playlist',0, 1) /* Mr.Why -> Main playlist*/,
    (2,'Canzoni', 1, 2), /* TeamNapoli -> Canzoni*/
    (6,'House', 0, 2), /* TeamNapoli -> House*/
    (3,'Anime', 0, 3), /* SupaHacka -> Anime*/
    (4,'kessoku', 0, 4); /* GuitarHero -> kessoku */


/*condivisioni*/
insert into condivide(ID_collezione, ID_collezionista)
VALUES
    (2, 4), /*Canzoni -> GuitarHero*/
    (3, 1); /*Anime -> Mr.Why*/

INSERT INTO etichetta(ID, nome, sede_legale, email)
VALUES
    (1, 'Aniplex', 'Tokyo', 'Aniplex.business@aho.jp'),
    (2, 'NCS', 'Oslo', 'nocopyrightsongs@support.com'),
    (3, 'TMS', 'Tokyo', 'TMSCompany@jld.com');

INSERT INTO genere(ID, nome) VALUES (1,'OST'),(2,'Rock'),(3,'Progressive Electronic');

INSERT INTO disco(ID, titolo, anno_uscita, barcode, formato, stato_conservazione, descrizione_conservazione, ID_etichetta, ID_genere)
VALUES
    (1,'kessoku bando album', '2022', '92999282', 'digitale', null, null, 1, 2),
    (2,'Dr. Stone Original Soundtrack', '2019', '2767236', 'cd', 'come nuovo', null, 3, 1),
    (3,'Dr. Stone Original Soundtrack 2', '2021', '274829', 'vinile', 'ottimo', 'ripescato dal mare', 3, 1),
    (4, 'Dr. Stone Original Soundtrack 3', '2023', '72483', 'digitale', 'accettabile', 'non completo', 3, 1),
    (5, 'Tobu collection', '2023', '243432', 'digitale', null, null, 2, 3),
    (6, 'Janji', '2022', '378465783', 'digitale', null,null, 2, 3);




/*TODO associare Immagini ai dischi*/

/*Collezione-Dischi*/
INSERT INTO comprende_dischi(ID_collezione, ID_disco)
VALUES
    (1,2), /*Dr. Stone ost -> Dr. Stone Original Soundtrack*/
    (1,3), /*Dr. Stone ost -> Dr. Stone Original Soundtrack 2*/
    (1,4), /*Dr. Stone ost -> Dr. Stone Original Soundtrack 3*/
    (2,5), /*Canzoni -> Tobu collection*/
    (2,6), /*Canzoni -> Janji*/
    (2,1), /*Canzoni -> kessoku bando album*/
    (2,2), /*Canzoni -> Dr. Stone Original Soundtrack*/
    (2,3), /*Canzoni -> Dr. Stone Original Soundtrack 2*/
    (3,1), /*Anime -> kessoku bando album*/
    (3,2), /*Anime -> Dr. Stone Original Soundtrack*/
    (3,3), /*Anime -> Dr. Stone Original Soundtrack 2*/
    (3,4), /*Anime -> Dr. Stone Original Soundtrack 3*/
    (4,1), /*kessoku -> kessoku bando album*/
    (5,2), /*Main playlist -> Dr. Stone Original Soundtrack*/
    (5,3), /*Main playlist -> Dr. Stone Original Soundtrack 2*/
    (5,4), /*Main playlist -> Dr. Stone Original Soundtrack 3*/
    (5,5), /*Main playlist -> Tobu collection*/
    (5,6), /*Main playlist -> Janji*/
    (6,5), /*House -> Tobu collection*/
    (6,6); /*House -> Janji*/


/*Collezionista-dischi(duplicati)*/
INSERT INTO colleziona_dischi(ID_collezionista, ID_disco, numero_duplicati)
VALUES
    (1,2,1), /*Mr.Why -> Dr. Stone Original Soundtrack*/
    (1,3,1), /*Mr. Why -> Dr. Stone Original Soundtrack 2*/
    (1,4,1), /*Mr.Why -> Dr. Stone Original Soundtrack 3*/
    (1,5,0), /*Mr.Why -> Tobu collection*/
    (1,6,0), /*Mr.Why -> Janji*/
    (2,2,0), /*TeamNapoli -> Dr. Stone Original Soundtrack*/
    (2,3,0), /*TeamNapoli -> Dr. Stone Original Soundtrack 2*/
    (2,1,0), /*TeamNapoli -> kessoku*/
    (2,5,1), /*TeamNapoli -> Tobu collection*/
    (2,6,1), /*TeamNapoli -> Janji*/
    (3,2,0), /*SupaHacka -> Dr. Stone Original Soundtrack*/
    (3,3,0), /*SupaHacka -> Dr. Stone Original Soundtrack 2*/
    (3,4,0), /*SupaHacka -> Dr. Stone Original Soundtrack 3*/
    (3,1,0), /*SupaHacka -> kessoku*/
    (4,1,0); /* GuitarHero -> kessoku bando album*/
    /*ha 2 dischi di ognuno*/


INSERT INTO autore(ID, nome, cognome, data_nascita, nome_autore, info, ruolo)
VALUES
    (1, 'Hitori', 'Gotou', '2004-02-21', 'GuitarHero', null, 'poliedrico'),
    (2, 'Nijika', 'Ijichi','2004-05-29', 'Nijika', null, 'poliedrico'),
    (3, 'Ryo', 'Yamada', '2002--09-18', 'Ryo', null, 'poliedrico'),
    (4, 'Kita', 'Ikuyo', '2003-04-21', 'Kita', 'la chitarra scappata', 'poliedrico'),
    (5, null, null, '1988-12-23', 'Tobu', null, 'poliedrico'),
    (6, null, null, '1997-10-05', 'Janji', null, 'poliedrico'),
    (7, null, null, '2007-01-06', 'BURNOUT SYNDROMES', null, 'poliedrico');

/*Autore-Disco*/
INSERT INTO produce_disco(ID_autore, ID_disco)
VALUES
    (1,1),
    (2,1),
    (3,1),
    (4,1),
    (7,2),
    (7,3),
    (7,4),
    (5,5),
    (6,6);

INSERT INTO traccia(ID, titolo, durata, ID_disco)
VALUES
    (1, 'Seishun Complex', '0:3:23',1),
    (2, 'Hitoribocchi Tokyo', '0:3:52',1),
    (3, 'Distortion!!', '0:3:23',1),
    (4, 'Secret Base', '0:3:52',1),
    (5, 'Guitar, Loneliness and Blue Planet', '0:3:48',1),
    (6, 'I Can''t Sing a Love Song', '0:3:08',1),
    (7, 'That Band', '0:3:33',1),
    (8, 'Karakara', '0:4:25',1),
    (9, 'The Little Sea', '0:3:43',1),
    (10, 'What Is Wrong With', '0:3:47',1),
    (11, 'Never Forget', '0:3:43',1),
    (12, 'If I Could Be a Constellation', '0:4:18',1),
    (13, 'Flashbacker', '0:4:35',1),
    (14, 'Rockn'' Roll, Morning Light Falls on You', '0:4:31',1),
    (15, 'STONE WORLD', '0:3:27',2),
    (16, 'Turned To Stone', '0:1:28',2),
    (17, 'Beginning', '0:2:39',2),
    (18, 'From Zero', '0:2:38',2),
    (19, 'Trash is a Treasure', '0:2:42',3),
    (20, 'Won\'t Give Up', '0:2:15',3),
    (21, 'One Small Step', '0:4:11',3),
    (22, 'Wake Up, Senku!!', '0:1:41',3),
    (23, 'Senku\'s Story', '0:4:35',4),
    (24, 'Spinning Gears', '0:2:45',4),
    (25, 'Kohaku VS Homura', '0:2:32',4),
    (26, 'Connecting Dots', '0:2:24',4),
    (27, 'Caelum', '0:3:23',5),
    (28, 'Colors', '0:3:52',5),
    (29, 'Candyland', '0:3:23',5),
    (30, 'Cloud 9', '0:3:52',5),
    (31, 'Shadows', '0:3:52',6),
    (32, 'Dawn', '0:3:29',6),
    (33, 'Aurora', '0:4:22',6),
    (34, 'Horizon', '0:3:17',6);

/*Autore-Traccia*/
INSERT INTO realizza_traccia(ID_autore, ID_traccia)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (1, 2),
    (3, 2),
    (2, 3),
    (3, 3),
    (1, 4),
    (3, 4),
    (1, 5),
    (4, 5),
    (3, 6),
    (3, 7),
    (2, 8),
    (4, 9),
    (3, 10),
    (4, 10),
    (2, 11),
    (1, 11),
    (3, 12),
    (4, 12),
    (1, 13),
    (3, 13),
    (1, 14),
    (7, 15),
    (7, 16),
    (7, 17),
    (7, 18),
    (7, 19),
    (7, 20),
    (7, 21),
    (7, 22),
    (7, 23),
    (7, 24),
    (7, 25),
    (7, 26),
    (5, 27),
    (5, 28),
    (5, 29),
    (5, 30),
    (6, 31),
    (6, 32),
    (6, 33),
    (6, 34);