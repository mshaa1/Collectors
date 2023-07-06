use collectors;
drop procedure if exists inserisci_collezione;
drop procedure if exists inserisci_disco_collezione;
drop procedure if exists inserisci_tracce_disco;
drop procedure if exists modifica_flag_collezione;
drop procedure if exists inserisci_condivisione;
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
delimiter $

-- 1
-- inserimento di una nuova collezione.
create procedure inserisci_collezione(in nome varchar(25), in flag boolean, in ID_collezionista integer)
begin
    insert into collezione (nome, flag, ID_collezionista)
    values (nome, flag, ID_collezionista); -- notare che possono essere create anche collezioni con stesso nome
end$


-- 2
-- aggiunta di dischi a una collezione
create procedure inserisci_disco_collezione(in ID_disco integer, in ID_collezione integer)
begin
    insert into comprende_dischi(ID_disco, ID_collezione) values (ID_disco, ID_collezione);
end$


-- 3
-- aggiunta di tracce a un disco
create procedure inserisci_tracce_disco(in ID_disco integer, in titolo varchar(35), in durata time)
begin
    insert into traccia(titolo, durata, ID_disco) values (titolo, durata, ID_disco);
end$


-- 4
-- Modifica dello stato di pubblicazione di una collezione
create procedure modifica_flag_collezione(in ID_collezione integer, in flag boolean)
begin
    update collezione set collezione.flag = flag where ID_collezione = ID; -- flag = 0 - collezione pubblica | flag = 1 - collezione privata
end$


-- 5
-- Aggiunta di nuove condivisioni a una collezione
create procedure inserisci_condivisione(in ID_collezione integer, in ID_collezionista integer)
begin
    insert into condivide(ID_collezione, ID_collezionista) values (ID_collezione, ID_collezionista);
end$


-- Rimozione di un disco da una collezione
create procedure rimozione_disco_collezione(in ID_disco integer, in ID_collezione integer)
begin
    delete from comprende_dischi c where c.ID_disco = ID_disco and c.ID_collezione = ID_collezione;
end$


-- Rimozione di una collezione.
create procedure rimozione_collezione(in ID_collezione integer)
begin
    delete from collezione where ID = ID_collezione;
end$


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


-- Tracklist di un disco
create procedure tracklist_disco(in ID_disco integer)
begin
    select titolo, durata
    from traccia
    where traccia.ID_disco = ID_disco;
end$

/*
-- 8
create procedure ricerca_dischi_per_titolo_autore(in titolo varchar(35), in nome_autore varchar(25),
                                                  in esattamente boolean,
                                                  out risultati_presenti boolean) -- esattamente = true -> fa l'and | esattamente = false -> fa l'or
-- risultati_presenti = 0 se non può essere fatta alcuna ricerca
begin
    case
        when titolo IS NULL and nome_autore IS NOT NULL -- cerco solo autore
            then select lista_dischi.titolo,
                        'anno di uscita',
                        genere,
                        formato,
                        stato_conservazione,
                        descrizione_conservazione,
                        barcode,
                        azienda,
                        sede_legale,
                        email
                 from lista_dischi
                          join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                          join autore on produce_disco.ID_autore = autore.ID
                 where lower(autore.nome_autore) = lower(nome_autore);
        when titolo IS NOT NULL and nome_autore IS NULL -- cerco solo il titolo
            then select lista_dischi.titolo,
                        'anno di uscita',
                        genere,
                        formato,
                        stato_conservazione,
                        descrizione_conservazione,
                        barcode,
                        azienda,
                        sede_legale,
                        email
                 from lista_dischi
                 where lower(lista_dischi.titolo) = lower(titolo);
        when titolo IS NOT NULL and nome_autore IS NOT NULL -- cerca entrambi
            then if (esattamente = 1) -- cerca entrambi in modo da avere per ogni riga esattamente il titolo e l'autore
            then
                select lista_dischi.titolo,
                       'anno di uscita',
                       genere,
                       formato,
                       stato_conservazione,
                       descrizione_conservazione,
                       barcode,
                       azienda,
                       sede_legale,
                       email
                from lista_dischi
                         join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                         join autore on produce_disco.ID_autore = autore.ID
                where lower(lista_dischi.titolo) = lower(titolo)
                  and lower(autore.nome_autore) = lower(nome_autore);
            else -- cerca entrambi in modo da avere per ogni riga o il titolo o l'autore
                select lista_dischi.titolo,
                       'anno di uscita',
                       genere,
                       formato,
                       stato_conservazione,
                       descrizione_conservazione,
                       barcode,
                       azienda,
                       sede_legale,
                       email
                from lista_dischi
                         join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                         join autore on produce_disco.ID_autore = autore.ID
                where lower(lista_dischi.titolo) = lower(titolo)
                   or lower(autore.nome_autore) = lower(nome_autore);
            end if;
        end case;
end$ */

