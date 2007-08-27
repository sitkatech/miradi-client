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
