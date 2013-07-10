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

import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import static info.jejking.opengeodb.neo4j.importer.SchemaCreator.createSchema;

/**
 * Helper class for constructing and destroying in-memory, non-persistent
 * {@link GraphDatabaseService} instances.
 *  
 * @author jejking
 */
public abstract class AbstractGraphDbTest {

    protected GraphDatabaseService graphDb;

    /**
     * Constructor.
     */
    public AbstractGraphDbTest() {
        super();
    }

    /**
     * Creates new test, non-persistent graph DB instance and
     * a node index.
     */
    @Before
    public void setUpDatabase() {
        this.graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        createSchema(graphDb);
    }

    /**
     * Shuts down the test graph database.
     */
    @After
    public void cleanUpDatabase() {
        graphDb.shutdown();
    }

}