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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Main entry point into the application.
 * 
 * @author jejking
 * 
 */
public class ImporterRunner {

	private static final Options options = new Options();

	/**
	 * Runs the importer.
	 * 
	 * <p>
	 * Supply the following arguments:
	 * </p>
	 * <ol>
	 * <li>path to the tab-delimited place file</li>
	 * <li>path to the tab-delimited postal code file</li>
	 * <li>path to the directory where the Graph DB is to be found/created</li>
	 * </ol>
	 * 
	 * @param args
	 *            as above
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {

		initOptions();

		CommandLineParser parser = new GnuParser();
        CommandLine commandLine = parser.parse(options, args);
		
        if (commandLine.hasOption("h")) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("opengeodb2neo4j", options);
        } else {
        	String placeFilePath = commandLine.getOptionValue("p");
        	String zipFilePath = commandLine.getOptionValue("z");
        	String neo4jDirPath = commandLine.getOptionValue("n");
        	Importer importer = new Importer();
        	importer.doImport(placeFilePath, zipFilePath, neo4jDirPath);
        }
		
	}

	@SuppressWarnings("static-access")
	private static void initOptions() {
		options.addOption(OptionBuilder
							.withLongOpt("placeFile")
							.withArgName("placeFilePath")
							.hasArg()
							.withDescription("path to tab delimited place file")
							.create("p"));

		options.addOption(OptionBuilder
							.withLongOpt("zipCodesFile")
							.withArgName("zipCodesFilePath")
							.hasArg()
							.withDescription("path to tab delimited zip code (PLZ) file")
							.create("z"));

		options.addOption(OptionBuilder
							.withLongOpt("neoDir")
							.withArgName("neoDirPath")
							.hasArg()
							.withDescription("path to directory in which to create neo4j database")
							.create("n"));

		options.addOption("h", "help", false, "prints this message");
	}

}
