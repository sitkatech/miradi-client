/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.DiagramFactorPool;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class SampleDiagramBuilder
{
	public static void buildNodeGrid(ProjectForTesting project, int itemsPerType, int[] linkagePairs) throws Exception
	{
		final int interventionIndexBase = 11;
		final int indirectFactorIndexBase = 21;
		final int directThreatIndexBase = 31;
		final int targetIndexBase = 41;
		
		for(int i = 0; i < itemsPerType; ++i)
		{
			createObjectAndSetLabel(project, ObjectType.STRATEGY, interventionIndexBase + i);
			createObjectAndSetLabel(project, ObjectType.CAUSE, indirectFactorIndexBase + i);
			createObjectAndSetLabel(project, ObjectType.CAUSE, directThreatIndexBase + i);
			createObjectAndSetLabel(project, ObjectType.TARGET, targetIndexBase + i);
		}
		
		for(int i = 0; i < linkagePairs.length / 2; ++i)
		{
			String fromLabel = Integer.toString(linkagePairs[ i * 2]);
			DiagramFactor fromDiagramFactor = findDiagramFactorByLabel(project, fromLabel);
			
			String toLabel = Integer.toString(linkagePairs[ i * 2 + 1]);
			DiagramFactor toDiagramFactor = findDiagramFactorByLabel(project, toLabel);
			
			addDiagramFactorLink(project, fromDiagramFactor, toDiagramFactor);
		}
	}

	private static void addDiagramFactorLink(ProjectForTesting project, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception, ParseException, CommandFailedException
	{
		DiagramFactorLinkId linkId = project.createDiagramFactorLink(fromDiagramFactor, toDiagramFactor);
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData addDiagramFactorLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, linkId);
		project.executeCommand(addDiagramFactorLink);
	}
	
	private static DiagramFactor findDiagramFactorByLabel(ProjectForTesting project, String label)
	{
		DiagramFactorPool diagramFactorPool = project.getDiagramFactorPool();
		IdList diagramFactorIds = diagramFactorPool.getIdList();
		for (int i = 0; i < diagramFactorIds.size(); ++i)
		{
			DiagramFactor diagramFactor = diagramFactorPool.find(diagramFactorIds.get(i));
			FactorId factorId = diagramFactor.getWrappedId();
			Factor factor = project.findNode(factorId);
			if (factor.getLabel().equals(label))
				return diagramFactor;
		}
		
		return null;
	}

	private static void createObjectAndSetLabel(ProjectForTesting project, int type, final int interventionIndex) throws Exception, CommandFailedException
	{
		DiagramFactor diagramFactor = project.createDiagramFactorAndAddToDiagram(type);
		Factor factor = (Factor) project.findObject(new ORef(type, diagramFactor.getWrappedId()));
		CommandSetObjectData addLabelCommand = new CommandSetObjectData(factor.getRef(), Factor.TAG_LABEL, Integer.toString(interventionIndex));
		project.executeCommand(addLabelCommand);
	}
}
