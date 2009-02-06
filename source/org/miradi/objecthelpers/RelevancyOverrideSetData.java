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
package org.miradi.objecthelpers;

import org.miradi.objectdata.ObjectData;

public class RelevancyOverrideSetData extends ObjectData
{
	public RelevancyOverrideSetData(String tagToUse)
	{
		super(tagToUse);
		relevancyOverrideSet = new RelevancyOverrideSet();
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof RelevancyOverrideSetData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	public RelevancyOverrideSet getRawRelevancyOverrideSet()
	{
		return relevancyOverrideSet;
	}
	
	public String get()
	{
		return relevancyOverrideSet.toString();
	}

	public int hashCode()
	{
		return toString().hashCode();
	}

	public void set(String newValue) throws Exception
	{
		set(new RelevancyOverrideSet(newValue));	
	}
	
	private void set(RelevancyOverrideSet relevancyOverrideSetToUse)
	{
		relevancyOverrideSet = relevancyOverrideSetToUse;
	}
	
	private RelevancyOverrideSet relevancyOverrideSet;
}
