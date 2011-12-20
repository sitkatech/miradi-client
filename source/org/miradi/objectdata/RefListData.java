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

import org.miradi.objecthelpers.ORefList;

public class RefListData extends ObjectData
{
	public RefListData(String tagToUse)
	{
		super(tagToUse);
		objectReferenceList = new ORefList();
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
		return objectReferenceList.toString();
	}

	@Override
	public ORefList getRefList()
	{
		return new ORefList(objectReferenceList);
	}
	
	public ORefList getORefList(int objectTypeToFilterOn)
	{
		return objectReferenceList.getFilteredBy(objectTypeToFilterOn);
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
		objectReferenceList = objectReferenceToUse;
	}
	
	private ORefList objectReferenceList;
}
