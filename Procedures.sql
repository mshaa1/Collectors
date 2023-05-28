USE collectors;
DROP PROCEDURE IF EXISTS Gestione_Disco;
DELIMITER $

CREATE PROCEDURE Gestione_Disco(IN IN_ID_Collezione integer, IN IN_ID_Disco integer,
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

DELIMITER ;