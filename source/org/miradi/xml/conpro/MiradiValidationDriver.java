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
package org.miradi.xml.conpro;

import java.io.IOException;

import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.thaiopensource.util.PropertyId;
import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;
import com.thaiopensource.validate.auto.AutoSchemaReader;
import com.thaiopensource.xml.sax.CountingErrorHandler;
import com.thaiopensource.xml.sax.ErrorHandlerImpl;
import com.thaiopensource.xml.sax.Jaxp11XMLReaderCreator;
import com.thaiopensource.xml.sax.XMLReaderCreator;

//NOTE: tis class was duplicated from com.thaiopensource.validate.ValidationDriver. in order to 
//have better error handling
public class MiradiValidationDriver
{
	public MiradiValidationDriver(PropertyMap properties, SchemaReader sr) throws InstantiationException, IllegalAccessException 
	{
		this(properties, properties, sr);
	}

	public MiradiValidationDriver(PropertyMap schemaProperties, PropertyMap instanceProperties, SchemaReader schemaReader) throws InstantiationException, IllegalAccessException 
	{
		PropertyMapBuilder builder = new PropertyMapBuilder(schemaProperties);
		for (int i = 0; i < requiredProperties.length; i++) 
		{
			if (!builder.contains(requiredProperties[i])) 
			{
				try 
				{
					builder.put(requiredProperties[i], defaultClasses[i].newInstance());
				}
				catch (InstantiationException e) 
				{
					throw e;
				}
				catch (IllegalAccessException e) 
				{
					throw e;
				}
			}
		}
		this.schemaProperties = builder.toPropertyMap();
		builder = new PropertyMapBuilder(instanceProperties);
		for (int i = 0; i < requiredProperties.length; i++) 
		{
			if (!builder.contains(requiredProperties[i]))
				builder.put(requiredProperties[i],
						this.schemaProperties.get(requiredProperties[i]));
		}
		eh = new CountingErrorHandler((ErrorHandler)builder.get(ValidateProperty.ERROR_HANDLER));
		eh.setErrorHandler(new CustomErrorHandler());
		ValidateProperty.ERROR_HANDLER.put(builder, eh);
		this.instanceProperties = builder.toPropertyMap();
		this.xrc = ValidateProperty.XML_READER_CREATOR.get(this.instanceProperties);
		this.sr = schemaReader == null ? new AutoSchemaReader() : schemaReader;
	}

	public boolean loadSchema(InputSource in) throws Exception 
	{
		try 
		{
			schema = sr.createSchema(in, schemaProperties);
			validator = null;
			return true;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public boolean validate(InputSource in) throws SAXException, IOException 
	{
		if (schema == null)
			throw new IllegalStateException("cannot validate without schema");
		if (validator == null)
			validator = schema.createValidator(instanceProperties);
		if (xr == null) 
		{
			xr = xrc.createXMLReader();
			xr.setErrorHandler(eh);
		}
		
		eh.reset();
		xr.setContentHandler(validator.getContentHandler());
		DTDHandler dh = validator.getDTDHandler();
		if (dh != null)
			xr.setDTDHandler(dh);
		try 
		{
			xr.parse(in);
			return !eh.getHadErrorOrFatalError();
		}
		finally 
		{
			validator.reset();
		}
	}

	public class CustomErrorHandler implements ErrorHandler
	{
		public void error(SAXParseException e) throws SAXException
		{
			throw e;
		}

		public void fatalError(SAXParseException e) throws SAXException
		{
			throw e;
		}

		public void warning(SAXParseException e) throws SAXException
		{
			throw e;
		}
	}
	
	private static final PropertyId[] requiredProperties = {ValidateProperty.XML_READER_CREATOR, ValidateProperty.ERROR_HANDLER};

	private static final Class[] defaultClasses = {Jaxp11XMLReaderCreator.class, ErrorHandlerImpl.class	};
	
	private final XMLReaderCreator xrc;
	private XMLReader xr;
	private final CountingErrorHandler eh;
	private final SchemaReader sr;
	private final PropertyMap schemaProperties;
	private final PropertyMap instanceProperties;
	private Validator validator;
	private Schema schema;
}