-- 9
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


-- 10
-- numero di tracce di dischi distinti di un certo autore presenti nelle collezioni pubbliche

create procedure numero_tracce_distinte_per_autore_collezioni_pubbliche(in nome_autore varchar(25), out numero_tracce integer)
begin
    select count(distinct traccia.ID)
    from traccia
             join disco on traccia.ID_disco = disco.ID
             join produce_disco on disco.ID = produce_disco.ID_disco
             join autore on produce_disco.ID_autore = autore.ID
             join comprende_dischi on disco.ID = comprende_dischi.ID_disco
             join collezione on comprende_dischi.ID_collezione = collezione.ID
    where lower(autore.nome_autore) = lower(nome_autore)
      and collezione.flag = 1;
end$


-- 11
-- minuti totali di musica riferibili a un certo autore memorizzati nelle collezioni pubbliche

create function minuti_totali_musica_pubblica_per_autore(nome_autore varchar(25))
    returns time
    deterministic
begin
    return (select sum(traccia.durata)
            from traccia
                     join disco on traccia.ID_disco = disco.ID
                     join produce_disco on disco.ID = produce_disco.ID_disco
                     join autore on produce_disco.ID_autore = autore.ID
                     join comprende_dischi on disco.ID = comprende_dischi.ID_disco
                     join collezione on comprende_dischi.ID_collezione = collezione.ID
            where lower(autore.nome_autore) = lower(nome_autore)
              and collezione.flag = 1);
end$


-- 12a
-- statistiche: numero collezioni di ciascun collezionista

create procedure statistiche_numero_collezioni()
begin
    select collezionista.nickname, count(all collezione.ID)
    from collezione
             join collezionista on collezione.ID_collezionista = collezionista.ID
    group by nickname;
end$


-- 12b
-- statistiche: numero di dischi per genere nel sistema

create procedure statistiche_dischi_per_genere()
begin
    select count(all disco.id), genere.nome
    from disco
             join genere on disco.ID_genere = genere.ID
    group by nome;
end$


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

-- Query 8
create view lista_dischi_generale as
    select c.nome, c1.nickname, d.*, a.nome_autore, c.flag, ID_collezionista
                from disco d
                         join produce_disco pd on d.ID = pd.ID_disco
                         join autore a on pd.ID_autore = a.ID
                         join comprende_dischi cd on d.ID = cd.ID_disco
                         join collezione c on cd.ID_collezione = c.ID
                         join collezionista c1 on ID_collezionista = c1.ID;


create procedure ricerca_dischi_per_autore_titolo(in nome_autore varchar(25), in titolo_disco varchar(50),
                                                  in ID_collezionista int, in flag boolean)
begin
    select nickname as 'proprietario collezione',
            titolo, anno_uscita as 'anno di uscita', formato, stato_conservazione as 'stato di conservazione',
            lUl.nome_autore as 'nome autore'
    from (
        select *
          from lista_dischi_generale l
          where
                l.titolo like titolo_disco

          union

        select *
          from lista_dischi_generale l

            where l.nome_autore like nome_autore
    ) as `lUl`
    where lUl.flag <> (not flag) and lUl.ID_collezionista = ID_collezionista;
end$

-- Altre query

-- Aggiunta Autore
create procedure aggiunta_autore(in nome varchar(25), in cognome varchar(25), in data_nascita date, in nome_autore varchar(25), in info varchar(255), in ruolo varchar(25))
begin
       insert into autore(nome, cognome, data_nascita, nome_autore, info, ruolo) values (nome, cognome, data_nascita, nome_autore, info, ruolo);
end$

create procedure aggiunta_genere(in nome varchar(25))
begin
       insert into genere(nome) values (nome);
end$



delimiter ;




