# Reset de tous les joueurs 
		UPDATE Player SET player_budget = 2;

#R2 : Coordonnées des stands, nom de boissons, quantité de boissons disponibles, quel prix 

		SELECT player_name as Joueur,latitude as X,longititude as Y,drink_name as Boisson, (stock_quantity_before - stock_sales) as Quantite, stock_drink_cost as Prix
		FROM Player pl,Stand st,Coordinates c,Drink d,Stock s
		WHERE st.player_id=pl.player_id  AND st.stand_id=c.stand_id AND s.stand_id=st.stand_id AND d.drink_id=s.drink_id
		ORDER BY player_name;

		SELECT player_name as Joueur,latitude as X,longititude as Y,drink_name as Boisson, (stock_quantity_before - stock_sales) as Quantite, stock_drink_cost as Prix
		FROM Player p,Drink d,Stock s,Stand st
		INNER JOIN Coordinates c ON c.stand_id = st.stand_id
		WHERE s.drink_id =d.drink_id AND s.stand_id = st.stand_id AND st.player_id = p.player_id
		ORDER BY p.player_id;

# Coût de production d'une boisson

		SELECT drink_name as Boisson, SUM(ingredient_cost) as Cout
		FROM Ingredient i,Drink d,Contain c
		WHERE c.ingredient_id=i.ingredient_id and d.drink_id=c.drink_id
		GROUP BY drink_name;



#-------------------Création Joueur---------------------#
	#Creation du joueur & Recupérer l'id du joueur

		INSERT INTO Player(player_name)
		VALUES($player_name); 

		$player_id= SELECT player_id FROM Player WHERE player_name=$player_name;

			#Creation du Stand
			INSERT INTO Stand(player_id)
				VALUES($player_id);

			$stand_id = SELECT stand_id FROM Stand WHERE player_id=$player_id;

				#Creation de la coordonnée du Stand et recupération

					'Tester si les coordonnées existent'

					SELECT * FROM Coordinates WHERE longititude=$longititude AND latitude=$latitude;

					"Si elles n'existent pas"

					INSERT INTO Coordinates(longititude,latitude,stand_id)
					VALUES($longititude,$latitude,$stand_id);
					

		#Mise à jour du stand du joueur dans Player
		UPDATE Player SET stand_id=$stand_id WHERE player_id=$player_id;

		#Creation du panneaux
			INSERT INTO Ad_Panel(player_id)
				VALUES($player_id);

			$panel_id = SELECT panel_id FROM Ad_Panel WHERE player_id=$player_id;

				#Creation de la coordonnée du Stand et recupération

					'Tester si les coordonnées existent'

					SELECT * FROM Coordinates WHERE longititude=$longititude AND latitude=$latitude;

				"Si elles n'existent pas "

					INSERT INTO Coordinates(longititude,latitude,panel_id)
					VALUES($longititude,$latitude,$panel_id);


	#____________________________________________________________

		INSERT INTO Player(player_name)
		VALUES('Yannick'); 

		INSERT INTO Stand(player_id)
		VALUES((SELECT player_id FROM Player WHERE player_name= 'Yannick'));

		INSERT INTO Coordinates(longititude,latitude,stand_id)
		VALUES(3.5,3.5,(SELECT stand_id FROM Stand WHERE player_id=(SELECT player_id FROM Player WHERE player_name= 'Yannick')));

		UPDATE Player SET stand_id=(SELECT stand_id FROM Stand WHERE player_id=(SELECT player_id FROM Player WHERE player_name= 'Yannick')) 
			WHERE player_id=(SELECT player_id FROM Player WHERE player_name= 'Yannick');



#------------------------------------------------------------#


# Coordonnées des panneaux publicitaires
OK		SELECT player_id,longititude as Y, latitude as X
		FROM Coordinates c, Ad_Panel p
		WHERE c.panel_id=p.panel_id 
		ORDER BY player_id;


OK		SELECT player_id, latitude as X, longititude as Y
		FROM Ad_Panel a
		INNER JOIN Coordinates c ON c.panel_id=a.panel_id
		ORDER BY player_id;



#quantités argent par joueur
OK		SELECT player_name as Joueur, player_budget as Cash
		FROM Player
		ORDER BY player_budget;

# Listes des ingrédient
OK		SELECT ingredient_name 
		FROM Ingredient;


#Emplacement des autres joueurs et panneaux publicitaire
OK		SELECT s.player_id, latitude as X, longititude as Y
		FROM Coordinates c
		INNER JOIN Stand s ON s.stand_id=c.stand_id
		UNION
		SELECT a.player_id, latitude as X, longititude as Y
				FROM Coordinates c
				INNER JOIN Ad_Panel a ON a.panel_id=c.panel_id
		ORDER BY player_id;
	OU	
		SELECT player_name AS Joueur, latitude as X, longititude as Y
				FROM Player p
				INNER JOIN Stand s ON s.player_id=p.player_id
				INNER JOIN Coordinates c ON s.stand_id=c.stand_id
		UNION
		SELECT player_name AS Joueur, latitude as X, longititude as Y
				FROM Player p
				INNER JOIN Ad_Panel a ON a.player_id=p.player_id
				INNER JOIN Coordinates c ON a.panel_id=c.panel_id
		ORDER BY Joueur;


#Ajout boisson dans stand
OK		#La quantité reste optionnel par défaut elle est à zéro
		INSERT INTO Stock(current_day_id,stand_id,drink_id,stock_quantity_before,stock_drink_cost)
			VALUES ($current_day_id,$stand_id,$drink_id,$stock_quantity_before,$stock_drink_cost);

		UPDATE sto

# Effacement d'un joueur
		
OK		# Effacer le joueur soit par son id ou son pseudo
		DELETE FROM Player WHERE player_id = $player_id;
		DELETE FROM Player WHERE player_name = $player_name;


