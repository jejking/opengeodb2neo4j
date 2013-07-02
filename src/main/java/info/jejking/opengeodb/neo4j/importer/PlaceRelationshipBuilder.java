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

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

/**
 * Class to build relationships between nodes representing places and postal codes given the data extracted from the
 * tab-delimited place file.
 * 
 * @author jejking
 */
public class PlaceRelationshipBuilder {

    enum Relationships implements RelationshipType {
        PART_OF, POSTAL_CODE_FOR;
    }

    /**
     * Constructs relationships between nodes.
     * 
     * @param graphDb
     *            the graph db service
     * @param placeNodeMap
     *            a local, complete cache of nodes representing places, keyed on integer ID
     * @param plzNodeMap
     *            a local, complete cache of nodes representing postal codes, keyed on the postal code
     * @param placeBean
     *            the place for which relationships are to be built
     */
    public void buildRelationshipsForPlace(GraphDatabaseService graphDb, Map<Integer, Node> placeNodeMap,
            Map<String, Node> plzNodeMap, PlaceBean placeBean) {

        buildPartOfRelationshipForPlace(graphDb, placeNodeMap, placeBean);
        buildPostalCodeRelationships(graphDb, placeNodeMap, plzNodeMap, placeBean);

    }

    /**
     * Builds postal code relationships. The "plzs" field of the place bean parameter is split on the comma to give an
     * array of the postal codes for the area. The node for each postal code node is looked up and a
     * {@link Relationships#POSTAL_CODE_FOR} relationship from this node to the node corresponding to the place bean is
     * added.
     * 
     * @param graphDb
     *            the graph db service
     * @param placeNodeMap
     *            a local, complete cache of nodes representing places, keyed on integer ID
     * @param plzNodeMap
     *            a local, complete cache of nodes representing postal codes, keyed on the postal code
     * @param placeBean
     *            the place for which relationships are to be built
     */
    void buildPostalCodeRelationships(GraphDatabaseService graphDb, Map<Integer, Node> placeNodeMap,
            Map<String, Node> plzNodeMap, PlaceBean placeBean) {

        if (placeBean.getPlzs() != null) {
            Node placeNode = placeNodeMap.get(placeBean.getId());
            if (placeNode != null) {
                String[] postalCodes = placeBean.getPlzs().split(",");
                for (String postalCode : postalCodes) {
                    Node postalCodeNode = plzNodeMap.get(postalCode.trim());
                    if (postalCodeNode != null) {
                        postalCodeNode.createRelationshipTo(placeNode, Relationships.POSTAL_CODE_FOR);
                    }
                }
            }

        }

    }

    /**
     * If the place bean has an "of" property that has been set, i.e. is greater than 0, then the corresponding node
     * representing that place is looked up and (if found), a {@link Relationships#PART_OF} relationship is created from
     * the node representing the place to the place further up the nocde hierarchy.
     * 
     * @param graphDb
     *            the graph db service
     * @param placeNodeMap
     *            a local, complete cache of nodes representing places, keyed on integer ID
     * @param placeBean
     *            the place for which relationships are to be built
     */
    void buildPartOfRelationshipForPlace(GraphDatabaseService graphDb, Map<Integer, Node> placeNodeMap,
            PlaceBean placeBean) {

        if (placeBean.getOf() > 0) {
            Node placeNode = placeNodeMap.get(placeBean.getId());
            Node ofNode = placeNodeMap.get(placeBean.getOf());

            if (placeNode != null && ofNode != null) {
                placeNode.createRelationshipTo(ofNode, Relationships.PART_OF);
            }
        }

    }

}
