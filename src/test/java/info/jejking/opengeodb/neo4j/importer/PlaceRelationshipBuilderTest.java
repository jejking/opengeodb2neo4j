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
import static org.junit.Assert.assertTrue;
import info.jejking.opengeodb.neo4j.importer.PlaceParser.PlaceBean;
import info.jejking.opengeodb.neo4j.importer.PlaceRelationshipBuilder.Relationships;
import info.jejking.opengeodb.neo4j.importer.PlzParser.PlzTabBean;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * Basic test of {@link PlaceRelationshipBuilder}.
 * 
 * @author jejking
 * 
 */
public class PlaceRelationshipBuilderTest extends AbstractGraphDbTest {

    private PlaceBean hamburg = hamburg();
    private PlaceBean hamburgNord = hamburgNord();
    private PlaceBean uhlenhorst = uhlenhorst();
    private PlzTabBean plz22087 = plz22087();
    private PlzTabBean plz22085 = plz22085();
    private PlzTabBean plz22081 = plz22081();

    private Map<Integer, Node> placeNodeMap;
    private Map<String, Node> plzNodeMap;

    @Test
    public void shouldBuildPlaceRelationships() {

        try ( Transaction tx = graphDb.beginTx() )
        {
            // operations on the graph
            // ...
            givenPlaceNodes(hamburg, hamburgNord);

            whenThePlaceRelationshipsAreBuilt(hamburgNord);

            thenPlaceRelationshipExists(hamburg, hamburgNord);
            tx.success();
        }
        
        
    
    }

    @Test
    public void shouldBuildPostalCodeRelationships() {
        
        try ( Transaction tx = graphDb.beginTx() )
        {
            // operations on the graph
            // ...
            givenPlaceNodes(uhlenhorst);
            givenPostalCodes(plz22081, plz22085, plz22087);

            whenThePostalCodeRelationshipsAreBuilt(uhlenhorst);

            thenPostalCodeRelationshipsExist(uhlenhorst, plz22081, plz22085, plz22087);
            tx.success();
        }
        
        

    }

    private void thenPostalCodeRelationshipsExist(PlaceBean place, PlzTabBean... plzBeans) {
        Node placeNode = lookUpPlaceByLocId(place);

        for (PlzTabBean plzBean : plzBeans) {
            Node plzNode = lookUpPostalCodeByPlz(plzBean);
            // is plz linked to place via POSTAL_CODE_FOR ?
            assertTrue(nodesAreRelated(plzNode, placeNode, PlaceRelationshipBuilder.Relationships.POSTAL_CODE_FOR));
        }
    }



    private boolean nodesAreRelated(Node from, Node to, Relationships relationship) {

        Iterable<Relationship> fromRelationships = from.getRelationships(relationship, Direction.OUTGOING);
        boolean foundRelatedNode = false;
        for (Relationship rel : fromRelationships) {
            if (rel.getOtherNode(from).equals(to)) {
                foundRelatedNode = true;
                break;
            }
        }

        return foundRelatedNode;
    }

    private void whenThePostalCodeRelationshipsAreBuilt(PlaceBean placeBean) {
        
        PlaceRelationshipBuilder prb = new PlaceRelationshipBuilder();
        prb.buildPostalCodeRelationships(graphDb, placeNodeMap, plzNodeMap, placeBean);
        
    }

    private void givenPostalCodes(PlzTabBean... plzBeans) {
        this.plzNodeMap = new HashMap<>();

        PlzNodeMapper plzNodeMapper = new PlzNodeMapper();
 
        for (PlzTabBean plzBean : plzBeans) {
            Node plzNode = plzNodeMapper.createPlzNode(graphDb, plzBean);
            this.plzNodeMap.put(plzBean.getPlz(), plzNode);
        }
           
    }

    private void givenPlaceNodes(PlaceBean... beans) {

        this.placeNodeMap = new HashMap<>();

  
        PlaceNodeMapper placeNodeMapper = new PlaceNodeMapper();
   
        for (PlaceBean bean : beans) {
            Node placeNode = placeNodeMapper.createPlaceNode(graphDb, bean);
            this.placeNodeMap.put(bean.getId(), placeNode);
        }
   
    }

    private PlaceBean hamburg() {
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
        return hh;
    }

    private PlaceBean hamburgNord() {
        return new PlaceBean(
                152980,
                "02000000",
                "HAMBURG-NORD",
                "Hamburg-Nord",
                53.6153d,
                9.99269d,
                null,
                "20144,20249,20251,20251,20253,22049,22081,22083,22085,22087,22089,22297,22299,22301,22303,22305,22307,22309,22335,22337,22339,22391,22415,22417,22419,22453,22529",
                "040", 280229, 57.8d, "HH", "Bezirk", 7, 17838, "0");
    }

    private PlzTabBean plz22081() {
        return new PlzTabBean(6144, "22081", 10.0432270264886d, 53.5786038795972d, "Hamburg");
    }

    private PlzTabBean plz22085() {
        return new PlzTabBean(6146, "22085", 10.0151758890645d, 53.5746546949603d, "Hamburg");
    }

    private PlzTabBean plz22087() {
        return new PlzTabBean(6147, "22087", 10.0247288490807d, 53.5651225938106d, "Hamburg");
    }

    private PlaceBean uhlenhorst() {
        return new PlaceBean(26808, "02000000", "UHLENHORST", "Uhlenhorst", 53.57140d, 10.01890d, null,
                "22081,22085,22087", null, 0, 0, "HH", "Stadtteil", 8, 152980, null);
    }

    private void thenPlaceRelationshipExists(PlaceBean parent, PlaceBean child) {
        // parent should have child relationship to child
        Node parentNode = lookUpPlaceByLocId(parent);
        Node childNode = lookUpPlaceByLocId(child);

        assertTrue(parentNode.hasRelationship(Direction.INCOMING, PlaceRelationshipBuilder.Relationships.PART_OF));

        assertTrue(childNode.hasRelationship(Direction.OUTGOING, PlaceRelationshipBuilder.Relationships.PART_OF));

        assertEquals(parentNode,
                childNode.getSingleRelationship(PlaceRelationshipBuilder.Relationships.PART_OF, Direction.OUTGOING)
                        .getOtherNode(childNode));
    }

    private void whenThePlaceRelationshipsAreBuilt(PlaceBean place) {
   
        PlaceRelationshipBuilder prb = new PlaceRelationshipBuilder();
 
        prb.buildPartOfRelationshipForPlace(graphDb, placeNodeMap, place);
        
 

    }
    
    private Node lookUpPostalCodeByPlz(PlzTabBean plzBean) {
        Label plzLabel = DynamicLabel.label(OpenGeoDbProperties.POSTAL_CODE_LABEL);
        ResourceIterator<Node> plzIterator = graphDb
                .findNodesByLabelAndProperty(
                        plzLabel, 
                        OpenGeoDbProperties.PlzProperties.POSTAL_CODE.name(), 
                        plzBean.getPlz())
                .iterator();
        try {
            return plzIterator.next(); 
        } finally {
            plzIterator.close();
        }
    }

    private Node lookUpPlaceByLocId(PlaceBean place) {
        Label locLabel = DynamicLabel.label(OpenGeoDbProperties.OPENGEO_DB_LOCATION);
        ResourceIterator<Node> placeIterator = graphDb
                                                .findNodesByLabelAndProperty(
                                                        locLabel, 
                                                        OpenGeoDbProperties.LOC_ID, 
                                                        place.getId())
                .iterator();
        try {
            return placeIterator.next(); 
        } finally {
            placeIterator.close();
        }
    }
}
