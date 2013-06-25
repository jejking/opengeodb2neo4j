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
import java.io.InputStream;
import java.util.List;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Simple class to parse the tab-delimited representation of a set 
 * of German <i>Postleitzahlen</i> (postal codes) as published by 
 * <a href="http://opengeodb.org/">Open GeoDB</a>.
 * 	
 * @author jejking
 */
public class PlzParser {
    
    /**
     * Dumb POJO representing a line of data from the 
     * tab-delimited file.
     */
    public static class PlzTabBean {

	private int id;
	private String plz;
	private double lon;
	private double lat;
	private String placeName;

	public PlzTabBean() {
	    super();
	}

	public PlzTabBean(int id, String plz, double lon, double lat,
		String placeName) {
	    super();
	    this.id = id;
	    this.plz = plz;
	    this.lon = lon;
	    this.lat = lat;
	    this.placeName = placeName;
	}

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public String getPlz() {
	    return plz;
	}

	public void setPlz(String plz) {
	    this.plz = plz;
	}

	public double getLon() {
	    return lon;
	}

	public void setLon(double lon) {
	    this.lon = lon;
	}

	public double getLat() {
	    return lat;
	}

	public void setLat(double lat) {
	    this.lat = lat;
	}

	public String getPlaceName() {
	    return placeName;
	}

	public void setPlaceName(String placeName) {
	    this.placeName = placeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + id;
	    long temp;
	    temp = Double.doubleToLongBits(lat);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    temp = Double.doubleToLongBits(lon);
	    result = prime * result + (int) (temp ^ (temp >>> 32));
	    result = prime * result
		    + ((placeName == null) ? 0 : placeName.hashCode());
	    result = prime * result + ((plz == null) ? 0 : plz.hashCode());
	    return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
		return true;
	    }
	    if (obj == null) {
		return false;
	    }
	    if (!(obj instanceof PlzTabBean)) {
		return false;
	    }
	    PlzTabBean other = (PlzTabBean) obj;
	    if (id != other.id) {
		return false;
	    }
	    if (Double.doubleToLongBits(lat) != Double
		    .doubleToLongBits(other.lat)) {
		return false;
	    }
	    if (Double.doubleToLongBits(lon) != Double
		    .doubleToLongBits(other.lon)) {
		return false;
	    }
	    if (placeName == null) {
		if (other.placeName != null) {
		    return false;
		}
	    } else if (!placeName.equals(other.placeName)) {
		return false;
	    }
	    if (plz == null) {
		if (other.plz != null) {
		    return false;
		}
	    } else if (!plz.equals(other.plz)) {
		return false;
	    }
	    return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "PlzTabBean [id=" + id + ", plz=" + plz + ", lon=" + lon
		    + ", lat=" + lat + ", placeName=" + placeName + "]";
	}

    }

    // note that for some reason latitude and longitude are the "wrong"
    // way round in the PLZ.tab file.
    private final String[] headers = {"id", "plz", "lon", "lat", "placeName"};
    
    private CellProcessor[] getProcessors() {
	CellProcessor[] processors = new CellProcessor[] {
		new NotNull(new ParseInt()), // id
		new NotNull(), // plz
		new NotNull(new ParseDouble()), // longitude
		new NotNull(new ParseDouble()), // latitude
		new NotNull() // name
	};
	return processors;
    }
    
    
    
    /**
     * Reads the data in from the given stream, constructing a list of {@link PlzTabBean} objects.
     * 
     * @param stream a stream to read in from. Will be closed.
     * @return list of {@link PlzTabBean} objects.
     * @throws IOException on IO issues
     */
    public List<PlzTabBean> readDataFromStream(InputStream stream) throws IOException {
	return new TabSeparatedBeanReader<>(PlzTabBean.class, getProcessors(), headers).readDataFromStream(stream, false);
    }
}
