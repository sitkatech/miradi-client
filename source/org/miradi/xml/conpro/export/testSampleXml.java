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

public class testSampleXml
{
	public void validate() throws Exception
	{
		PropertyMapBuilder properties = new PropertyMapBuilder();
		//RngProperty.FEASIBLE.add(properties);
		URL resourceURL = EAM.getResourceURL("xml/test.rnc");
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
			System.out.println("Schema not loaded");
		}
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
