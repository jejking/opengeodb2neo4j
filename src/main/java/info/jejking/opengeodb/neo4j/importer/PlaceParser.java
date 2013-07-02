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

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Class to parse the tab delimited data file for Germany into a list of data beans representing the contents of the
 * file for further processing.
 * 
 * @author jejking
 * 
 */
public class PlaceParser {

    /**
     * Dumb POJO to represent the contents of a line of data in the file.
     */
    public static class PlaceBean {

        private int id;
        private String ags;
        private String ascii;
        private String name;
        private double lat;
        private double lon;
        private String amt;
        private String plzs;
        private String vorwahl;
        private int einwohner;
        private double flaeche;
        private String kz;
        private String typ;
        private int level;
        private int of;
        private String invalid;

        public PlaceBean() {
            super();
        }

        public PlaceBean(int id, String ags, String ascii, String name, double lat, double lon, String amt,
                String plzs, String vorwahl, int einwohner, double flaeche, String kz, String typ, int level, int of,
                String invalid) {
            super();
            this.id = id;
            this.ags = ags;
            this.ascii = ascii;
            this.name = name;
            this.lat = lat;
            this.lon = lon;
            this.amt = amt;
            this.plzs = plzs;
            this.vorwahl = vorwahl;
            this.einwohner = einwohner;
            this.flaeche = flaeche;
            this.kz = kz;
            this.typ = typ;
            this.level = level;
            this.of = of;
            this.invalid = invalid;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * @return the ags
         */
        public String getAgs() {
            return ags;
        }

        /**
         * @param ags
         *            the ags to set
         */
        public void setAgs(String ags) {
            this.ags = ags;
        }

        /**
         * @return the ascii
         */
        public String getAscii() {
            return ascii;
        }

        /**
         * @param ascii
         *            the ascii to set
         */
        public void setAscii(String ascii) {
            this.ascii = ascii;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the lat
         */
        public double getLat() {
            return lat;
        }

        /**
         * @param lat
         *            the lat to set
         */
        public void setLat(double lat) {
            this.lat = lat;
        }

        /**
         * @return the lon
         */
        public double getLon() {
            return lon;
        }

        /**
         * @param lon
         *            the lon to set
         */
        public void setLon(double lon) {
            this.lon = lon;
        }

        /**
         * @return the amt
         */
        public String getAmt() {
            return amt;
        }

        /**
         * @param amt
         *            the amt to set
         */
        public void setAmt(String amt) {
            this.amt = amt;
        }

        /**
         * @return the plzs
         */
        public String getPlzs() {
            return plzs;
        }

        /**
         * @param plzs
         *            the plzs to set
         */
        public void setPlzs(String plzs) {
            this.plzs = plzs;
        }

        /**
         * @return the vorwahl
         */
        public String getVorwahl() {
            return vorwahl;
        }

        /**
         * @param vorwahl
         *            the vorwahl to set
         */
        public void setVorwahl(String vorwahl) {
            this.vorwahl = vorwahl;
        }

        /**
         * @return the einwohner
         */
        public int getEinwohner() {
            return einwohner;
        }

        /**
         * @param einwohner
         *            the einwohner to set
         */
        public void setEinwohner(int einwohner) {
            this.einwohner = einwohner;
        }

        /**
         * @return the flaeche
         */
        public double getFlaeche() {
            return flaeche;
        }

        /**
         * @param flaeche
         *            the flaeche to set
         */
        public void setFlaeche(double flaeche) {
            this.flaeche = flaeche;
        }

        /**
         * @return the kz
         */
        public String getKz() {
            return kz;
        }

        /**
         * @param kz
         *            the kz to set
         */
        public void setKz(String kz) {
            this.kz = kz;
        }

        /**
         * @return the typ
         */
        public String getTyp() {
            return typ;
        }

        /**
         * @param typ
         *            the typ to set
         */
        public void setTyp(String typ) {
            this.typ = typ;
        }

        /**
         * @return the level
         */
        public int getLevel() {
            return level;
        }

        /**
         * @param level
         *            the level to set
         */
        public void setLevel(int level) {
            this.level = level;
        }

        /**
         * @return the of
         */
        public int getOf() {
            return of;
        }

        /**
         * @param of
         *            the of to set
         */
        public void setOf(int of) {
            this.of = of;
        }

        /**
         * @return the invalid
         */
        public String getInvalid() {
            return invalid;
        }

        /**
         * @param invalid
         *            the invalid to set
         */
        public void setInvalid(String invalid) {
            this.invalid = invalid;
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
            result = prime * result + ((ags == null) ? 0 : ags.hashCode());
            result = prime * result + ((amt == null) ? 0 : amt.hashCode());
            result = prime * result + ((ascii == null) ? 0 : ascii.hashCode());
            result = prime * result + einwohner;
            long temp;
            temp = Double.doubleToLongBits(flaeche);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + id;
            result = prime * result + ((invalid == null) ? 0 : invalid.hashCode());
            result = prime * result + ((kz == null) ? 0 : kz.hashCode());
            temp = Double.doubleToLongBits(lat);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + level;
            temp = Double.doubleToLongBits(lon);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + of;
            result = prime * result + ((plzs == null) ? 0 : plzs.hashCode());
            result = prime * result + ((typ == null) ? 0 : typ.hashCode());
            result = prime * result + ((vorwahl == null) ? 0 : vorwahl.hashCode());
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
            if (!(obj instanceof PlaceBean)) {
                return false;
            }
            PlaceBean other = (PlaceBean) obj;
            if (ags == null) {
                if (other.ags != null) {
                    return false;
                }
            } else if (!ags.equals(other.ags)) {
                return false;
            }
            if (amt == null) {
                if (other.amt != null) {
                    return false;
                }
            } else if (!amt.equals(other.amt)) {
                return false;
            }
            if (ascii == null) {
                if (other.ascii != null) {
                    return false;
                }
            } else if (!ascii.equals(other.ascii)) {
                return false;
            }
            if (einwohner != other.einwohner) {
                return false;
            }
            if (Double.doubleToLongBits(flaeche) != Double.doubleToLongBits(other.flaeche)) {
                return false;
            }
            if (id != other.id) {
                return false;
            }
            if (invalid == null) {
                if (other.invalid != null) {
                    return false;
                }
            } else if (!invalid.equals(other.invalid)) {
                return false;
            }
            if (kz == null) {
                if (other.kz != null) {
                    return false;
                }
            } else if (!kz.equals(other.kz)) {
                return false;
            }
            if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat)) {
                return false;
            }
            if (level != other.level) {
                return false;
            }
            if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon)) {
                return false;
            }
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (of != other.of) {
                return false;
            }
            if (plzs == null) {
                if (other.plzs != null) {
                    return false;
                }
            } else if (!plzs.equals(other.plzs)) {
                return false;
            }
            if (typ == null) {
                if (other.typ != null) {
                    return false;
                }
            } else if (!typ.equals(other.typ)) {
                return false;
            }
            if (vorwahl == null) {
                if (other.vorwahl != null) {
                    return false;
                }
            } else if (!vorwahl.equals(other.vorwahl)) {
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
            return "PlaceBean [id=" + id + ", ags=" + ags + ", ascii=" + ascii + ", name=" + name + ", lat=" + lat
                    + ", lon=" + lon + ", amt=" + amt + ", plzs=" + plzs + ", vorwahl=" + vorwahl + ", einwohner="
                    + einwohner + ", flaeche=" + flaeche + ", kz=" + kz + ", typ=" + typ + ", level=" + level + ", of="
                    + of + ", invalid=" + invalid + "]";
        }

    }

    // maps the columns in the file to fields on the bean
    private final String[] headers = { "id", "ags", "ascii", "name", "lat", "lon", "amt", "plzs", "vorwahl",
            "einwohner", "flaeche", "kz", "typ", "level", "of", "invalid" };

    private CellProcessor[] getCellProcessors() {

        /*
         * Note that due to the way in which the data is contructed we can only assume that the ID field will be there.
         * This means that all other columns are marked Optional. In some cases we need to trim the string as some data
         * has white space lurking around.
         */
        return new CellProcessor[] { new NotNull(new Trim(new ParseInt())), // id
                new Optional(), // ags, not all lines have one
                new Optional(), // ascii
                new Optional(), // name
                new Optional(new Trim(new ParseDouble())), // lat
                new Optional(new Trim(new ParseDouble())), // lon,
                new Optional(), // amt
                new Optional(), // plzs
                new Optional(), // vorwahl
                new Optional(new Trim(new ParseInt())), // einwohner
                new Optional(new Trim(new ParseDouble())), // fläche
                new Optional(), // kz
                new Optional(), // typ
                new Optional(new Trim(new ParseInt())), // level
                new Optional(new Trim(new ParseInt())), // of
                new Optional() // invalid
        };
    }

    /**
     * Reads the data in from the given stream, constructing a list of {@link PlaceBean} objects.
     * 
     * @param stream
     *            a stream to read in from. Will be closed.
     * @return list of {@link PlaceBean} objects.
     * @throws IOException
     *             on IO issues
     */
    public List<PlaceBean> readDataFromStream(InputStream stream) throws IOException {
        return new TabSeparatedBeanReader<>(PlaceBean.class, getCellProcessors(), headers).readDataFromStream(stream,
                true);
    }
}
