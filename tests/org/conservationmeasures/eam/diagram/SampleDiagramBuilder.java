/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
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
			project.createModelNode(new NodeTypeIntervention(), new BaseId(interventionIndexBase + i));
			project.createModelNode(new NodeTypeFactor(), new BaseId(indirectFactorIndexBase + i));
			project.createModelNode(new NodeTypeFactor(), new BaseId(directThreatIndexBase + i));
			project.createModelNode(new NodeTypeTarget(), new BaseId(targetIndexBase + i));
		}
		for(int i = 0; i < linkagePairs.length / 2; ++i)
		{
			ModelNodeId fromId = new ModelNodeId(linkagePairs[i*2]);
			ModelNodeId toId = new ModelNodeId(linkagePairs[i*2+1]);
			project.createModelLinkage(BaseId.INVALID, fromId, toId);
		}
	}
}
