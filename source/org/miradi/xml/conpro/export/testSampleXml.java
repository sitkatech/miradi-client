/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.conpro.export;

import java.io.InputStream;
import java.net.URL;

import org.miradi.main.EAM;
import org.xml.sax.InputSource;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.rng.CompactSchemaReader;
import com.thaiopensource.validate.rng.RngProperty;

//FIXME rename this to something meaningful or extract the validation data into own class
public class testSampleXml
{
	public boolean isValid(InputStream xmlInputStream) throws Exception
	{
		PropertyMapBuilder properties = getValidatorProperties();
		URL resourceURL = EAM.getResourceURL("xml/ConProMiradi.rnc");
		InputSource schemaInputSource = new InputSource(resourceURL.openStream());
		SchemaReader schemaReader = CompactSchemaReader.getInstance();
		ValidationDriver validationDriver = new ValidationDriver(properties.toPropertyMap(), schemaReader);
		if (validationDriver.loadSchema(schemaInputSource))
		{
			InputSource xmlInputSource = new InputSource(xmlInputStream);
			return validationDriver.validate(xmlInputSource);
		}
		
		throw new Exception("Could not load schema");
	}

	private PropertyMapBuilder getValidatorProperties()
	{
		PropertyMapBuilder properties = new PropertyMapBuilder();
		final boolean ALLOW_MISSING_ELEMENTS = false;
		if (ALLOW_MISSING_ELEMENTS)
		{
			RngProperty.FEASIBLE.add(properties);
		}
		
		return properties;
	}
	
	public static void main(String[] args)
	{
		try
		{
			URL xmlUrl = EAM.getResourceURL("xml/test.xml");
			new testSampleXml().isValid(xmlUrl.openStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
