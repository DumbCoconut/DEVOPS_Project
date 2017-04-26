<h1 align="center"><a href="https://github.com/DumbCoconut/J-REDIS">J-REDIS</a></h1>

**Table of Contents**

- [J-REDIS](#)
  - [Overview](#overview)
  - [Features](#featurse)
	  - [Features overview](#featuresoverview)
	  - [Client commands](#clientcommands)
	  - [String and integers](#stringandintegers)
	  - [Lists](#lists)
	  - [Sets](#sets)
  - [Getting started](#gettingstarted)
	- [Requirements](#requirements)
	- [build](#build)
	- [run a client](#runaclient)
	- [run a server](#runaserver)
	- [generate javadoc](#generatejavadoc)
	- [miscellaneous](#miscellaneous)
	- [example](#example)
  - [What has been tested](#whathasbeentested)
  - [What has NOT been (automatically) tested](#whathasnotbeen(automatically)tested)
  - [What could be improved](#whatcouldbeimproved)
  - [Feedback](#feedback)
    - [Travis](#travis)
	- [Projet](#projet)
		
## Overview

J-REDIS is a simple redis-like console application. It was made as a toy-project for the DEVOPS course
taught at <a href="http://www.univ-grenoble-alpes.fr/">UGA</a>.

It provides two things:

- A server holding a in-memory data structure store. It supports data structures such as 
strings, integers, sets and lists. 

- A client able to query a given J-REDIS server. 

The connection between the two is done using 
<a href="http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/rmiregistry.html">rmiregistry</a>.

## Features

### Overview

J-REDIS provides a subset of redis features. Not everything is supported, e.g negative indexes are not.
You can't put quotes inside quotes yet - "my name is" would work but "my "name" is" would produce an error.
The "integer" type has been introduced, e.g "3" will be an integer and not a string. 

### Client commands 

- SET_SERVER _host\_ip server\_name_
- HELP _[cmd1, cmd2, ..., cmdN]_
- QUIT
- EXIT

### String and integers

- DECR _key_
- DECRBY _key integer_
- DEL _key_
- GET _key_
- INCR _key_
- INCRBY _key integer_
- SET _key value_
- TYPE _key_

### Lists

- LINDEX _key index_
- LLEN _key_
- LPOP _key_
- LPUSH _key value_
- LRANGE _key start end_
- LREM _key count value_
- LSET _key index value_
- LTRIM _key start end_
- RPOP _key_
- RPUSH _key value_


### Sets

- SADD _key member_
- SCARD _key_
- SISMEMBER _key_ _member_
- SMEMBERS _key_
- SREM _key member_
- SINTER _key1 key2 ... keyN_
- SINTERSTORE _dstkey key1 key2 ... keyN_
- SPOP _key_
- SRANDMEMBER _key_
- SMOVE _srckey dstkey member_
- SUNION _key1 key2 ... keyN_
- SUNIONSTORE _dstkey key1 key2 ... keyN_
- SDIFF _key1 key2 ... keyN_
- SDIFFSTORE _dstkey key1 key2 ... keyN_

For more explanations, use the HELP command directly in the client.

## Getting started

### Requirements

To build and run the application you need:

- java jdk 1.8 
- maven 3
- A linux distribution, as nothing has been tested under Windows

### build

To build both the server and the client, run (at the root directory) `mvn install`.

If you want to build only one of both, go into the module directory and run `mvn install`.

### run a client

To run a client, from the root directory, run `java -jar jredisclient/target/j-redis-client-1.0-SNAPSHOT-shaded.jar`.

To connect the client to a server, you need to make sure rmiregistry is running. To launch rmiregistry, 
run (from the root directory) `./launch_rmiregistry.sh`. 

Once rmiregistry is running, you should be able to connect your client to a server by using the command 
`SET_SERVER host_ip server_name`. You can use the `HELP` command in the client if you need more information.


### run a server

To run a server, first make sure that you've already launched rmiregistry (if not, run from the root directory 
`./launch_rmiregistry.sh`). 

Once rmiregistry is running, you can run a server by running the command (from the root directory) 
`java -jar jredisserver/target/j-redis-server-1.0-SNAPSHOT-shaded.jar [-options]`. The available options are:

	-h	--help	Display this information.
	-n	--name	Set the name of this server.
	-p	--port	Set the port of this server.

For example if you want to run a server named "hello_world" running on port 4000, you should run the 
following command from the root directory: 
`java -jar jredisserver/target/j-redis-server-1.0-SNAPSHOT-shaded.jar -n "hello_world" -p 4000`. You should be greeted by
the following message: `Hello. I am server "hello_world" and I'm running on port 40000`.    

### generate javadoc

The code is documented. If you want to generate the javadoc, just run `mvn javadoc:javadoc`. You can then access the 
javadoc by going into `{module}/target/site/apidocs/` and opening `index.html`.

### miscellaneous

Note that using the non-shaded jars while not work properly. You might still be able to launch the client, 
but some features will not work. You need to run the shaded jar. Still, it should be possible to run the
non-shaded jar by adding on your own the classpath in the java command-line.

### example

Here is an example showing how to launch everything, connect the client to a server and work on that server.

![example](http://i.imgur.com/LzPMTmn.png)

## What has been tested

- Everything regarding the data structure store

- The server doing operations on the store

- The construction of the requests made by the client to the server

## What has NOT been (automatically) tested

Either tested manually or still in experimental stage:

- The client interface

- The client-server connection

- The "did you mean X" function. It uses the Levenshtein distance to find the closest match, without
any kind of heuristic behind it, so it might be way off sometimes

- One server with multiple clients accessing to the storage at the same time, thought: a) the server 
uses RMI registry, so a new thread should be created for each new client and b) the storage uses synchronized methods
and a concurrent hashmap from <a href="https://github.com/google/guava">Guava</a> so everything _should_ 
be thread-safe.

## What could be improved

- The overall design, which has not been given much thoughts: the goal was to have something that 
works great. We could for example have a RequestFactory. We do have a few unchecked casts, thought
they should never break anything, it could still be improved with a bit of refactoring. 

- The responses given by the server. We could for example follow the 
<a href="https://redis.io/topics/protocol"> Redis Protocol specification</a> 
to allow multi-buck replies.

- Tests: bash script to test the client interface, make them easier to navigate and refactor, etc

- Consistency in the code base, e.g we have both doStuff and dostuff (simple enough with refactoring tools,
but not important enough right now)

- One README per module with more detailed information.

## Feedback

### Travis

- Trivial à utiliser dans une utilisation "simple", e.g sans cron jobs etc.

- Les erreurs ne sont pas toujours très claires. Par exemple il n'y a aucune indication directe sur 
comment résoudre des erreurs comme : `The command "eval mvn install -DskipTests=true 
-Dmaven.javadoc.skip=true -B -V" failed.` qui ne sont au final que des soucis de compatibilité entre 
les versions utilisées par travis et celles utilisées dans le pom. 

- Le principal intérêt de Travis est découvrir rapidement si un commit a cassé quelque chose, mais ça
ne devrait pas arriver si on vérifie que les tests passent avant de commit. Néanmoins ça devient plus
utile au moment de merge : on sait directement si le pull request peut être validé (en tout cas sur 
le plan "technique") sans avoir à vérifier manuellement que la personne demandant le pull de sa 
branche a bien passé les tests. On peut donc facilement gérer le projet à "grande échelle".
 
- Etant donné la facilité d'utilisation et le fait que ce soit gratuit pour une utilisation open 
source, intégrer travis à tous ses projets ne peut qu'être positif, exception faite du cas où on se
moque de passer les tests (ou qu'il n'y a pas de tests unitaires). 

### Projet

Une amélioration possible (en tout cas d'un point de vue étudiant noté) serait de détailler dans le
sujet la notation. On sait en gros sur quoi nous sommes évalués, mais 

- Pour Github, les commits doivent-ils être pertinents (clareté, description, ...) ? Ne pas utiliser 
de branches est-il pénalisant ?  

- Pour la qualité du code, comment est-elle évaluée ? Uniquement par le fait que les tests soient 
passés (et donc que le logiciel produit est fonctionnel) ou bien d'autres critères sont pris en 
comptes (par exemple : structuration, cohérence, efficacité, documentation, facilité à maintenir,
etc.) ? 

- L'évaluation des fonctionnalités est faite notamment à partir de la couverture, mais est-ce si 
important ? Typiquement dans le cas de fonctions qui ne font "rien" (par exemple un wrapper d'une 
ligne qui retourne une autre fonction), écrire des tests semble être une perte de temps, mais ne
pas le faire diminuera le taux de couverture de code.

