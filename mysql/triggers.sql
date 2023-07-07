DROP TRIGGER IF EXISTS Update_Duplicati_On_Insert_Comprende_Dischi;
DROP TRIGGER IF EXISTS Update_Duplicati_On_Delete_Comprende_Dischi;
DROP TRIGGER IF EXISTS Update_Duplicati_On_Update_Comprende_Dischi;
DROP TRIGGER IF EXISTS Check_Anno_Uscita_Inserimento_Disco;
DROP TRIGGER IF EXISTS Check_Anno_Uscita_Aggiornamento_Disco;



use collectors;
delimiter $

/* Sezione Update duplicati */

create trigger Update_Duplicati_On_Insert_Comprende_Dischi
    after insert
    on Comprende_Dischi
    for each row
begin
    call gestione_disco(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
end$

create trigger Update_Duplicati_On_Delete_Comprende_Dischi
    after delete
    on Comprende_Dischi
    for each row
begin
    call gestione_disco(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
end$

create trigger Update_Duplicati_On_Update_Comprende_Dischi
    after update
    on Comprende_Dischi
    for each row
begin
    if OLD.ID_collezione = NEW.ID_collezione and OLD.ID_disco <> NEW.ID_disco then
        call gestione_disco(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
        call gestione_disco(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
    end if;
end$

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