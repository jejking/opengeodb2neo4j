opengeodb2neo4j
===============

This is a simple tool to reuse the data from [OpenGeoDb](http://opengeodb.org/wiki/OpenGeoDB) 
in the graph database [Neo4j](http://www.neo4j.org) which feels like a better fit for such hierarchically
linked data than MySQL.

It reads in OpenGeoDb data in tabular form and uses it to create a Neo4j database.

Building
--------

The tool requires Java 7 to compile and run.

The project comes with a maven `pom.xml` file. Simply do a `mvn install` to build from source.

The result is a jar with dependencies in the `target` directory. The jar is executable.

Running the database creation tool
----------------------------------

First, we need the opengeodb tab delimited data files for Germany and for its postal codes.

```shell
wget http://fa-technik.adfc.de/code/opengeodb/DE.tab
wget http://fa-technik.adfc.de/code/opengeodb/PLZ.tab
```
We also need to define for ourselves a neo4j target directory. A whole new data directory will be created here.

```shell
mkdir opengeodb-neo
```

Assuming we have downloaded these into our working directory, we can then run the program that creates us a neo4j database.

```shell
java -jar target/opengeodb2neo4j-0.0.1-SNAPSHOT-jar-with-dependencies.jar -n opengeodb-neo -p DE.tab -z PLZ.tab
```

Note that passing in the option `-h` gives more details on the options.

Running the command as above results in some logging to the console and, after a bit of work, the creation of a neo4j datastore in the specified directory.

Binding the database into neo4j
-------------------------------

Now, assuming that we have downloaded the Neo4j 2.0 distribution and extracted it, we can now simply link in the datastore we created earlier.

Edit the `NEO4J_HOME/conf/neo4j-server.properties` file and set the `org.neo4j.server.database.location` property to the `opengeodb-neo` directory specified above.

Then start the neo4j database as per the documentation.

Implementing the opengeodb example queries in neo4j
---------------------------------------------------

The OpenGeoDb website has a number of [examples](http://opengeodb.org/wiki/Beispiele) which show how to query the MySQL database (and which are, incidentally, a good demonstration of why a graph database is a better match for the data set). 

### Reading the properties of Oberammergau

`match (oberammergau:Place { NAME:'Oberammergau' }) return oberammergau`

Selections on properties by type (placed into the corresponding tables) are then not really very interesting.

### What types of node do we have named 'Berlin'?

`match (berlin:Place { NAME:'Berlin'}) return berlin.TYPE, berlin.LOC_ID`

This returns us three nodes, as per the OpenGeoDb examples. Note that the tab data already has the types of place calculated, so that there is no need to consider the abstract "Politische Gliederung".

### What are the basic properties of the city of Berlin?

`match (berlin:Place { NAME:'Berlin', LOC_ID:14356}) return berlin`

The example also gives us the postal codes of the city, so lets get those as well.

``MATCH (berlinplz:PostalCode)-[:`POSTAL_CODE_FOR`]->(berlin:Place {NAME:'Berlin'}) RETURN berlinplz``

### Identifying rural administrative districts (Landkreise)

`match (landkreis:Place {TYPE:'Landkreis'}) return landkreis.LOC_ID, landkreis.NAME, landkreis.NUMBER_PLATE_CODE`

This gives relatively few results - an indication that the TAB data has suffered some form of data loss in the extraction process from the SQL database.

### Places in the Adminstrative District of Pinneberg

``MATCH (placeInPinnebergPlz:PostalCode)-[:`POSTAL_CODE_FOR`]-(placeInPinneberg:Place)-[:`PART_OF`]->(pinneberg:Place {NAME:'Pinneberg'}) RETURN placeInPinneberg.LOC_ID, placeInPinnebergPlz.POSTAL_CODE, placeInPinneberg.NAME, placeInPinneberg.TYPE, placeInPinneberg.DIALING_CODE, placeInPinneberg.POPULATION``

Note that Elmshorn has 3 postal codes.


