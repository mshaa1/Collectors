use collectors;
drop procedure if exists inserisci_collezione;
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
drop procedure if exists numero_tracce_distinte_per_autore_collezioni_pubbliche;
drop function if exists minuti_totali_musica_pubblica_per_autore;
drop procedure if exists statistiche_dischi_per_genere;
drop procedure if exists statistiche_numero_collezioni;
drop procedure if exists ricerca_dischi_per_autore_titolo;
drop view if exists lista_dischi_generale;
drop procedure if exists ricerca_dischi_per_titolo_autore;

delimiter $

-- Funzionalità 1
-- inserimento di una nuova collezione.
create procedure inserisci_collezione(in nome varchar(25), in flag boolean, in ID_collezionista integer)
begin
    insert into collezione (nome, flag, ID_collezionista)
    values (nome, flag, ID_collezionista); -- notare che possono essere create anche collezioni con stesso nome
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
    update collezione set collezione.flag = flag where ID_collezione = ID; -- flag = 0 - collezione pubblica | flag = 1 - collezione privata
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
    select titolo,
           'anno di uscita',
           genere,
           formato,
           stato_conservazione,
           descrizione_conservazione,
           barcode,
           azienda,
           sede_legale,
           email
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
                                                  in flag boolean, in ID_collezionista int)
begin
    select distinct -- nickname as 'proprietario collezione',
            ID, titolo, anno_uscita as 'anno di uscita', barcode, formato, stato_conservazione as 'stato di conservazione', descrizione_conservazione as 'descrizione conservazione'
            -- lUl.nome_autore as 'nome autore'
    from (
        select *
          from lista_dischi_generale l
            where
                l.titolo like titolo_disco -- ricerca per titolo del disco

          union -- unione per ottenere entrambe le ricerche

        select *
          from lista_dischi_generale l
            where l.nome_autore like nome_autore -- ricerca per nome autore
    ) as `lUl`
    where (lUl.flag=flag or flag is null) and lUl.ID_collezionista = ID_collezionista; -- ricerca per flag e costraint del collezionista
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
          and c.ID_collezionista = ID_collezionista) = 1 then
        return true;
    end if;
    return false;
end$


-- Funzionalità 10
-- numero di tracce di dischi distinti di un certo autore presenti nelle collezioni pubbliche

create procedure numero_tracce_distinte_per_autore_collezioni_pubbliche(in ID_autore integer, out numero_tracce integer)
begin
    select count(distinct traccia.ID) into numero_tracce
    from traccia
             join disco on traccia.ID_disco = disco.ID
             join produce_disco on disco.ID = produce_disco.ID_disco
             join autore on produce_disco.ID_autore = autore.ID
             join comprende_dischi on disco.ID = comprende_dischi.ID_disco
             join collezione on comprende_dischi.ID_collezione = collezione.ID
    where lower(autore.nome_autore) = lower(nome_autore)
      and collezione.flag = 1;
end$


-- Funzionalità 11
-- Minuti totali di musica riferibili a un certo autore memorizzati nelle collezioni pubbliche
#TODO: capire come funziona la somma dei tipi time
create function minuti_totali_musica_pubblica_per_autore(ID_autore integer)
    returns integer
    deterministic
