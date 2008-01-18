/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ThreatReductionResultPool;

public class TestThreatReductionResultPool extends TestFactorPool
{
	public TestThreatReductionResultPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getThreatReductionResultPool();
	}
	
	public int getObjectType()
	{
		return ObjectType.THREAT_REDUCTION_RESULT;
	}

	ThreatReductionResultPool pool;
}
