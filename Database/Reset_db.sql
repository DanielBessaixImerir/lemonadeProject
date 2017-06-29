#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
#User : Iyokan
#------------------------------------------------------------

CREATE USER "Iyokan" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  NOREPLICATION;

#------------------------------------------------------------
# Database : Iyokan
#------------------------------------------------------------

CREATE DATABASE "Iyokan_DB"
    WITH 
    OWNER = "Iyokan"
    ENCODING = 'UTF8'
    LC_COLLATE = 'French_France.1252'
    LC_CTYPE = 'French_France.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

# USE Iyokan;

#------------------------------------------------------------
# Table: Player
#------------------------------------------------------------

CREATE TABLE Player(
        player_id     bigserial  NOT NULL ,
        player_name   Varchar (25) NOT NULL ,
        player_budget real NOT NULL ,
        player_state  Bool NOT NULL ,
        stand_id      bigint ,
        PRIMARY KEY (player_id ) ,
        UNIQUE (player_name )
);


#------------------------------------------------------------
# Table: Stand
#------------------------------------------------------------

CREATE TABLE Stand(
        stand_id        bigserial   NOT NULL ,
        stand_influence real NOT NULL ,
        stand_sell      real NOT NULL ,
        player_id       bigint NOT NULL ,
        coord_id        bigint NOT NULL ,
        PRIMARY KEY (stand_id )
        UNIQUE (coord_id,player_id)
);


#------------------------------------------------------------
# Table: Ad_Panel
#------------------------------------------------------------

CREATE TABLE Ad_Panel(
        panel_id        bigserial  NOT NULL ,
        panel_influence real ,
        player_id       bigint NOT NULL ,
        coord_id        bigint NOT NULL ,
        PRIMARY KEY (panel_id )
        UNIQUE (coord_id)
);


#------------------------------------------------------------
# Table: Coordinates
#------------------------------------------------------------

CREATE TABLE Coordinates(
        coord_id    bigserial  NOT NULL ,
        latitude    real NOT NULL ,
        longititude real NOT NULL ,
        PRIMARY KEY (coord_id )
);


#------------------------------------------------------------
# Table: Ingredient
#------------------------------------------------------------

CREATE TABLE Ingredient(
        ingredient_id             bigserial  NOT NULL ,
        ingredient_name           Varchar (25) NOT NULL ,
        ingredient_cost           real NOT NULL ,
        ingredient_type_hasAlcool Bool NOT NULL ,
        ingredient_type_cold      Bool NOT NULL ,
        PRIMARY KEY (ingredient_id ) ,
        UNIQUE (ingredient_name )
);


#------------------------------------------------------------
# Table: Drink
#------------------------------------------------------------

CREATE TABLE Drink(
        drink_id   bigserial   NOT NULL ,
        drink_name Varchar (25) NOT NULL ,
        drink_cost real ,
        PRIMARY KEY (drink_id )
);


#------------------------------------------------------------
# Table: Stock
#------------------------------------------------------------

CREATE TABLE Stock(
        stock_id       bigserial  NOT NULL ,
        stock_quantity Int NOT NULL ,
        PRIMARY KEY (stock_id )
);


#------------------------------------------------------------
# Table: contain
#------------------------------------------------------------

CREATE TABLE Contain(
        ingredient_id bigint NOT NULL ,
        drink_id      bigint NOT NULL ,
        PRIMARY KEY (ingredient_id ,drink_id )
);


#------------------------------------------------------------
# Table: prepare
#------------------------------------------------------------

CREATE TABLE Prepare(
        drink_id bigint NOT NULL ,
        stock_id bigint NOT NULL ,
        stand_id bigint NOT NULL ,
        PRIMARY KEY (drink_id ,stock_id ,stand_id )
)

#------------------------------------------------------------
# Contraintes
#------------------------------------------------------------

ALTER TABLE Player ADD CONSTRAINT FK_Player_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id);
ALTER TABLE Stand ADD CONSTRAINT FK_Stand_player_id FOREIGN KEY (player_id) REFERENCES Player(player_id);
ALTER TABLE Stand ADD CONSTRAINT FK_Stand_coord_id FOREIGN KEY (coord_id) REFERENCES Coordinates(coord_id);
ALTER TABLE Ad_Panel ADD CONSTRAINT FK_Ad_Panel_player_id FOREIGN KEY (player_id) REFERENCES Player(player_id);
ALTER TABLE Ad_Panel ADD CONSTRAINT FK_Ad_Panel_coord_id FOREIGN KEY (coord_id) REFERENCES Coordinates(coord_id);
ALTER TABLE contain ADD CONSTRAINT FK_contain_ingredient_id FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id);
ALTER TABLE contain ADD CONSTRAINT FK_contain_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id);
ALTER TABLE prepare ADD CONSTRAINT FK_prepare_drink_id FOREIGN KEY (drink_id) REFERENCES Drink(drink_id);
ALTER TABLE prepare ADD CONSTRAINT FK_prepare_stand_id FOREIGN KEY (stand_id) REFERENCES Stand(stand_id);
ALTER TABLE prepare ADD CONSTRAINT FK_prepare_stock_id FOREIGN KEY (stock_id) REFERENCES Stock(stock_id);
