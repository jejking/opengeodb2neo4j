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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * Driver class for {@link PlaceParser}.
 * 
 * @author jejking
 */
public class PlaceParserMain {

    private static final Logger LOGGER = Logger.getLogger(PlaceParserMain.class.getName());
    
    /**
     * Parses a tab-delimited file of place data whose path is specified by the first argument.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        PlaceParser pp = new PlaceParser();
        InputStream is = new FileInputStream(args[0]);
        List<PlaceBean> data = pp.readDataFromStream(is);
        LOGGER.info(String.format("Read %d records from file %s", data.size(), args[0]));
    }

}
