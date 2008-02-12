/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.ids;

public class ProjectResourceId extends ObjectId
{

	public ProjectResourceId(int idToUse)
	{
		super(idToUse);
	}
	
	public static final ProjectResourceId INVALID = new ProjectResourceId(IdAssigner.INVALID_ID);

}
