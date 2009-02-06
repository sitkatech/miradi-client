/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.xml.conpro.exporter;

import java.io.InputStream;
import java.net.URL;

import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;
import org.miradi.xml.conpro.MiradiValidationDriver;
import org.xml.sax.InputSource;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.rng.CompactSchemaReader;
import com.thaiopensource.validate.rng.RngProperty;

public class ConProMiradiXmlValidator
{
	public boolean isValid(InputStream xmlInputStream) throws Exception
	{
		PropertyMapBuilder properties = getValidatorProperties();
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(MIRADI_CONPRO_SCHEMA_FILE_NAME);
		if(resourceURL == null)
			throw new Exception("Schema not found: " + MIRADI_CONPRO_SCHEMA_FILE_NAME);
		
		InputSource schemaInputSource = new InputSource(resourceURL.openStream());
		SchemaReader schemaReader = CompactSchemaReader.getInstance();
		MiradiValidationDriver validationDriver = new MiradiValidationDriver(properties.toPropertyMap(), schemaReader);
		if (validationDriver.loadSchema(schemaInputSource))
		{
			InputSource xmlInputSource = new InputSource(xmlInputStream);
			try
			{
				return validationDriver.validate(xmlInputSource);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				return false;
			}
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
			URL xmlUrl = ResourcesHandler.getResourceURL("xml/test.xml");
			new ConProMiradiXmlValidator().isValid(xmlUrl.openStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static final String MIRADI_CONPRO_SCHEMA_FILE_NAME = "xml/ConProMiradi.rnc";
}
