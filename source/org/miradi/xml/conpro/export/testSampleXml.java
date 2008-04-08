/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.conpro.export;

import java.net.URL;

import org.miradi.main.EAM;
import org.xml.sax.InputSource;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.rng.CompactSchemaReader;
import com.thaiopensource.validate.rng.RngProperty;

public class testSampleXml
{
	public void validate() throws Exception
	{
		PropertyMapBuilder properties = getValidatorProperties();
		URL resourceURL = EAM.getResourceURL("xml/ConservationProjectsSchema_compactRNG.rnc");
		InputSource schemaInputSource = new InputSource(resourceURL.openStream());
		SchemaReader schemaReader = CompactSchemaReader.getInstance();
		ValidationDriver validationDriver = new ValidationDriver(properties.toPropertyMap(), schemaReader);
		if (validationDriver.loadSchema(schemaInputSource))
		{
			URL xmlUrl = EAM.getResourceURL("xml/test.xml");
			InputSource xmlInputSource = new InputSource(xmlUrl.openStream());
			System.out.println("Is valid xml doc: " + validationDriver.validate(xmlInputSource));
		}
		else
		{
			throw new Exception("Could not load schema");
		}
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
			new testSampleXml().validate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
