use collectors;

delimiter $

-- 1
-- inserimento di una nuova collezione.
create procedure inserisci_collezione(in nome varchar(25), in flag boolean, in ID_collezionista integer)
begin
    insert into collezione (nome, flag, ID_collezionista) values (nome, flag, ID_collezionista); -- notare che possono essere create anche collezioni con stesso nome
end$

-- 2
-- aggiunta di dischi a una collezione
create procedure inserisci_disco_collezione(in ID_disco integer, in ID_collezione integer)
begin
    insert into comprende_dischi(ID_disco, ID_collezione) values (ID_disco, ID_collezione);
end$

-- 3
-- aggiunta di tracce a un disco
-- TODO modificare, contiene_tracce non esiste più
create procedure inserisci_tracce_disco(in ID_disco integer, in ID_traccia integer)
begin
    insert into contiene_tracce(ID_disco, ID_traccia) values (ID_disco, ID_disco)
end$


-- 4
-- Modifica dello stato di pubblicazione di una collezione
create procedure modifica_flag_collezione(in ID_collezione integer, in in_flag boolean)
begin
    update collezione set collezione.flag = in_flag where ID_collezione = ID; -- flag = 0 - collezione pubblica | flag = 1 - collezione privata
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
    select disco.ID, titolo, anno_uscita as 'anno di uscita', genere.nome as genere, formato, stato_conservazione, descrizione_conservazione, barcode, etichetta.nome as azienda, sede_legale, email
        from disco
            join etichetta on disco.ID_etichetta = etichetta.ID
            join genere on disco.ID_genere = genere.ID;

create procedure lista_dischi_collezione(in ID_collezione integer)
begin
    select titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
        from comprende_dischi
            join lista_dischi on ID_disco = lista_dischi.ID
        where comprende_dischi.ID_collezione = ID_collezione;
end$


-- Tracklist di un disco
create procedure tracklist_disco(in ID_disco integer)
begin
    select titolo, durata
        from traccia where traccia.ID_disco = ID_disco;
end$

-- 8
-- TODO TESTARE
create procedure ricerca_dischi_per(in titolo varchar(35), in nome_autore varchar(25), in esattamente boolean, out risultati_presenti boolean) -- esattamente = true -> fa l'and | esattamente = false -> fa l'or
-- risultati_presenti = 0 se non può essere fatta alcuna ricerca
begin
    case
        when titolo = null and nome_autore <> null -- cerco solo autore
            then
                select titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                from lista_dischi
                    join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                    join autore on produce_disco.ID_autore = autore.ID
                where lower(autore.nome_autore) = lower(nome_autore);
        when titolo <> null and nome_autore = null -- cerco solo il titolo
            then
                select titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                from lista_dischi
                where lower(lista_dischi.titolo) = lower(titolo);
        when titolo <> null and nome_autore <> null -- cerca entrambi
            then
                if (esattamente = 1) -- cerca entrambi in modo da avere per ogni riga esattamente il titolo e l'autore
                then
                    select titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                        from lista_dischi
                        join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                        join autore on produce_disco.ID_autore = autore.ID
                    where lower(lista_dischi.titolo) = lower(titolo) and lower(autore.nome_autore) = lower(nome_autore);
                else -- cerca entrambi in modo da avere per ogni riga o il titolo o l'autore
                    select titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                        from lista_dischi
                        join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                        join autore on produce_disco.ID_autore = autore.ID
                    where lower(lista_dischi.titolo) = lower(titolo) or lower(autore.nome_autore) = lower(nome_autore);
                end if;
    end case;
end$

-- 9
-- verifica della visibilità di una collezione da parte di un collezionista

create function verifica_visibilita_collezione(ID_collezione integer, ID_collezionista integer)
    returns boolean deterministic
begin
    if (select flag from collezione where collezione.ID = ID_collezione) = 1 then -- se la collezione ha flag impostato a true allora la collezione è visibile a tutti i collezionisti
        return true;
    end if;
    if (select ID_collezionista from collezione where collezione.ID = ID_collezione and collezione.ID_collezionista=ID_collezionista) then -- se la collezione appartiene al collezionista, allora può visionarla
        return true;
    end if;
    if (select count(*) from condivide c where c.ID_collezione = ID_collezione and c.ID_collezionista = ID_collezionista) = 1 then
        return true;
    end if;
    return false;
end;
end$


delimiter ;