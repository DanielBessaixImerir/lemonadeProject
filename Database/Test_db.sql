CREATE USER "Iyokan" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  NOREPLICATION;

CREATE DATABASE "Iyokan_DB"
    WITH 
    OWNER = "Iyokan"
    ENCODING = 'UTF8'
    LC_COLLATE = 'French_France.1252'
    LC_CTYPE = 'French_France.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

CREATE TABLE Player(
        player_id     bigserial  NOT NULL ,
        player_name   Varchar (25) NOT NULL ,
        player_budget Int NOT NULL ,
        player_state  Bool NOT NULL ,
        stand_id      Int NOT NULL ,
        PRIMARY KEY (player_id ) ,
        UNIQUE (player_name )
);

CREATE TABLE Stand(
        stand_id        bigserial   NOT NULL ,
        stand_influence Float NOT NULL ,
        stand_sell      Float NOT NULL ,
        player_id       Int NOT NULL ,
        coord_id        Int NOT NULL ,
        PRIMARY KEY (stand_id )
);

CREATE TABLE Ad_Panel(
        panel_id        bigserial   NOT NULL ,
        panel_influence Float ,
        player_id       Int NOT NULL ,
        coord_id        Int NOT NULL ,
        PRIMARY KEY (panel_id )
);

CREATE TABLE Coordinates(
        coord_id    bigserial   NOT NULL ,
        latitude    Float NOT NULL ,
        longititude Float NOT NULL ,
        PRIMARY KEY (coord_id )
);

CREATE TABLE Ingredient(
        ingredient_id             bigserial   NOT NULL ,
        ingredient_name           Varchar (25) NOT NULL ,
        ingredient_cost           Float NOT NULL ,
        ingredient_type_hasAlcool Bool NOT NULL ,
        ingredient_type_cold      Bool NOT NULL ,
        PRIMARY KEY (ingredient_id ) ,
        UNIQUE (ingredient_name )
);


CREATE TABLE Drink(
        drink_id   bigserial   NOT NULL ,
        drink_name Varchar (25) NOT NULL ,
        drink_cost Float NOT NULL ,
        stock_id   Int NOT NULL ,
        PRIMARY KEY (drink_id )
);

CREATE TABLE Stock(
        stock_id       bigserial   NOT NULL ,
        stock_quantity Int NOT NULL ,
        drink_id       Int NOT NULL ,
        PRIMARY KEY (stock_id )
);

CREATE TABLE contain(
        ingredient_id Int NOT NULL ,
        drink_id      Int NOT NULL ,
        PRIMARY KEY (ingredient_id ,drink_id )
);

CREATE TABLE prepare(
        drink_id Int NOT NULL ,
        stand_id Int NOT NULL ,
        PRIMARY KEY (drink_id ,stand_id )
);

ALTER TABLE Player ADD CONSTRAINT FK_Player_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id);
ALTER TABLE Stand ADD CONSTRAINT FK_Stand_player_id FOREIGN KEY (player_id) REFERENCES Player(player_id);
ALTER TABLE Stand ADD CONSTRAINT FK_Stand_coord_id FOREIGN KEY (coord_id) REFERENCES Coordinates(coord_id);
ALTER TABLE Ad_Panel ADD CONSTRAINT FK_Ad_Panel_player_id FOREIGN KEY (player_id) REFERENCES Player(player_id);
ALTER TABLE Ad_Panel ADD CONSTRAINT FK_Ad_Panel_coord_id FOREIGN KEY (coord_id) REFERENCES Coordinates(coord_id);
ALTER TABLE Drink ADD CONSTRAINT FK_Drink_stock_id FOREIGN KEY (stock_id) REFERENCES Stock(stock_id);
ALTER TABLE Stock ADD CONSTRAINT FK_Stock_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id);
ALTER TABLE contain ADD CONSTRAINT FK_contain_ingredient_id FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id);
ALTER TABLE contain ADD CONSTRAINT FK_contain_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id);
ALTER TABLE prepare ADD CONSTRAINT FK_prepare_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id);
ALTER TABLE prepare ADD CONSTRAINT FK_prepare_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id);