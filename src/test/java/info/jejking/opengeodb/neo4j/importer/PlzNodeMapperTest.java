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

import info.jejking.opengeodb.neo4j.importer.PlzParser.PlzTabBean;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

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
        givenAPlzBean();

        whenTheNodeIsCreated();

        thenItCanBeFound();
        thenItIsIndexed();
        thenTheDataMatches();
    }

    private void givenAPlzBean() {
        this.plzBean = new PlzTabBean(5078, "01067", 13.7210676148814d, 51.0600336463379d, "Dresden");

    }

    private void whenTheNodeIsCreated() {
        Transaction tx = this.graphDb.beginTx();
        try {
            this.plzNode = this.plzMapper.createPlzNode(graphDb, nodeIndex, plzBean);
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

    }

    private void thenItCanBeFound() {
        Node found = this.graphDb.getNodeById(this.plzNode.getId());
        assertNotNull(found);
    }

    private void thenItIsIndexed() {
        IndexHits<Node> hits1 = this.nodeIndex.get(PlzNodeMapper.PlzProperties.LOC_ID.name(), 5078);
        assertEquals(1, hits1.size());

        IndexHits<Node> hits2 = this.nodeIndex.get(PlzNodeMapper.PlzProperties.POSTAL_CODE.name(), "01067");
        assertEquals(1, hits2.size());
    }

    private void thenTheDataMatches() {
        assertEquals(this.plzBean.getId(), this.plzNode.getProperty(PlzNodeMapper.PlzProperties.LOC_ID.name()));
        assertEquals(this.plzBean.getPlz(), this.plzNode.getProperty(PlzNodeMapper.PlzProperties.POSTAL_CODE.name()));
        assertEquals(this.plzBean.getLat(), this.plzNode.getProperty(PlzNodeMapper.PlzProperties.LATITUDE.name()));
        assertEquals(this.plzBean.getLon(), this.plzNode.getProperty(PlzNodeMapper.PlzProperties.LONGITUDE.name()));
        assertEquals(this.plzBean.getPlaceName(),
                this.plzNode.getProperty(PlzNodeMapper.PlzProperties.PLACE_NAME.name()));
    }
}
