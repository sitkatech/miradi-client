/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;


public class TestPlanningTreeObjectiveNode extends TestPlanningTree
{
	public TestPlanningTreeObjectiveNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeObjectiveNode() throws Exception
	{
		String ralatedStrategies = getObjective().getPseudoData(Objective.PSEUDO_TAG_RELATED_STRATEGY_OREF_LIST);
		ORefList strategytORefs = new ORefList(ralatedStrategies);
		assertEquals("wrong stratgy count?", 1, strategytORefs.size());
		assertEquals("wrong type?", Strategy.getObjectType(), strategytORefs.get(0).getObjectType());
		
		String relatedIndicators = getObjective().getPseudoData(Objective.PSEUDO_TAG_RELATED_INDICATOR_OREF_LIST);
		ORefList indicatortORefs = new ORefList(relatedIndicators);
		assertEquals("wrong indicator count?", 1, indicatortORefs.size());
		assertEquals("wrong type?", Indicator.getObjectType(), indicatortORefs.get(0).getObjectType());
	}
}
