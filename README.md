# import-toast-data

###But

Ce projet permet d'importer des données (au format CSV) dans l'application Toast-Next-Gen (https://github.com/florentL/toast-next-gen).

### Lancer le script d'import

Pré-requis : maven doit être installé

 ```bash
 # [Projet toast-next-gen]
 # Génération d'un jar et d'un war
 ./mvnw install -DskipTests=true
 
# [Projet import-toast-data]
# Créer le jar et copier toutes les dépendances
mvn package
 
# Lancer le script d'import
java -jar target/*.jar
 ```

### Profils

Le script d'import possède 2 configurations : dev ou prod.

### Bug(s) connu(s)

Toutes les entités ne sont pas disponibles à l'import. En effet les entités auditées provoquent des erreurs à l'import (cf la Pull Request #3 - https://github.com/Mariam112/import-toast-data/pulls).

