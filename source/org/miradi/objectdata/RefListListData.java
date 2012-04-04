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

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.CodeList;

public class RefListListData extends AbstractStringListData
{
	public RefListListData(String tagToUse)
	{
		super(tagToUse);
	}
	
	public RefListListData(String tagToUse, Vector<ORefList> refListList)
	{
		super(tagToUse);
		
		addLists(refListList);
	}

	public Vector<ORefList> convertToRefListVector() throws Exception
	{
		Vector<ORefList> refListList = new Vector<ORefList>();
		for (int index = 0; index < size(); ++index)
		{
			ORefList refList = new ORefList(get(index));
			refListList.add(refList);
		}
		
		return refListList;
	}
	
	public static CodeList convertToCodeList(Vector<ORefList> refListList)
	{
		CodeList codeList = new CodeList();
		for (int index = 0; index < refListList.size(); ++index)
		{
			codeList.add(refListList.get(index).toString());
		}
		
		return codeList;
	}
	
	public void addList(ORefList listToAdd)
	{
		add(listToAdd.toString());
	}
	
	private void addLists(Vector<ORefList> listsToAdd)
	{
		for(ORefList listToAdd : listsToAdd)
		{
			addList(listToAdd);
		}
	}
}
