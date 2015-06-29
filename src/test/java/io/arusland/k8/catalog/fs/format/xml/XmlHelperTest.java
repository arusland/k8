package io.arusland.k8.catalog.fs.format.xml;

import io.arusland.k8.TestConfig;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by ruslan on 29.06.2015.
 */
public class XmlHelperTest extends TestCase {
    public void test(){
        XmlHelper helper = new XmlHelper();

        helper.load(new File(TestConfig.TEST_DATA_PATH + "\\folder3\\drivers1.xml"));

        // TODO: !!!
    }
}
