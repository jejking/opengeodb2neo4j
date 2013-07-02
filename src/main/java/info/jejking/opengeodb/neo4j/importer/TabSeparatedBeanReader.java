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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Class to help read in data in tab-separated format, wrapping some functionality of <a
 * href="http://supercsv.sourceforge.net/">SuperCSV</a>.
 * 
 * @author jejking
 * @param <T>
 *            the type of bean
 */
class TabSeparatedBeanReader<T> {

    private static final Logger LOGGER = Logger.getLogger(TabSeparatedBeanReader.class.getName());
    
    private final Class<T> clazz;
    private final CellProcessor[] cellProcessors;
    private final String[] headers;

    /**
     * Constructs reader with details of class to process, the necessary {@link CellProcessor} instances and the
     * necessary headers.
     * 
     * @param clazz
     *            the class to process
     * @param cellProcessors
     *            the cell processors
     * @param headers
     *            the headers
     */
    public TabSeparatedBeanReader(Class<T> clazz, CellProcessor[] cellProcessors, String[] headers) {
        super();
        this.clazz = clazz;
        this.cellProcessors = cellProcessors;
        this.headers = headers;
    }

    /**
     * Reads in a list of T from a tab separated stream.
     * 
     * @param stream
     *            the stream, will be closed when finished with
     * @param readFirstLine
     *            whether the first line of the file should be considered part of the data to process or not
     * @return list of T
     * @throws IOException
     *             on IO problems
     */
    public List<T> readDataFromStream(InputStream stream, boolean readFirstLine) throws IOException {

        List<T> typeBeans = new LinkedList<>();
        try (ICsvBeanReader beanReader = new CsvBeanReader(new BufferedReader(new InputStreamReader(stream)),
                CsvPreference.TAB_PREFERENCE)) {
            if (readFirstLine) {
                beanReader.getHeader(true);
            }
            T typeBean;
            boolean carryOn = true;
            while (carryOn) {
                try {
                    typeBean = beanReader.read(clazz, headers, cellProcessors);
                    if (typeBean != null) {
                        typeBeans.add(typeBean);
                    } else {
                        carryOn = false;
                    }
                } catch (SuperCsvException e) {
                    // we need this as the data file MAY contain rubbish
                    LOGGER.warning("Ignoring data error in line: " + beanReader.getLineNumber());
                }

            }
        }
        return typeBeans;
    }

}
