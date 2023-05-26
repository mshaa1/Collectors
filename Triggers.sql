USE collectors;
DELIMITER $

/* Sezione Update duplicati */

CREATE TRIGGER Update_Duplicati_On_Insert_Comprende_Dischi
    AFTER INSERT
    ON Comprende_Dischi
    FOR EACH ROW
BEGIN
    CALL Aggiornamento_Duplicati(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
END$

CREATE TRIGGER Update_Duplicati_On_Delete_Comprende_Dischi
    AFTER DELETE
    ON Comprende_Dischi
    FOR EACH ROW
BEGIN
    CALL Aggiornamento_Duplicati(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
END$

CREATE TRIGGER Update_Duplicati_On_Update_Comprende_Dischi
    AFTER UPDATE
    ON Comprende_Dischi
    FOR EACH ROW
BEGIN
    IF OLD.ID_collezione = NEW.ID_collezione AND OLD.ID_disco <> NEW.ID_disco THEN
        CALL Aggiornamento_Duplicati(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
        CALL Aggiornamento_Duplicati(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
    END IF;
END$