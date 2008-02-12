/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.diagram;

import java.text.ParseException;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.DiagramFactorPool;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.project.ProjectForTesting;

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
		BaseId linkId = project.createDiagramFactorLink(fromDiagramFactor, toDiagramFactor);
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
