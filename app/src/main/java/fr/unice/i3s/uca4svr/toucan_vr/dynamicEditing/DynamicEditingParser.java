/*
 * Copyright 2017 Université Nice Sophia Antipolis (member of Université Côte d'Azur), CNRS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.unice.i3s.uca4svr.toucan_vr.dynamicEditing;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DynamicEditingParser {

    //private attributes
    private DynamicEditingHolder dynamicEditingHolder;
    private SnapChange snapchange;
    private String text;
    private String[] strTiles;
    private int[] intTiles;
    private String dynamicEditingFN;

    public DynamicEditingParser(String dynamicEditingFN) {
        this.dynamicEditingFN = dynamicEditingFN;
    }

    public void parse(DynamicEditingHolder dynamicEditingHolder, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        xpploop:
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("snapchange")) {
                        snapchange = new SnapChange();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("snapchange")) {
                        // add video object to list and check if all the parameters are set
                        if (snapchange != null && snapchange.isOK()) {
                            dynamicEditingHolder.add(snapchange);
                        } else {
                            throw new XmlPullParserException("Not well formed snapchange tag!");
                        }
                    } else if (tagname.equalsIgnoreCase("milliseconds")) {
                        if (snapchange != null) snapchange.setMilliseconds(Integer.parseInt(text));
                    } else if (tagname.equalsIgnoreCase("roiDegrees")) {
                        if (snapchange != null) snapchange.setRoiDegrees(Integer.parseInt(text));
                    } else if (tagname.equalsIgnoreCase("foVTile")) {
                        strTiles = text.split(",");
                        intTiles = new int[strTiles.length];
                        for (int i = 0; i < strTiles.length; i++) {
                            intTiles[i] = Integer.parseInt(strTiles[i]);
                        }
                        if (snapchange != null) snapchange.setFoVTiles(intTiles);
                    } else if (tagname.equalsIgnoreCase("snapchanges")) {
                        break xpploop;
                    }

                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        //check if the file is empty
        if (dynamicEditingHolder.empty()) {
            throw new XmlPullParserException("File is empty!");
        } else {
            //Snapchanges have been stored into the holder object. Preparing the first snapchange.
            dynamicEditingHolder.getNextSnapChange();
        }

        Log.v("M360", "Parsed " + dynamicEditingHolder.getSnapChanges().size() +" snapchanges");
    }

    //Main parse method
    public void parse(DynamicEditingHolder dynamicEditingHolder) throws XmlPullParserException, IOException {
        File file = new File("/storage/emulated/0/" + dynamicEditingFN);
        XmlPullParserFactory factory;
        XmlPullParser parser;
        factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        parser = factory.newPullParser();
        parser.setInput(new FileInputStream(file), "UTF-8");
        parse(dynamicEditingHolder, parser);
    }

}
