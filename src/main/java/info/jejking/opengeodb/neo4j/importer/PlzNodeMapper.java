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

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * Class with functionality to create a {@link Node} corresponding to a {@link PlzTabBean}.
 * 
 * @author jejking
 */
public class PlzNodeMapper {

    /**
     * Creates the node for a postal code. Properties are mapped across. The OpenGeoDb ID and the postal code are
     * indexed.
     * 
     * @param graphDb
     *            the graph db service
     * @param plzBean
     *            the postal code for which to create a node
     * @return node with properties set
     */
    public Node createPlzNode(GraphDatabaseService graphDb, PlzTabBean plzBean) {
        Node node = graphDb.createNode(
                                DynamicLabel.label(OpenGeoDbProperties.POSTAL_CODE_LABEL),
                                DynamicLabel.label(OpenGeoDbProperties.OPENGEO_DB_LOCATION));

        node.setProperty(OpenGeoDbProperties.LOC_ID, plzBean.getId());

        node.setProperty(PlzProperties.POSTAL_CODE.name(), plzBean.getPlz());

        node.setProperty(PlzProperties.LATITUDE.name(), plzBean.getLat());
        node.setProperty(PlzProperties.LONGITUDE.name(), plzBean.getLon());
        node.setProperty(PlzProperties.PLACE_NAME.name(), plzBean.getPlaceName());

        return node;
    }

}
