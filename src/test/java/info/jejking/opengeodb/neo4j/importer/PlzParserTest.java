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

import static org.junit.Assert.*;

import info.jejking.opengeodb.neo4j.importer.PlzParser.PlzTabBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Basic test of {@link PlzParser}.
 * 
 * @author jejking
 */
public class PlzParserTest {

    PlzParser parser = new PlzParser();
    
    List<PlzTabBean> expectedData;
    List<PlzTabBean> actualData;
    
    @Test
    public void test() throws IOException {
	
	givenExpectedData();
	
	whenTheStreamIsParsed();
	
	thenTheDataIsAsExpected();
    }


    private void thenTheDataIsAsExpected() {
	assertNotNull(this.actualData);
	assertEquals(this.expectedData.size(), this.actualData.size());
	for (PlzTabBean bean : this.expectedData) {
	    assertTrue("Could not find: " + bean, this.actualData.contains(bean));
	}
    }


    private void whenTheStreamIsParsed() throws IOException {
	InputStream testDataStream = PlzParserTest.class.getResourceAsStream("/PLZ.tab.txt");
	this.actualData = this.parser.readDataFromStream(testDataStream);
    }


    private void givenExpectedData() {
	List<PlzTabBean> list = new ArrayList<>(2);
	list.add(new PlzTabBean(5078, "01067", 13.7210676148814d, 51.0600336463379d, "Dresden"));
	list.add(new PlzTabBean(5079, "01069", 13.7389066401609d, 51.039558876083d, "Dresden"));
	
	this.expectedData = list;
    }

}
