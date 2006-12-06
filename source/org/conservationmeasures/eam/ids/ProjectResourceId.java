/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ids;

public class ProjectResourceId extends ObjectId
{

	public ProjectResourceId(int idToUse)
	{
		super(idToUse);
	}
	
	public static final ProjectResourceId INVALID = new ProjectResourceId(IdAssigner.INVALID_ID);

}
