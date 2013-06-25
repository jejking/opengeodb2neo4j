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

import info.jejking.opengeodb.neo4j.importer.PlaceParser.PlaceBean;
import info.jejking.opengeodb.neo4j.importer.PlzParser.PlzTabBean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

/**
 * Class which integrates the parsers together with the node and 
 * relationship builders to turn the TAB-separated exports from
 * OpenGeoDB into a Neo4j graph database.
 * 
 * @author jejking
 *
 */
public class Importer {

    private List<PlaceBean> placeBeans;
    private List<PlzTabBean> plzBeans;
    private GraphDatabaseService graphDb;
    private Index<Node> nodeIndex;
    private Map<Integer, Node> placeNodeMap;
    private Map<String, Node> plzNodeMap;

    /**
     * Parses the files, assembles in memory bean representations, converts these
     * beans to Neo4j {@link Node} instances and links them together appropriately 
     * with Neo4j {@link Relationship} instances.
     * 
     * @param placeFile path to file with tab-delimited place data
     * @param plzFile path to file with tab-delimited postal code data
     * @param dbDir directory in which to create the Neo4J graph database
     * @throws IOException on io problems
     */
    public void doImport(String placeFile, String plzFile, String dbDir) throws IOException {
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
		System.out.println("Created place nodes");
	    }
	});
	
	doInTransaction(new Runnable() {
	    
	    @Override
	    public void run() {
		createPlzNodes();
		System.out.println("Created plz nodes");
	    }
	});
	
	doInTransaction(new Runnable() {
	    
	    @Override
	    public void run() {
		createRelationships();
		System.out.println("Created relationships");
	    }
	});
	
	System.out.println("Done!");
    }

    private void doInTransaction(Runnable runnable) {
	Transaction tx = graphDb.beginTx();
	try {
	    runnable.run();
	    tx.success();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println("Tx failure");
	    tx.failure();
	} finally {
	    tx.finish();
	}
	
    }

    private void createPlaceNodes() {
	PlaceNodeMapper placeNodeMapper = new PlaceNodeMapper();
	this.placeNodeMap = new HashMap<>();
	for (PlaceBean placeBean : this.placeBeans) {
	    Node placeNode = placeNodeMapper.createPlaceNode(graphDb, nodeIndex, placeBean);
	    this.placeNodeMap.put(placeBean.getId(), placeNode);
	}
    }

    private void createPlzNodes() {
	PlzNodeMapper plzNodeMapper = new PlzNodeMapper();
	this.plzNodeMap = new HashMap<>();
	for (PlzTabBean plzBean : this.plzBeans) {
	    Node plzNode = plzNodeMapper.createPlzNode(graphDb, nodeIndex, plzBean);
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
        this.nodeIndex = graphDb.index().forNodes( "nodes" );
        
        // and register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                graphDb.shutdown();
                System.out.println("Shutdown graph db instance");
            }
        });
    }

    private void parsePlz(String plzFile) throws IOException {
	PlzParser plzParser = new PlzParser();
	this.plzBeans = plzParser.readDataFromStream(new FileInputStream(plzFile));
	System.out.println("read in plz from: " + plzFile);
    }

    private void parsePlaces(String placeFile) throws IOException {
	PlaceParser placeParser = new PlaceParser();
	this.placeBeans = placeParser.readDataFromStream(new FileInputStream(placeFile));
	System.out.println("read in places from: " + placeFile);
    }
    

}
