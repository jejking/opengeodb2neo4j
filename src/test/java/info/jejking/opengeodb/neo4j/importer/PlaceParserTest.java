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

import info.jejking.opengeodb.neo4j.importer.PlaceParser.PlaceBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Basic test of {@link PlaceParser}.
 * 
 * @author jejking
 */
public class PlaceParserTest {

    private PlaceParser placeParser = new PlaceParser();

    private List<PlaceBean> expectedPlaces;
    private List<PlaceBean> actualPlaces;

    @Test
    public void shouldParsePlaceData() throws IOException {
        givenExpectedData();

        whenTheStreamIsParsed();

        thenTheDataIsAsExpected();
    }

    private void thenTheDataIsAsExpected() {
        assertNotNull(this.actualPlaces);
        assertEquals(this.expectedPlaces.size(), this.actualPlaces.size());
        for (PlaceBean bean : this.expectedPlaces) {
            assertTrue("Could not find: " + bean, this.actualPlaces.contains(bean));
        }

    }

    private void whenTheStreamIsParsed() throws IOException {
        InputStream stream = PlaceParserTest.class.getResourceAsStream("/DE.tab.txt");
        this.actualPlaces = this.placeParser.readDataFromStream(stream);

    }

    private void givenExpectedData() {
        List<PlaceBean> list = new ArrayList<>(3);
        addPlace(deutschland(), list);
        addPlace(hamburg(), list);
        addPlace(barmbekSued(), list);
        this.expectedPlaces = list;
    }

    private void addPlace(PlaceBean place, List<PlaceBean> list) {
        list.add(place);
    }

    private PlaceBean barmbekSued() {
        PlaceBean barmbekSued = new PlaceBean();
        barmbekSued.setId(26722);
        barmbekSued.setAgs("02000000");
        barmbekSued.setAscii("BARMBEK-SUED");
        barmbekSued.setName("Barmbek-Süd");
        barmbekSued.setLat(53.5796d);
        barmbekSued.setLon(10.03838d);
        barmbekSued.setPlzs("22081,22083,22085,22305");
        barmbekSued.setKz("HH");
        barmbekSued.setLevel(8);
        barmbekSued.setTyp("Stadtteil");
        barmbekSued.setOf(152980);
        return barmbekSued;
    }

    private PlaceBean hamburg() {
        PlaceBean hh = new PlaceBean();
        hh.setId(17838);
        hh.setAgs("02000000");
        hh.setAscii("HAMBURG");
        hh.setName("Hamburg");
        hh.setLat(53.554423d);
        hh.setLon(9.994583d);
        hh.setPlzs("20038,20088,20095,20097,20099,20144,20146,20148,20149,20249,20251,20253,20255,20257,20259,20350,20354,20355,20357,20359,20457,20459,20535,20537,20539,21029,21031,21033,21035,21037,21039,21073,21075,21077,21079,21107,21109,21129,21147,21149,22041,22043,22045,22047,22049,22081,22083,22085,22087,22089,22111,22113,22115,22117,22119,22143,22145,22147,22149,22159,22175,22177,22179,22297,22299,22301,22303,22305,22307,22309,22335,22337,22339,22359,22391,22393,22395,22397,22399,22415,22417,22419,22453,22455,22457,22459,22523,22525,22527,22529,22547,22549,22559,22587,22589,22605,22607,22609,22761,22763,22765,22767,22769"); // quite
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // a
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // few
        hh.setVorwahl("040");
        hh.setEinwohner(1734830);
        hh.setFlaeche(755);
        hh.setLevel(6);
        hh.setTyp("Freie und Hansestadt");
        hh.setOf(526);
        hh.setKz("HH");
        return hh;
    }

    private PlaceBean deutschland() {
        PlaceBean d = new PlaceBean();
        d.setId(105);
        d.setAgs("D");
        d.setAscii("DEUTSCHLAND");
        d.setName("Bundesrepublik Deutschland");
        d.setLat(51.16766d);
        d.setLon(10.42498d);
        d.setEinwohner(82169000);
        d.setFlaeche(357104);
        d.setKz("D");
        d.setLevel(2);
        d.setOf(104);
        return d;
    }

}
