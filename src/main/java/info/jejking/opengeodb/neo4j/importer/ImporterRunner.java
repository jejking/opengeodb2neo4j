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

import java.io.IOException;

/**
 * Main entry point into the application.
 * @author jejking
 *
 */
public class ImporterRunner {

    /**
     * Runs the importer.
     * 
     * <p>Supply the following arguments:</p>
     * <ol>
     * <li>path to the tab-delimited place file</li>
     * <li>path to the tab-delimited postal code file</li>
     * <li>path to the directory where the Graph DB is to be found/created</li>
     * </ol>
     * 
     * @param args as above
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
	Importer importer = new Importer();
	importer.doImport(args[0], args[1], args[2]);
    }

}
