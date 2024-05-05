#!/bin/bash

# Compilation du code du serveur
javac Server.java

# Génération des classes RMI (stub et skeleton)
rmic Server

# Démarrage du registre RMI en arrière-plan
rmiregistry &

# Lancement du serveur
java Server

# Nettoyage des fichiers de compilation
rm Server.class
 
