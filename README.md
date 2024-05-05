# Implémentation du "Bag of Tasks" avec persistance des données dans une architecture RMI

Ce dépôt contient l'implémentation du "Bag of Tasks" avec persistance des données dans une architecture RMI, réalisée dans le cadre du cours "Base de Données et Environnement Distribué" du programme M2 BDIA.

## Installation et Test du Système

Pour tester le système mis en place, suivez les étapes ci-dessous :

### 1. Compilation et Lancement du Serveur

Placez-vous dans le répertoire contenant les différentes classes Java et exécutez les commandes suivantes :

bash
javac Server.java
java Server

### 2. Compilation et Lancement de la Connexion à la Base de Données

Placez-vous dans le répertoire contenant les différentes classes Java et exécutez les commandes suivantes :
javac DatabaseConnectionPoolServer.java
java -cp "ojdbc8.jar";. DatabaseConnectionPoolServer

Assurez-vous d'ajouter votre propre base de données Oracle. Modifiez les variables LOGIN, MDP, et URL dans DatabaseConnectionPoolServer.java pour correspondre à vos informations de connexion :
// LOGIN et MDP doivent correspondre à ceux de la BD
final private static String LOGIN = "votre_login";
final private static String MDP = "votre_mot_de_passe";
// URL pour se connecter à votre base de données Oracle
final private static String URL = "jdbc:oracle:thin:@adresse_hote:port:nom_de_la_bd";
3. Compilation et Lancement du Client Applicatif

Placez-vous dans le répertoire contenant les différentes classes Java et exécutez les commandes suivantes :

bash

javac Client.java
java Client

4. Lancement du Worker Router

Placez-vous dans le répertoire contenant les différentes classes Java et exécutez la commande suivante :

bash

javac WorkerRouter.java


