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
