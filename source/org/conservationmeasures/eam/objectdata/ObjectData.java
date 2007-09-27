/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;


abstract public class ObjectData
{
	abstract public void set(String newValue) throws Exception;
	abstract public String get();
	abstract public boolean equals(Object rawOther);
	abstract public int hashCode();
	
	final public String toString()
	{
		return get();
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.write(XmlUtilities.getXmlEncoded(get()));
	}
	
	public boolean isPseudoField()
	{
		return false;
	}
}
