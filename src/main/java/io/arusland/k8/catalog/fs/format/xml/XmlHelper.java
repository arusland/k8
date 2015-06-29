package io.arusland.k8.catalog.fs.format.xml;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

/**
 * Created by ruslan on 29.06.2015.
 */
public class XmlHelper  {
    private static Logger logger = LoggerFactory.getLogger(XmlHelper.class);

    public void load(File file) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();

            try(InputStream is = new FileInputStream(file)){
                SaxParser handler   = new SaxParser();
                parser.parse(is, handler);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private class SaxParser extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            logger.debug("startElement: " + localName);
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            logger.debug("endElement: " + localName);
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            logger.debug("characters: " + ch);

            super.characters(ch, start, length);
        }
    }
}
