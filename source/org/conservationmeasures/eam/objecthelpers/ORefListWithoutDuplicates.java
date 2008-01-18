/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.objecthelpers;

public class ORefListWithoutDuplicates extends ORefList
{
	public void add(ORef newRef)
	{
		if(contains(newRef))
			return;
		super.add(newRef);
	}

	public void add(int index, ORef objectReferenceToUse)
	{
		throw new RuntimeException("Not supported!");
	}

}
