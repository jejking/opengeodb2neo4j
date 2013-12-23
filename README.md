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


