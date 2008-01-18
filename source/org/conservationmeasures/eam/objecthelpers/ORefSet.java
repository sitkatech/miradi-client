/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Arrays;
import java.util.HashSet;

public class ORefSet extends HashSet<ORef>
{
	public ORefSet()
	{
		super();
	}
	
	public ORefSet(ORefList refList)
	{
		this();
		addAll(Arrays.asList(refList.toArray()));
	}
}
