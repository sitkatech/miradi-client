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
package org.miradi.objectdata;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaReflist;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.w3c.dom.Node;

public class RefListData extends ObjectData
{
	public RefListData(String tagToUse)
	{
		super(tagToUse);
		
		refList = new ORefList();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof RefListData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	@Override
	public String get()
	{
		return refList.toString();
	}

	@Override
	public ORefList getRefList()
	{
		return new ORefList(refList);
	}
	
	public ORefList getORefList(int objectTypeToFilterOn)
	{
		return refList.getFilteredBy(objectTypeToFilterOn);
	}
	
	@Override
	public boolean isRefListData()
	{
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public void set(String newValue) throws Exception
	{
		set(new ORefList(newValue));	
	}

	private void set(ORefList objectReferenceToUse)
	{
		refList = objectReferenceToUse;
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeRefList(baseObjectSchema, fieldSchema, refList);
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef detinationRef, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importRefs(node, detinationRef, baseObjectSchema, fieldSchema, ((FieldSchemaReflist)fieldSchema).getTypeName());
	}
	
	private ORefList refList;
}
