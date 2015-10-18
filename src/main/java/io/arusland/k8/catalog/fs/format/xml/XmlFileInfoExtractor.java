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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 29.06.2015.
 */
public class XmlFileInfoExtractor {
    private static Logger logger = LoggerFactory.getLogger(XmlFileInfoExtractor.class);
    private final List<String> tags = new LinkedList<>();
    private final List<String> attributeNames = new LinkedList<>();
    private final List<String> attributeValues = new LinkedList<>();
    private final List<String> contentLines = new LinkedList<>();
    private String content;

    public void load(File file) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();

            try (InputStream is = new FileInputStream(file)) {
                SaxParser handler = new SaxParser();
                parser.parse(is, handler);

                StringBuilder builder = new StringBuilder();

                contentLines.forEach(p -> {
                    builder.append(p);
                    builder.append(System.getProperty("line.separator"));
                });

                contentLines.clear();
                content = builder.toString();
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getAttributeNames() {
        return attributeNames;
    }

    public List<String> getAttributeValues() {
        return attributeValues;
    }

    public String getContent() {
        return content;
    }

    private class SaxParser extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            addIfNone(tags, qName);

            for (int i = 0; i < attributes.getLength(); i++) {
                addIfNone(attributeNames, attributes.getQName(i));
                addIfNone(attributeValues, attributes.getValue(i));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String value = new String(ch, start, length).trim();
            if (value.length() == 0) return; // ignore white space

            addIfNone(contentLines, value);
        }

        private void addIfNone(List<String> list, String value) {
            if (!list.contains(value)) {
                list.add(value);
            }
        }
    }
}
