/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
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
			project.createObject(ObjectType.MODEL_NODE, new BaseId(interventionIndexBase + i), new CreateFactorParameter(new FactorTypeStrategy()));
			project.createObject(ObjectType.MODEL_NODE, new BaseId(indirectFactorIndexBase + i), new CreateFactorParameter(new FactorTypeCause()));
			project.createObject(ObjectType.MODEL_NODE, new BaseId(directThreatIndexBase + i), new CreateFactorParameter(new FactorTypeCause()));
			project.createObject(ObjectType.MODEL_NODE, new BaseId(targetIndexBase + i), new CreateFactorParameter(new FactorTypeTarget()));
		}
		for(int i = 0; i < linkagePairs.length / 2; ++i)
		{
			FactorId fromId = new FactorId(linkagePairs[i*2]);
			FactorId toId = new FactorId(linkagePairs[i*2+1]);
			CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(fromId, toId);
			project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter);
		}
	}
}
