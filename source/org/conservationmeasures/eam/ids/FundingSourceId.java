/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ids;

public class FundingSourceId extends ObjectId
{
	public FundingSourceId(int idToUse)
	{
		super(idToUse);
	}
	
	public static final FundingSourceId INVALID = new FundingSourceId(IdAssigner.INVALID_ID);
}

