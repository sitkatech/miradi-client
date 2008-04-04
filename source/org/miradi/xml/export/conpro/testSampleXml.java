/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.export.conpro;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.miradi.main.EAM;

public class testSampleXml
{
	public void validate() throws Exception
	{
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);//, "http://java.sun.com/j2se/1.5.0/docs/api/javax/xml/XMLConstants.html#RELAXNG_NS_URI");
		Schema schema = schemaFactory.newSchema(EAM.getResourceURL("xml/test.rng"));
		
		URL xmlUrl = EAM.getResourceURL("xml/test.xml");
		StreamSource streamSource = new StreamSource(xmlUrl.openStream());
		schema.newValidator().validate(streamSource);
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
