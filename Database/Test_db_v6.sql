

CREATE TABLE Player(
        player_id     bigserial  NOT NULL ,
        player_name   Varchar (25) NOT NULL ,
        player_budget real NOT NULL DEFAULT 2,
        player_state  Bool NOT NULL DEFAULT '1',
        stand_id      bigint ,
        PRIMARY KEY (player_id ) ,
        UNIQUE (player_name ),
        UNIQUE (stand_id)
);
CREATE TABLE Stand(
        stand_id        bigserial   NOT NULL ,
        stand_influence real NOT NULL DEFAULT 2.5,
        stand_sell      real NOT NULL DEFAULT 0,
        player_id       bigint NOT NULL ,
        PRIMARY KEY (stand_id ),
        UNIQUE (player_id)
);

CREATE TABLE Ad_Panel(
        panel_id        bigserial  NOT NULL ,
        panel_influence real NOT NULL DEFAULT 2.1,
        player_id       bigint NOT NULL ,
        PRIMARY KEY (panel_id )
);

CREATE TABLE Coordinates(
        coord_id    bigserial  NOT NULL ,
        latitude    real NOT NULL ,
        longititude real NOT NULL ,
        stand_id bigint,
        panel_id bigint,
        PRIMARY KEY (coord_id ),
        UNIQUE (stand_id),
);

CREATE TABLE Ingredient(
        ingredient_id             bigserial  NOT NULL ,
        ingredient_name           Varchar (25) NOT NULL ,
        ingredient_cost           real NOT NULL ,
        PRIMARY KEY (ingredient_id ) ,
        UNIQUE (ingredient_name )
);

CREATE TABLE Drink(
        drink_id   bigserial   NOT NULL ,
        drink_name Varchar (25) NOT NULL ,
        drink_type_hasAlcool Bool NOT NULL ,
        drink_type_cold      Bool NOT NULL ,
        PRIMARY KEY (drink_id )
);


CREATE TABLE Stock(
        stock_id       bigserial  NOT NULL ,
        day_value int  NOT NULL, 
        stand_id bigint NOT NULL ,
        drink_id bigint NOT NULL ,        
        stock_drink_cost real DEFAULT 0,
        stock_quantity_before Int NOT NULL DEFAULT 0,
        stock_sales int NOT NULL DEFAULT 0,
        PRIMARY KEY (stock_id ),
        UNIQUE(drink_id,stand_id,day_value)
);

CREATE TABLE Contain(
        ingredient_id bigint NOT NULL ,
        drink_id      bigint NOT NULL ,
        PRIMARY KEY (ingredient_id ,drink_id )
);


CREATE TABLE Current_day(
    day_name varchar(255),
    current_hour int,
    current_weather varchar(255),
    current_forecast varchar(255)
);

CREATE TABLE Weather(
        weather_id int NOT NULL,
        weather_name Varchar (25) NOT NULL ,
        PRIMARY KEY (weather_id),
        UNIQUE(weather_name)
);
CREATE TABLE Region(
        region int NOT NULL,
        latitude    real NOT NULL ,
        longititude real NOT NULL ,
        latitude_span    real NOT NULL ,
        longititude_span real NOT NULL ,
        PRIMARY KEY (region_name)
);



ALTER TABLE Player ADD CONSTRAINT FK_Player_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id) ON DELETE CASCADE;
ALTER TABLE Stand ADD CONSTRAINT FK_Stand_player_id FOREIGN KEY (player_id) REFERENCES Player(player_id) ON DELETE CASCADE;
ALTER TABLE Ad_Panel ADD CONSTRAINT FK_Ad_Panel_player_id FOREIGN KEY (player_id) REFERENCES Player(player_id)ON DELETE CASCADE;
ALTER TABLE contain ADD CONSTRAINT FK_contain_ingredient_id FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id) ON DELETE CASCADE;
ALTER TABLE contain ADD CONSTRAINT FK_contain_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id) ON DELETE CASCADE;
ALTER TABLE Stock ADD CONSTRAINT FK_stock_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id) ON DELETE CASCADE;
ALTER TABLE Stock ADD CONSTRAINT FK_stock_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id) ON DELETE CASCADE;
ALTER TABLE Coordinates ADD CONSTRAINT FK_Coordinates_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id) ON DELETE CASCADE;
ALTER TABLE Coordinates ADD CONSTRAINT FK_Coordinates_panel_id FOREIGN KEY (panel_id) REFERENCES Ad_Panel(panel_id) ON DELETE CASCADE;


INSERT INTO Weather (weather_name)
VALUES('rainny'),('cloudy'),('sunny'),('heatwave'),('thunderstorm');

INSERT INTO Region (region,latitude,longititude,latitude_span,longititude_span)
VALUES(1,0,0,10,10);


INSERT INTO Player(player_name)
VALUES('Clément'),
    ('Mouhamadi'),
    ('Aymeric'),
    ('Yanick'),
    ('Daniel'),
    ('Test');

INSERT INTO Stand(player_id)
VALUES(1),
    (2),
    (3),
    (4),
    (5),
     (6);

INSERT INTO Ad_Panel(player_id)
VALUES(1),
    (2),
    (3),
    (4),
    (5),
    (6);


INSERT INTO Coordinates(latitude,longititude,stand_id)
VALUES(2.5,3.1,1),
    (2.4,3.7,2),
    (2.4,3.8,3),
    (2.4,3.9,4),
    (2.4,4.1,5),
    (2.5,4.2,6);
INSERT INTO Coordinates(latitude,longititude,panel_id)
VALUES(2.5,3.9,1),
    (2.7,4.1,2),
    (2.8,4.2,3),
    (2.8,4.1,4),
    (2.8,4.5,5),
    (3.4,2.8,6);

INSERT INTO Ingredient(ingredient_name,ingredient_cost)
    VALUES('Rhum',4.99),
        ('Vodka',4.49),
        ('Gin',4.19),
        ('Tequila',4.69),
        ('Thé',2.5),
        ('Eau',2.5),
        ('Glacon',0.5),
        ('Citron',0.5),
        ('Whisky',3.69),
        ('Coca',0.75);


INSERT INTO Drink(drink_name,drink_type_hasAlcool,drink_type_cold)
    VALUES ('Limonade','0','1'),
            ('Eau','0','0'),
            ('Caipirina','1','1'),
            ('Caipiroska','1','1'),
            ('Thé','0','0');

INSERT INTO contain(ingredient_id,drink_id)
    VALUES(1,3),
            (7,3),
            (8,3),
            (6,2),
            (7,4),
            (8,4),
            (2,4),
            (7,1),
            (8,1),
            (6,1),
            (5,5);


INSERT INTO Stock(day_value,stand_id,drink_id,stock_drink_cost,stock_quantity_before,stock_sales)
    VALUES (1,1,1,5.2,50,25),
            (1,1,2,2.4,50,27),
            (1,2,3,3.7,50,35),
            (1,2,4,4.1,50,28),
            (1,3,1,5.6,50,32),
            (1,3,2,4.1,50,21),
            (1,4,3,4.2,50,10),
            (1,5,4,4.3,50,11),
            (1,6,1,9,1.3,50,25),
            (1,6,2,1.5,50,13),
            (1,6,3,1.6,50,19),
            (1,6,4,1.9,50,25);

SELECT * FROM Player;
SELECT * FROM Stand;
SELECT * FROM Ad_Panel;
SELECT * FROM Coordinates;
SELECT * FROM Drink;
SELECT * FROM Ingredient;
SELECT * FROM Contain;
SELECT * FROM Current_day;
SELECT * FROM Current_hour;
SELECT * FROM Stock;
