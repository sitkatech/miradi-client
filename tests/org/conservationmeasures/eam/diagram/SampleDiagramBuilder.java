/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIndirectFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;

public class SampleDiagramBuilder
{
	public static void buildNodeGrid(Project project, int itemsPerType, int[] linkagePairs) throws Exception
	{
		final int interventionIndexBase = 11;
		final int indirectFactorIndexBase = 21;
		final int directThreatIndexBase = 31;
		final int targetIndexBase = 41;
		for(int i = 0; i < itemsPerType; ++i)
		{
			project.insertNodeAtId(new NodeTypeIntervention(), new BaseId(interventionIndexBase + i));
			project.insertNodeAtId(new NodeTypeIndirectFactor(), new BaseId(indirectFactorIndexBase + i));
			project.insertNodeAtId(new NodeTypeDirectThreat(), new BaseId(directThreatIndexBase + i));
			project.insertNodeAtId(new NodeTypeTarget(), new BaseId(targetIndexBase + i));
		}
		for(int i = 0; i < linkagePairs.length / 2; ++i)
		{
			BaseId fromId = new BaseId(linkagePairs[i*2]);
			BaseId toId = new BaseId(linkagePairs[i*2+1]);
			project.insertLinkageAtId(BaseId.INVALID, fromId, toId);
		}
	}
}
