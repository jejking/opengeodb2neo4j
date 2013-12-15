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

import java.util.logging.Logger;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.Schema;

/**
 * Utility to set up schema, particularly auto-indexing.
 * 
 * @author jejking
 */
class SchemaCreator {

    private static final Logger LOGGER = Logger.getLogger(SchemaCreator.class.getName());

    /**
     * @param args
     */
    public static void createSchema(GraphDatabaseService graphDb) {
       
        LOGGER.info("Creating schema");
        try (Transaction tx = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            schema.indexFor(DynamicLabel.label(OpenGeoDbProperties.OPENGEO_DB_LOCATION))
                             .on(OpenGeoDbProperties.LOC_ID)
                             .create();

            LOGGER.info("Created index for loc id");
            
            schema.indexFor(DynamicLabel.label(OpenGeoDbProperties.PLACE_LABEL))
                             .on(OpenGeoDbProperties.PlaceNodeProperties.NAME.name())
                             .create();

            LOGGER.info("Created index for place name");
            
            schema.indexFor(DynamicLabel.label(OpenGeoDbProperties.POSTAL_CODE_LABEL))
                             .on(OpenGeoDbProperties.PlzProperties.POSTAL_CODE.name())
                             .create();
            
            LOGGER.info("Created index for postal code");
            
            tx.success();
        }

        LOGGER.info("Created schema with indexes");
        
  
    }

}
