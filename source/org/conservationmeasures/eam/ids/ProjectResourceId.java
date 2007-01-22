/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
