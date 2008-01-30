/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;


abstract public class ObjectData
{
	public ObjectData(String tagToUse)
	{
		tag = tagToUse;
	}
	
	abstract public void set(String newValue) throws Exception;
	abstract public String get();
	abstract public boolean equals(Object rawOther);
	abstract public int hashCode();
	
	final public String toString()
	{
		return get();
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		out.write("<" + getTag() + ">");
		out.write(XmlUtilities.getXmlEncoded(get()));
		out.writeln("</" + getTag() + ">");
	}
	
	public boolean isPseudoField()
	{
		return false;
	}
	
	public boolean isORefListData()
	{
		return false;
	}
	
	public boolean isIdListData()
	{
		return false;
	}
	
	public ORefList getRefList()
	{
		return new ORefList();
	}
	
	public String getTag()
	{
		return tag;
	}

	private String tag;
}
