use collectors;
insert into collezionista(ID, email, nickname)
VALUES
    (1,'whywhywhy@gmail.com', 'Mr.Why'),
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

INSERT INTO disco(ID, titolo, anno_uscita, barcode, fomato, stato_conservazione, descrizione_conservazione, ID_etichetta, ID_genere)
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
    (2,5,1), /*TeamNapoli -> Tobu collection*/
    (2,6,1), /*TeamNapoli -> Janji*/
    (2,2,1), /*TeamNapoli -> Dr. Stone Original Soundtrack*/
    (2,3,1); /*TeamNapoli -> Dr. Stone Original Soundtrack 2*/
    /*ha 2 dischi di ognuno*/


INSERT INTO autore(ID, nome, cognome, data_nascita, nome_autore, info, ruolo)
VALUES
    (1, 'Hitori', 'Gotou', '2004-02-21', 'GuitarHero', null, 'poliedrico'),
    (2, 'Nijika', 'Ijichi','2004-05-29', 'Nijika', null, 'poliedrico'),
    (3, 'Ryo', 'Yamada', '2002--09-18', 'Ryo', null, 'poliedrico'),
    (4, 'Kita', 'Ikuyo', '2003-04-21', 'Kita', 'la chitarra scappata', 'poliedrico'),
    (5, 'nome', 'inventato', '1988-12-23', 'Tobu', null, 'poliedrico'),
    (6, 'nome', 'inventato', '1997-10-05', 'Janji', null, 'poliedrico'),
    (7, 'nome', 'inventato', '2007-01-06', 'BURNOUT SYNDROMES', null, 'poliedrico');

/*Autore-Disco*/
INSERT INTO produce_disco(ID_autore, ID_disco)
VALUES
    (1,1),
    (2,1),
    (3,1),
    (4,1),
    (7,3),
    (7,3),
    (7,4),
    (5,5),
    (6,6);

INSERT INTO traccia(ID, titolo, durata)
VALUES
    (1, 'Seishun Complex', '0:3:23'),
    (2, 'Hitoribocchi Tokyo', '0:3:52'),
    (3, 'Distortion!!', '0:3:23'),
    (4, 'Secret Base', '0:3:52'),
    (5, 'Guitar, Loneliness and Blue Planet', '0:3:48'),
    (6, 'I Can''t Sing a Love Song', '0:3:08'),
    (7, 'That Band', '0:3:33'),
    (8, 'Karakara', '0:4:25'),
    (9, 'The Little Sea', '0:3:43'),
    (10, 'What Is Wrong With', '0:3:47'),
    (11, 'Never Forget', '0:3:43'),
    (12, 'If I Could Be a Constellation', '0:4:18'),
    (13, 'Flashbacker', '0:4:35'),
    (14, 'Rockn'' Roll, Morning Light Falls on You', '0:4:31'),
    (15, 'STONE WORLD', '0:3:27'),
    (16, 'Turned To Stone', '0:1:28'),
    (17, 'Beginning', '0:2:39'),
    (18, 'From Zero', '0:2:38'),
    (19, 'Trash is a Treasure', '0:2:42'),
    (20, 'Won\'t Give Up', '0:2:15'),
    (21, 'One Small Step', '0:4:11'),
    (22, 'Wake Up, Senku!!', '0:1:41'),
    (23, 'Senku\'s Story', '0:4:35'),
    (24, 'Spinning Gears', '0:2:45'),
    (25, 'Kohaku VS Homura', '0:2:32'),
    (26, 'Connecting Dots', '0:2:24'),
    (27, 'Caelum', '0:3:23'),
    (28, 'Colors', '0:3:52'),
    (29, 'Candyland', '0:3:23'),
    (30, 'Cloud 9', '0:3:52'),
    (31, 'Shadows', '0:3:52'),
    (32, 'Dawn', '0:3:29'),
    (33, 'Aurora', '0:4:22'),
    (34, 'Horizon', '0:3:17');

/*Disco-Traccia*/
INSERT INTO contiene_tracce(ID_disco, ID_traccia)
VALUES
    (1,1),
    (1,2),
    (1,3),
    (1,4),
    (1,5),
    (1,6),
    (1,7),
    (1,8),
    (1,9),
    (1,10),
    (1,11),
    (1,12),
    (1,13),
    (1,14),
    (2,15),
    (2,16),
    (2,17),
    (2,18),
    (3,19),
    (3,20),
    (3,21),
    (3,22),
    (4,22),
    (4,24),
    (4,25),
    (4,26),
    (5,27),
    (5,27),
    (5,29),
    (5,30),
    (6,31),
    (6,32),
    (6,33),
    (6,34);

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