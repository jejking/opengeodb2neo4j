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

import info.jejking.opengeodb.neo4j.importer.OpenGeoDbProperties.PlzProperties;
import info.jejking.opengeodb.neo4j.importer.PlzParser.PlzTabBean;

import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.*;

/**
 * Basic test of {@link PlzNodeMapper}.
 * 
 * @author jejking
 */
public class PlzNodeMapperTest extends AbstractGraphDbTest {

    private PlzNodeMapper plzMapper = new PlzNodeMapper();
    private PlzTabBean plzBean;
    private Node plzNode;

    @Test
    public void shouldCreatePlzNodeFromBean() {
        
        
        try ( Transaction tx = graphDb.beginTx() )
        {
            givenAPlzBean();

            whenTheNodeIsCreated();
            tx.success();
        }
        
        try ( Transaction tx = graphDb.beginTx() )
        {

            thenItCanBeFound();
            thenItIsIndexed();
            thenTheDataMatches();
        }
        
    }

    private void givenAPlzBean() {
        this.plzBean = new PlzTabBean(5078, "01067", 13.7210676148814d, 51.0600336463379d, "Dresden");

    }

    private void whenTheNodeIsCreated() {
        this.plzNode = this.plzMapper.createPlzNode(graphDb, plzBean);
    }

    private void thenItCanBeFound() {

        Node found = this.graphDb.getNodeById(this.plzNode.getId());
        assertNotNull(found);
    }

    private void thenItIsIndexed() {
        
        
        Label plzLabel = DynamicLabel.label(OpenGeoDbProperties.POSTAL_CODE_LABEL);
        Label openGeoDbLocLabel = DynamicLabel.label(OpenGeoDbProperties.OPENGEO_DB_LOCATION);
        ResourceIterator<Node> plzById = graphDb
                                            .findNodesByLabelAndProperty(
                                                openGeoDbLocLabel, 
                                                OpenGeoDbProperties.LOC_ID,
                                                5078)
                                            .iterator();
        
        try {
            assertTrue(plzById.hasNext());
        } finally {
            plzById.close();
        }
        
        ResourceIterator<Node> plzByPlz = graphDb
                                                .findNodesByLabelAndProperty(
                                                        plzLabel, 
                                                        OpenGeoDbProperties.PlzProperties.POSTAL_CODE.name(), 
                                                        "01067")
                                                .iterator();
        try {
            assertTrue(plzByPlz.hasNext());
        } finally {
            plzByPlz.close();
        }

        
       

    }

    private void thenTheDataMatches() {
        assertEquals(this.plzBean.getId(), this.plzNode.getProperty(PlzProperties.LOC_ID.name()));
        assertEquals(this.plzBean.getPlz(), this.plzNode.getProperty(PlzProperties.POSTAL_CODE.name()));
        assertEquals(this.plzBean.getLat(), this.plzNode.getProperty(PlzProperties.LATITUDE.name()));
        assertEquals(this.plzBean.getLon(), this.plzNode.getProperty(PlzProperties.LONGITUDE.name()));
        assertEquals(this.plzBean.getPlaceName(),
                this.plzNode.getProperty(PlzProperties.PLACE_NAME.name()));
    }
}
