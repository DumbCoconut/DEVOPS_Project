# DEVOPS_Project

NOTE : tout se passe sur la branche client pour le moment, la branche master a plusieurs jours de retard

# FONCTIONNEMENT

En attendant que le pom soit modifié (cf TODO), pour faire fonctionner le programme :

1) clone https://github.com/DumbCoconut/Redis-Like_App.git
2) cd Redis-Like-App
3) mvn install
4) cd target
5) cd classes
6) rmiregistry
7) lancer la classe server et la laisser tourner
8) lancer le client, se connecter au serveur (add_server 127.0.0.1 server_0), puis on peut faire les set, get etc

Normalement mvn install devrait créer le dossier target, mais impossible de vérifier maintenant, mandelbrot ayant une
version ancienne de mvn et java 1.7 donc impossible de compiler ... si ça ne le créé pas alors gérer ça avec un IDE pour le moment

# TODO

- écrire les tests du client (faisable en lançant un client et en lui envoyant des données via stdin ? Sachant qu'il 
faut avoir un client de lancé, autrement passer par des scripts bash).
- écrire les tests de RequestName, RequestAddServer et RequestHelp.
- faire en sorte qu'on puisse parser des chaînes incluant des quotes échapées, par exemple "une \\"quote\"\ comme ça".
Actuellement les " à l'intérieur de "" ne fonctionnent pas (voir client.splitIntoTokens, il faut modifier le regex)
- intégrer List et Set
- améliorer la gestion des remote errors
- modifier le pom pour qu'il nous fasse un module client et un module server (qui contiendra aussi le storage)
- écrire le vrai readme
- faciliter le lancement d'un serveur (simple script bash)
