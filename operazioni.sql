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
drop procedure if exists ricerca_dischi_per;
drop view if exists lista_dischi;
drop function if exists verifica_visibilita_collezione;
drop procedure if exists gestione_disco;
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
        when titolo IS NULL and nome_autore IS NOT NULL -- cerco solo autore
            then
                select lista_dischi.titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                from lista_dischi
                    join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                    join autore on produce_disco.ID_autore = autore.ID
                where lower(autore.nome_autore) = lower(nome_autore);
        when titolo IS NOT NULL and nome_autore IS NULL -- cerco solo il titolo
            then
                select lista_dischi.titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                from lista_dischi
                where lower(lista_dischi.titolo) = lower(titolo);
        when titolo IS NOT NULL and nome_autore IS NOT NULL -- cerca entrambi
            then
                if (esattamente = 1) -- cerca entrambi in modo da avere per ogni riga esattamente il titolo e l'autore
                then
                    select lista_dischi.titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
                        from lista_dischi
                        join produce_disco on lista_dischi.ID = produce_disco.ID_disco
                        join autore on produce_disco.ID_autore = autore.ID
                    where lower(lista_dischi.titolo) = lower(titolo) and lower(autore.nome_autore) = lower(nome_autore);
                else -- cerca entrambi in modo da avere per ogni riga o il titolo o l'autore
                    select lista_dischi.titolo, 'anno di uscita', genere, formato, stato_conservazione, descrizione_conservazione, barcode, azienda, sede_legale, email
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
end$


-- Gestione aggiornamento / cancellazione disco (e duplicati)
CREATE PROCEDURE gestione_disco(IN IN_ID_Collezione integer, IN IN_ID_Disco integer,
                                                    IN tipo_aggiornamento enum ('DELETE','INSERT'))
BEGIN
    SELECT ID_collezionista FROM collezione WHERE ID = IN_ID_Collezione INTO @ID_collezionista;
    IF (tipo_aggiornamento = 'DELETE') THEN
        UPDATE colleziona_dischi
        SET numero_duplicati=numero_duplicati - 1
        WHERE colleziona_dischi.ID_disco = IN_ID_Disco
          AND colleziona_dischi.ID_collezionista = @ID_collezionista;
        DELETE FROM colleziona_dischi WHERE numero_duplicati = 0;
    ELSE
        IF (tipo_aggiornamento = 'INSERT')
        THEN
            -- conto il numero di dischi IN_ID_Disco nella collezione del collezionista @ID_collezionista
            SELECT COUNT(ID_disco)
            FROM comprende_dischi
                     JOIN collezione ON ID_collezione = ID
            WHERE ID_collezionista = @ID_collezionista
              AND ID_disco = IN_ID_Disco
            INTO @numero_dischi;
            CASE
                WHEN @numero_dischi = 2 -- è stata inserita la prima copia
                    THEN INSERT INTO colleziona_dischi(ID_collezionista, ID_disco, numero_duplicati)
                         VALUES (@ID_collezionista, IN_ID_Disco, 1);
                WHEN @numero_dischi > 2 -- è stata inserita la n-esima copia
                    THEN UPDATE colleziona_dischi
                         SET numero_duplicati=numero_duplicati + 1
                         WHERE colleziona_dischi.ID_disco = IN_ID_Disco
                           AND colleziona_dischi.ID_collezionista = @ID_collezionista;
                ELSE SELECT NULL INTO @nullvar; -- è stato inserito il primo disco, quindi non fare nulla
                END CASE;
        END IF;
    END IF;
END$

delimiter ;
