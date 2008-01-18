/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.ids;

import java.text.ParseException;

public class TemporaryIdList extends IdList
{
	public TemporaryIdList(String listAsJsonString) throws ParseException
	{
		super(-1, listAsJsonString);
	}

}
