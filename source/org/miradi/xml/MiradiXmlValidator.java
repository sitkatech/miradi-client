/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml;

import java.net.URL;

import org.martus.util.UnicodeReader;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.exceptions.XmlValidationException;
import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;
import org.miradi.xml.xmpz2.ValidationDriver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.prop.rng.RngProperty;
import com.thaiopensource.validate.rng.CompactSchemaReader;

//NOTE: this class was duplicated from com.thaiopensource.validate.ValidationDriver. in order to 
//have better error handling
abstract public class MiradiXmlValidator
{
	public boolean isValid(InputStreamWithSeek xmlInputStream) throws Exception
	{
		try
		{
			validate(xmlInputStream);
			return true;
		}
		catch (XmlValidationException e)
		{
			logException(e, xmlInputStream);
			return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	public void validate(InputStreamWithSeek xmlInputStream) throws Exception
	{
		PropertyMapBuilder properties = getValidatorProperties();
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(getSchemaFileRelativePathName());
		if(resourceURL == null)
			throw new Exception("Schema not found: " + getSchemaFileRelativePathName());

		InputSource schemaInputSource = new InputSource(resourceURL.openStream());
		SchemaReader schemaReader = CompactSchemaReader.getInstance();
		ValidationDriver validationDriver = new ValidationDriver(properties.toPropertyMap(), schemaReader);
		if (!validationDriver.loadSchema(schemaInputSource))
			throw new Exception("Schema cannot be loaded: " + getSchemaFileRelativePathName());

		String invalidFileMessage = EAM.text("XML file is invalid (does not conform to the schema)");
		InputSource xmlInputSource = new InputSource(xmlInputStream);
		try
		{
			Boolean isValid = validationDriver.validate(xmlInputSource);
			if (!isValid)
				throw new XmlValidationException(invalidFileMessage);
		}
		catch (SAXParseException e)
		{
			throw new XmlValidationException(invalidFileMessage, e);
		}
	}

	protected void logException(XmlValidationException e, InputStreamWithSeek xmlInputStream) throws Exception
	{
		SAXParseException saxParseExc = e.getSAXParseException();
		if (saxParseExc != null)
		{
			final int lineNumber = saxParseExc.getLineNumber();
			String lineValue = findLineValue(xmlInputStream, lineNumber);
			String error = "XML Parse error line " + lineNumber + ", column " + saxParseExc.getColumnNumber() + "\n";
			error += "Line :" + lineValue + "\n";
			error += " Public Id: " + saxParseExc.getPublicId() + "\n";
			error += " System Id: " + saxParseExc.getSystemId();
			EAM.logError(error);
		}
		EAM.logException(e);
	}

	private String findLineValue(InputStreamWithSeek xmlInputStream, final int lineNumber) throws Exception
	{
		xmlInputStream.seek(0);
		UnicodeReader reader = new UnicodeReader(xmlInputStream);
		int lineCounter = 0;
		while(true)
		{
			String line = reader.readLine();
			lineCounter++;
			if(line == null)
				break;
			
			if (lineCounter == lineNumber)
				return line;
		}
		
		return "Line Not Found";
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
	
	abstract protected String getSchemaFileRelativePathName();
}
