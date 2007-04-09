/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
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
			project.createObject(ObjectType.STRATEGY, new BaseId(interventionIndexBase + i));
			project.createObject(ObjectType.CAUSE, new BaseId(indirectFactorIndexBase + i));
			project.createObject(ObjectType.CAUSE, new BaseId(directThreatIndexBase + i));
			project.createObject(ObjectType.TARGET, new BaseId(targetIndexBase + i));
		}
		for(int i = 0; i < linkagePairs.length / 2; ++i)
		{
			FactorId fromId = new FactorId(linkagePairs[i*2]);
			FactorId toId = new FactorId(linkagePairs[i*2+1]);
			CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(fromId, toId);
			project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter);
		}
	}
}
