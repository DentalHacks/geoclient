package gov.nyc.doitt.gis.geoclient.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nyc.doitt.gis.geoclient.function.Configuration;
import gov.nyc.doitt.gis.geoclient.function.DefaultConfiguration;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ConfigurationConverterTest
{
    private ConfigurationConverter converter;
    private XStream xStream;
    
    @Before
    public void setUp() {
        converter = new ConfigurationConverter();
        xStream = new XStream(new DomDriver());
        xStream.registerConverter(converter);
        xStream.alias("configuration", DefaultConfiguration.class);
    }

    @Test
    public void testCanConvert()
    {
        assertTrue(this.converter.canConvert(DefaultConfiguration.class));
        assertFalse(this.converter.canConvert(Configuration.class));
    }

    @Test
    public void testUnmarshalEmpty()
    {
        DefaultConfiguration result = (DefaultConfiguration) xStream.fromXML("<configuration></configuration>");
        assertNotNull(result);
        assertNotNull(result.requiredArguments());
        assertTrue(result.requiredArguments().isEmpty());
    }

    @Test
    public void testUnmarshal()
    {
        DefaultConfiguration result = (DefaultConfiguration) xStream.fromXML(
                  "<configuration>"
                +   "<requiredArgument name=\"arg1\" value=\"val1\"/>"
                +   "<requiredArgument name=\"arg2\" value=\"val2\"/>"
                + "</configuration>");
        assertNotNull(result);
        assertNotNull(result.requiredArguments());
        assertEquals("val1",result.requiredArguments().get("arg1"));
        assertEquals("val2",result.requiredArguments().get("arg2"));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testMarshal()
    {
        this.converter.marshal(new DefaultConfiguration(), null, null);
    }

}
