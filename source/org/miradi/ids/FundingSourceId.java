/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.ids;

public class FundingSourceId extends ObjectId
{
	public FundingSourceId(int idToUse)
	{
		super(idToUse);
	}
	
	public static final FundingSourceId INVALID = new FundingSourceId(IdAssigner.INVALID_ID);
}

