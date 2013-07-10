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

/**
 * Some properties for use in populating the graph database and the indexes.
 * 
 * @author jejking
 */
public final class OpenGeoDbProperties {

    public static final String OPENGEO_DB_LOCATION = "Location";
    public static final String PLACE_LABEL = "Place";
    public static final String POSTAL_CODE_LABEL = "PostalCode";

    public static final String LOC_ID = "LOC_ID";
    
    /**
     * Property names corresponding for a given subset of the properties in a line of the tab-delimited file. Postal codes
     * and hierarchical containment are (obviously) not mapped as node properties but as relationships.
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
         * Some indication of validity - 1 meaning "invalid", 0 meaning "valid" and its absence also indicating validity.
         */
        INVALID;
    }

    /**
     * Properties to be set on the {@link Node} for a line in the tab-delimited file.
     */
    enum PlzProperties {
        /**
         * OpenGeoDB id.
         */
        LOC_ID,
        /**
         * 5 digit German postal code as string.
         */
        POSTAL_CODE,
        /**
         * Latitude in degrees, as double.
         */
        LATITUDE,
        /**
         * Longitude in degrees, as double.
         */
        LONGITUDE,
        /**
         * Name "representing" the postal code. Note that outside large cities a single postal code may represent many
         * places, so one has been chosen by the OpenGeoDB to represent the postal code.
         */
        PLACE_NAME;
    }
    
}


