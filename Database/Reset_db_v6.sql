

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
        longitude real NOT NULL ,
        stand_id bigint,
        panel_id bigint,
        PRIMARY KEY (coord_id ),
        UNIQUE (stand_id)
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

CREATE TABLE Day(
        day_value int NOT NULL,
        PRIMARY KEY (day_value)
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


CREATE TABLE Region(
        region_name int NOT NULL,
        latitude    real NOT NULL ,
        longitude real NOT NULL ,
        latitude_span    real NOT NULL ,
        longitude_span real NOT NULL ,
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