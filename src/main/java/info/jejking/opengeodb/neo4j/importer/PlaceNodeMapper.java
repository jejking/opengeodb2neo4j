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

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

/**
 * Class with functionality to create a {@link Node} corresponding to a {@link PlaceBean}.
 * 
 * @author jejking
 * 
 */
public class PlaceNodeMapper {

    /**
     * Property names corresponding for a given subset of the properties in a line of the tab-delimited file. Postal
     * codes and hierarchical containment are (obviously) not mapped as node properties but as relationships.
     */
    enum PlaceNodeProperties {
        /**
         * The ID in OpenGeoDB.
         */
        LOC_ID,
        /**
         * Amtlicher Gemeinde-Schlüssel. German ID for the administrative area.
         */
        AGS,
        /**
         * Name in capitals and ASCII only - i.e. without any German characters.
         */
        ASCII,
        /**
         * Name in mixed upper and lower case, as usual. Contains German characters.
         */
        NAME,
        /**
         * Latitude in degrees, as double.
         */
        LATITUDE,
        /**
         * Longitude in degrees, as double.
         */
        LONGITUDE,
        /**
         * Where given, the nearest official local government office for citizens' affairs.
         */
        AMT,
        /**
         * Dialing code.
         */
        DIALING_CODE,
        /**
         * Population.
         */
        POPULATION,
        /**
         * Area, in square kilometers.
         */
        AREA,
        /**
         * The first part of the German number plate, indicating region of origin.
         */
        NUMBER_PLATE_CODE,
        /**
         * The "type" of place, in German - e.g. city, federal state, commune.
         */
        TYPE,
        /**
         * The level in the OpenGeoDB hierarchy.
         */
        LEVEL,
        /**
         * Some indication of validity - 1 meaning "invalid", 0 meaning "valid" and its absence also indicating
         * validity.
         */
        INVALID;
    }

    /**
     * Creates the node for a place. Properties are mapped across. The OpenGeoDb ID and the name are indexed.
     * 
     * @param graphDb
     *            the graph db service
     * @param nodeIndex
     *            a node index
     * @param placeBean
     *            the place for which to create a node
     * @return node with properties set
     */
    public Node createPlaceNode(GraphDatabaseService graphDb, Index<Node> nodeIndex, PlaceBean placeBean) {
        // TODO. Consider use of Labels and auto-indexing
        Node node = graphDb.createNode();
        node.setProperty(PlaceNodeProperties.LOC_ID.name(), placeBean.getId());
        if (placeBean.getAgs() != null) {
            node.setProperty(PlaceNodeProperties.AGS.name(), placeBean.getAgs());
            nodeIndex.add(node, PlaceNodeProperties.LOC_ID.name(), placeBean.getId());
        }
        if (placeBean.getAscii() != null) {
            node.setProperty(PlaceNodeProperties.ASCII.name(), placeBean.getAscii());
        }
        if (placeBean.getName() != null) {
            node.setProperty(PlaceNodeProperties.NAME.name(), placeBean.getName());
            nodeIndex.add(node, PlaceNodeProperties.NAME.name(), placeBean.getName());
        }
        if (placeBean.getLat() > 0) {
            node.setProperty(PlaceNodeProperties.LATITUDE.name(), placeBean.getLat());
        }
        if (placeBean.getLon() > 0) {
            node.setProperty(PlaceNodeProperties.LONGITUDE.name(), placeBean.getLon());
        }
        if (placeBean.getAmt() != null) {
            node.setProperty(PlaceNodeProperties.AMT.name(), placeBean.getAmt());
        }
        if (placeBean.getVorwahl() != null) {
            node.setProperty(PlaceNodeProperties.DIALING_CODE.name(), placeBean.getVorwahl());
        }
        if (placeBean.getEinwohner() > 0) {
            node.setProperty(PlaceNodeProperties.POPULATION.name(), placeBean.getEinwohner());
        }
        if (placeBean.getFlaeche() > 0) {
            node.setProperty(PlaceNodeProperties.AREA.name(), placeBean.getFlaeche());
        }
        if (placeBean.getKz() != null) {
            node.setProperty(PlaceNodeProperties.NUMBER_PLATE_CODE.name(), placeBean.getKz());
        }
        if (placeBean.getTyp() != null) {
            node.setProperty(PlaceNodeProperties.TYPE.name(), placeBean.getTyp());
        }
        if (placeBean.getLevel() > 0) {
            node.setProperty(PlaceNodeProperties.LEVEL.name(), placeBean.getLevel());
        }
        if (placeBean.getInvalid() != null) {
            node.setProperty(PlaceNodeProperties.INVALID.name(), placeBean.getInvalid());
        }
        return node;
    }

}
