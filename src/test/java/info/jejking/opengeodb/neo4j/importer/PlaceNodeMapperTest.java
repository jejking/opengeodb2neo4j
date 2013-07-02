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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.jejking.opengeodb.neo4j.importer.PlaceParser.PlaceBean;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

/**
 * Basic test of {@link PlaceNodeMapper}.
 * 
 * @author jejking
 * 
 */
public class PlaceNodeMapperTest extends AbstractGraphDbTest {

    private PlaceNodeMapper placeNodeMapper = new PlaceNodeMapper();
    private PlaceBean placeBean;
    private Node placeNode;

    @Test
    public void shouldCreateANodeFromAPlaceBean() {
        givenAPlaceBean();

        whenTheNodeIsCreated();

        thenItCanBeFound();
        thenItIsIndexed();
        thenTheDataMatches();
    }

    private void thenTheDataMatches() {
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.LOC_ID.name()), 17838);
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.AGS.name()), "02000000");
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.ASCII.name()), "HAMBURG");
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.NAME.name()), "Hamburg");
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.LATITUDE.name()), 53.554423d);
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.LONGITUDE.name()), 9.994583d);
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.DIALING_CODE.name()), "040");
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.POPULATION.name()), 1734830);
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.AREA.name()), 755.0);
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.LEVEL.name()), 6);
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.TYPE.name()),
                "Freie und Hansestadt");
        assertEquals(this.placeNode.getProperty(PlaceNodeMapper.PlaceNodeProperties.NUMBER_PLATE_CODE.name()), "HH");
    }

    private void givenAPlaceBean() {
        PlaceBean hh = new PlaceBean();
        hh.setId(17838);
        hh.setAgs("02000000");
        hh.setAscii("HAMBURG");
        hh.setName("Hamburg");
        hh.setLat(53.554423d);
        hh.setLon(9.994583d);
        hh.setPlzs("20038,20088,20095,20097,20099,20144,20146,20148,20149,20249,20251,20253,20255,20257,20259,20350,20354,20355,20357,20359,20457,20459,20535,20537,20539,21029,21031,21033,21035,21037,21039,21073,21075,21077,21079,21107,21109,21129,21147,21149,22041,22043,22045,22047,22049,22081,22083,22085,22087,22089,22111,22113,22115,22117,22119,22143,22145,22147,22149,22159,22175,22177,22179,22297,22299,22301,22303,22305,22307,22309,22335,22337,22339,22359,22391,22393,22395,22397,22399,22415,22417,22419,22453,22455,22457,22459,22523,22525,22527,22529,22547,22549,22559,22587,22589,22605,22607,22609,22761,22763,22765,22767,22769"); // quite
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // a
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // few
        hh.setVorwahl("040");
        hh.setEinwohner(1734830);
        hh.setFlaeche(755);
        hh.setLevel(6);
        hh.setTyp("Freie und Hansestadt");
        hh.setOf(526);
        hh.setKz("HH");
        this.placeBean = hh;
    }

    private void whenTheNodeIsCreated() {
        Transaction tx = this.graphDb.beginTx();
        try {
            this.placeNode = this.placeNodeMapper.createPlaceNode(graphDb, nodeIndex, placeBean);
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

    }

    private void thenItCanBeFound() {
        Node found = this.graphDb.getNodeById(this.placeNode.getId());
        assertNotNull(found);
    }

    private void thenItIsIndexed() {
        IndexHits<Node> foundByLocId = this.nodeIndex.query(PlaceNodeMapper.PlaceNodeProperties.LOC_ID.name(),
                Integer.valueOf(17838));
        assertEquals(1, foundByLocId.size());

        IndexHits<Node> foundByName = this.nodeIndex.query(PlaceNodeMapper.PlaceNodeProperties.NAME.name(), "Hamburg");
        assertEquals(1, foundByName.size());
    }

}
