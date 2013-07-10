/*
 * Copyright 2013 by John E. J. King.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.jejking.opengeodb.neo4j.importer;

import static info.jejking.opengeodb.neo4j.importer.SchemaCreator.createSchema;
import info.jejking.opengeodb.neo4j.importer.PlaceParser.PlaceBean;
import info.jejking.opengeodb.neo4j.importer.PlzParser.PlzTabBean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Class which integrates the parsers together with the node and relationship builders to turn the TAB-separated exports
 * from OpenGeoDB into a Neo4j graph database.
 * 
 * @author jejking
 * 
 */
public class Importer {

    private static final Logger LOGGER = Logger.getLogger(Importer.class.getName());

    private List<PlaceBean> placeBeans;
    private List<PlzTabBean> plzBeans;
    private GraphDatabaseService graphDb;
    private Map<Integer, Node> placeNodeMap;
    private Map<String, Node> plzNodeMap;

    /**
     * Parses the files, assembles in memory bean representations, converts these beans to Neo4j {@link Node} instances
     * and links them together appropriately with Neo4j {@link Relationship} instances.
     * 
     * @param placeFile
     *            path to file with tab-delimited place data
     * @param plzFile
     *            path to file with tab-delimited postal code data
     * @param dbDir
     *            directory in which to create the Neo4J graph database
     * @throws IOException
     *             on io problems
     */
    public void doImport(String placeFile, String plzFile, String dbDir) throws IOException {
        LOGGER.info("Starting import");
        parsePlaces(placeFile);
        parsePlz(plzFile);

        createDatabase(dbDir);

        // currently runs in separate transactions in order to keep memory overhead
        // for the Neo4j tx processing under control
        // TODO consider using bulk importer
        doInTransaction(new Runnable() {

            @Override
            public void run() {
                createPlaceNodes();
                LOGGER.info("Created place nodes");
            }
        });

        doInTransaction(new Runnable() {

            @Override
            public void run() {
                createPlzNodes();
                LOGGER.info("Created plz nodes");
            }
        });

        doInTransaction(new Runnable() {

            @Override
            public void run() {
                createRelationships();
                LOGGER.info("Created relationships");
            }
        });

        graphDb.shutdown();
        LOGGER.info("Shut down graph db");
        
        LOGGER.info("Done!");
    }

    private void doInTransaction(Runnable runnable) {
        Transaction tx = graphDb.beginTx();
        try {
            runnable.run();
            tx.success();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Transaction Failed", e);
            tx.failure();
        } finally {
            tx.finish();
        }

    }

    private void createPlaceNodes() {
        PlaceNodeMapper placeNodeMapper = new PlaceNodeMapper();
        this.placeNodeMap = new HashMap<>();
        for (PlaceBean placeBean : this.placeBeans) {
            Node placeNode = placeNodeMapper.createPlaceNode(graphDb, placeBean);
            this.placeNodeMap.put(placeBean.getId(), placeNode);
        }
    }

    private void createPlzNodes() {
        PlzNodeMapper plzNodeMapper = new PlzNodeMapper();
        this.plzNodeMap = new HashMap<>();
        for (PlzTabBean plzBean : this.plzBeans) {
            Node plzNode = plzNodeMapper.createPlzNode(graphDb, plzBean);
            this.plzNodeMap.put(plzBean.getPlz(), plzNode);
        }
    }

    private void createRelationships() {
        PlaceRelationshipBuilder prb = new PlaceRelationshipBuilder();
        for (PlaceBean placeBean : this.placeBeans) {
            prb.buildRelationshipsForPlace(graphDb, placeNodeMap, plzNodeMap, placeBean);
        }
    }

    private void createDatabase(String dbDir) {
        this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbDir);
        
        createSchema(this.graphDb);

        LOGGER.info("Created graph db in directory " + dbDir);
    }

    

    private void parsePlz(String plzFile) throws IOException {
        PlzParser plzParser = new PlzParser();
        this.plzBeans = plzParser.readDataFromStream(new FileInputStream(plzFile));
        LOGGER.info("read in plz from: " + plzFile);
    }

    private void parsePlaces(String placeFile) throws IOException {
        PlaceParser placeParser = new PlaceParser();
        this.placeBeans = placeParser.readDataFromStream(new FileInputStream(placeFile));
        LOGGER.info("read in places from: " + placeFile);
    }

}
