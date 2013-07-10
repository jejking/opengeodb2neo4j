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

import info.jejking.opengeodb.neo4j.importer.OpenGeoDbProperties.PlaceNodeProperties;
import info.jejking.opengeodb.neo4j.importer.PlaceParser.PlaceBean;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * Class with functionality to create a {@link Node} corresponding to a {@link PlaceBean}.
 * 
 * @author jejking
 * 
 */
public class PlaceNodeMapper {
    
    /**
     * Creates the node for a place. Properties are mapped across. The OpenGeoDb ID and the name are indexed.
     * 
     * @param graphDb
     *            the graph db service
     * @param placeBean
     *            the place for which to create a node
     * @return node with properties set
     */
    public Node createPlaceNode(GraphDatabaseService graphDb, PlaceBean placeBean) {
        Node node = graphDb.createNode(
                            DynamicLabel.label(OpenGeoDbProperties.PLACE_LABEL),
                            DynamicLabel.label(OpenGeoDbProperties.OPENGEO_DB_LOCATION));
        
        
        node.setProperty(OpenGeoDbProperties.LOC_ID, placeBean.getId());
        if (placeBean.getAgs() != null) {
            node.setProperty(PlaceNodeProperties.AGS.name(), placeBean.getAgs());
        }
        if (placeBean.getAscii() != null) {
            node.setProperty(PlaceNodeProperties.ASCII.name(), placeBean.getAscii());
        }
        if (placeBean.getName() != null) {
            node.setProperty(PlaceNodeProperties.NAME.name(), placeBean.getName());
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
            node.addLabel(DynamicLabel.label(placeBean.getTyp()));
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
