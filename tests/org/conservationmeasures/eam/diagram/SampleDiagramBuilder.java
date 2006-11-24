/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
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
			project.createObject(ObjectType.MODEL_NODE, new BaseId(interventionIndexBase + i), new CreateModelNodeParameter(new NodeTypeIntervention()));
			project.createObject(ObjectType.MODEL_NODE, new BaseId(indirectFactorIndexBase + i), new CreateModelNodeParameter(new NodeTypeCause()));
			project.createObject(ObjectType.MODEL_NODE, new BaseId(directThreatIndexBase + i), new CreateModelNodeParameter(new NodeTypeCause()));
			project.createObject(ObjectType.MODEL_NODE, new BaseId(targetIndexBase + i), new CreateModelNodeParameter(new NodeTypeTarget()));
		}
		for(int i = 0; i < linkagePairs.length / 2; ++i)
		{
			ModelNodeId fromId = new ModelNodeId(linkagePairs[i*2]);
			ModelNodeId toId = new ModelNodeId(linkagePairs[i*2+1]);
			CreateModelLinkageParameter parameter = new CreateModelLinkageParameter(fromId, toId);
			project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter);
		}
	}
}