begin
    return (select sum(traccia.durata)/60
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

-- da qui seguono procedure aggiuntive

-- Funzionalità 14
-- Gestione aggiornamento / cancellazione disco (e duplicati)

create procedure gestione_disco(in IN_ID_Collezione integer, in IN_ID_Disco integer,
                                in tipo_aggiornamento enum ('DELETE','INSERT'))
begin
    select ID_collezionista from collezione where ID = IN_ID_Collezione into @ID_collezionista;
    if (tipo_aggiornamento = 'DELETE') then
        update colleziona_dischi
        set numero_duplicati=numero_duplicati - 1
        where colleziona_dischi.ID_disco = IN_ID_Disco
          and colleziona_dischi.ID_collezionista = @ID_collezionista;
        delete from colleziona_dischi where numero_duplicati = 0;
    else
        if (tipo_aggiornamento = 'INSERT')
        then
            -- conto il numero di dischi IN_ID_Disco nella collezione del collezionista @ID_collezionista
            select count(all ID_disco) -- TODO: controllare se effettivamente è un all o se è un distinct
            from comprende_dischi
                     join collezione on ID_collezione = ID
            where ID_collezionista = @ID_collezionista
              and ID_disco = IN_ID_Disco
            into @numero_dischi;
            case
                when @numero_dischi = 2 -- è stata inserita la prima copia
                    then insert into colleziona_dischi(ID_collezionista, ID_disco, numero_duplicati)
                         values (@ID_collezionista, IN_ID_Disco, 1);
                when @numero_dischi > 2 -- è stata inserita la n-esima copia
                    then update colleziona_dischi
                         set numero_duplicati=numero_duplicati + 1
                         where colleziona_dischi.ID_disco = IN_ID_Disco
                           and colleziona_dischi.ID_collezionista = @ID_collezionista;
                else select null into @nullvar; -- è stato inserito il primo disco, quindi non fare nulla
                end case;
        end if;
    end if;
end$



-- *****  Altre query per il funzionamento dell' applicazione *****

drop procedure if exists inserisci_condivisione;
-- Funzionalità 15
-- Aggiunta di nuove condivisioni a una collezione'

create procedure inserisci_condivisione(in ID_collezione integer, in ID_collezionista integer)
begin
    insert into condivide(ID_collezione, ID_collezionista) values (ID_collezione, ID_collezionista);
end$

drop procedure if exists aggiunta_autore;

-- Funzionalità 16
-- aggiunta di un autore

create procedure aggiunta_autore(in nome varchar(25), in cognome varchar(25), in data_nascita date, in nome_autore varchar(25), in info varchar(255), in ruolo varchar(25))
begin
       insert into autore(nome, cognome, data_nascita, nome_autore, info, ruolo) values (nome, cognome, data_nascita, nome_autore, info, ruolo);
end$

drop procedure if exists aggiunta_genere;

-- Funzionalità 17
-- aggiunta di un genere

create procedure aggiunta_genere(in nome varchar(25))
begin
       insert into genere(nome) values (nome);
end$

drop procedure if exists rimozione_genere;

-- Funzionalità 18
-- rimozione di un genere
create procedure rimozione_genere(in ID_genere int)
begin
       delete from genere where ID_genere = ID;
end$

drop procedure if exists convalida_utente;

-- Funzionalità 19
-- convalida l'accesso di un utente

create procedure convalida_utente(in nickname varchar(25), in email varchar(25), out flag boolean)
begin
    select count(*) from collezionista where collezionista.nickname = nickname and collezionista.email = email into flag;
end$

drop procedure if exists registrazione_utente;

-- Funzionalità 20
-- Registrazione utente
create procedure registrazione_utente(in nickname varchar(25), in email varchar(25))
begin
    insert into collezionista(nickname, email) values (nickname, email);
end$

drop procedure if exists prendi_ID_utente;

-- Funzionalità 21
-- ottenimento ID utente per l'interfacciamento con Java

create procedure prendi_ID_utente(in nickname varchar(25), in email varchar(25), out ID integer)
begin
    select collezionista.ID from collezionista where collezionista.nickname = nickname and collezionista.email = email into ID;
end$

drop procedure if exists get_collezioni_utente;

-- Funzionalità 22
-- prendo tutte le collezioni di un utente

create procedure get_collezioni_utente(in ID_utente integer)
begin
    select * from collezione where collezione.ID_collezionista = ID_utente;
end$

-- Funzionalità 23
-- tutti i dichi di un utente con etichetta

drop procedure if exists get_dischi_utente_etichetta;

create procedure get_dischi_utente_etichetta(in ID_utente integer)
begin
    select distinct disco.ID, titolo, anno_uscita, barcode, formato, stato_conservazione, descrizione_conservazione, etichetta.ID as etichetta_id, nome, sede_legale, email
    from disco
    join colleziona_dischi on disco.ID = colleziona_dischi.ID_disco
    join etichetta on disco.ID_etichetta = etichetta.ID
    where colleziona_dischi.ID_collezionista = ID_utente;
end $


-- Funzionalità 24
-- etichetta di un disco

drop procedure if exists get_etichetta_disco;

create procedure get_etichetta_disco(in ID_disco integer)
begin
    select *
    from etichetta
    join disco on disco.ID_etichetta = etichetta.ID
    where disco.ID = ID_disco;
end $

drop procedure if exists statistiche_numero_collezioni_collezionista;

-- Funzionalità 25
-- numero di collezioni di un collezionista
create procedure statistiche_numero_collezioni_collezionista(in ID_collezionista integer)
    begin
        select count(all collezione.ID) from collezione where collezione.ID_collezionista = ID_collezionista;
    end $

-- Funzionalità 26
-- get tutti autori

drop procedure if exists get_autori;

create procedure get_autori()
begin
    select * from autore;
end $

-- Funzionalità 27
-- get genere di un disco

drop procedure if exists get_genere_disco;

create procedure get_genere_disco(in ID_disco integer)
begin
    select genere.ID, genere.nome
    from genere
    join disco on disco.ID_genere = genere.ID
    where disco.ID = ID_disco;
end $


-- Funzionalità 28
-- tutti i dischi di un utente

drop procedure if exists get_dischi_utente;

create procedure get_dischi_utente(in ID_utente integer)
begin
    select distinct *
    from disco
    join colleziona_dischi on disco.ID = colleziona_dischi.ID_disco
    where colleziona_dischi.ID_collezionista = ID_utente;
end $

-- Funzionalità 29
-- get autore di un disco

drop procedure if exists get_autore_disco;

create procedure get_autori_disco(in ID_disco integer)
begin
    select *
    from disco d
    join produce_disco on ID=produce_disco.ID_disco
    join autore a on ID_autore=a.ID
    where d.ID=ID_disco;
end $

delimiter ;





