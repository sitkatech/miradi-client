/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objectdata;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.objecthelpers.ORefList;
import org.miradi.questions.ChoiceQuestion;


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
	
	final public boolean isEmpty()
	{
		return toString().length() == 0;
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		startTagToXml(out);
		out.write(XmlUtilities.getXmlEncoded(get()));
		endTagToXml(out);
	}
	
	protected void startTagToXml(UnicodeWriter out) throws Exception
	{
		out.write("<" + getTag() + ">");
	}
	
	protected void endTagToXml(UnicodeWriter out) throws Exception
	{
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
	
	public boolean isChoiceItemData()
	{
		return false;
	}
	
	public boolean isCodeListData()
	{
		return false;
	}
	
	public boolean isTagListData()
	{
		return false;
	}
	
	public boolean isStringMapData()
	{
		return false;
	}
	
	public ChoiceQuestion getChoiceQuestion()
	{
		return null;
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
