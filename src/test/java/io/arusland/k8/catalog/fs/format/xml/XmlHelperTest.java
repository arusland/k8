package io.arusland.k8.catalog.fs.format.xml;

import io.arusland.k8.TestConfig;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by ruslan on 29.06.2015.
 */
public class XmlHelperTest extends TestCase {
    private static Logger logger = LoggerFactory.getLogger(XmlHelperTest.class);

    public void test(){
        XmlHelper helper = new XmlHelper();

        helper.load(new File(TestConfig.TEST_DATA_PATH + "\\folder3\\drivers1.xml"));

        assertEquals(2, helper.getAttributeNames().size());
        assertEquals(2, helper.getAttributeValues().size());
        assertEquals(8, helper.getTags().size());

        logger.debug("tag: " + helper.getTags());
        logger.debug("attr names: " + helper.getAttributeNames());
        logger.debug("attr values: " + helper.getAttributeValues());
        logger.debug("content: " + helper.getContent());
    }
}
