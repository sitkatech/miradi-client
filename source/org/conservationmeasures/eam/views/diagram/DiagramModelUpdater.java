/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;

public class DiagramModelUpdater
{

	public DiagramModelUpdater(Project projectToUse, DiagramModel modelToUse, DiagramObject diagramObjectToUse)
	{
		project = projectToUse;
		model = modelToUse;
		diagramObject = diagramObjectToUse;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (! event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		try
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			
			if (! setCommand.getObjectORef().equals(diagramObject.getRef()))
				return;
			
			String dataValueBefore = setCommand.getPreviousDataValue();
			String dataValueAfter = setCommand.getDataValue();
			String dataTag = setCommand.getFieldTag();
			
			updateDiagramFactorInDiagramObject(dataValueBefore, dataValueAfter, dataTag);
			updateDiagramFactorLinkInDiagramObject(dataValueBefore, dataValueAfter, dataTag);
			model.updateVisibilityOfFactors();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void updateDiagramFactorLinkInDiagramObject(String dataValueBefore, String dataValueAfter, String dataTag) throws Exception
	{
		if (! dataTag.equals(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS))
			return;
		
		IdList factorLinkIdsBefore = new IdList(dataValueBefore);
		IdList factorLinkIdsAfter = new IdList(dataValueAfter);

		possiblyAddDiagramFactorLink(factorLinkIdsBefore, factorLinkIdsAfter);
		possiblyRemoveDiagramFactorLink(factorLinkIdsBefore, factorLinkIdsAfter);
	}

	private void possiblyRemoveDiagramFactorLink(IdList factorLinkIdsBefore, IdList factorLinkIdsAfter) throws Exception
	{
		IdList removedFactorLinkIds = new IdList(factorLinkIdsBefore);
		removedFactorLinkIds.subtract(factorLinkIdsAfter);
				
		for (int i = 0; i < removedFactorLinkIds.size(); i++)
		{
			DiagramFactorLink diagramFactorLink = (DiagramFactorLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, removedFactorLinkIds.get(i)));
			model.deleteDiagramFactorLink(diagramFactorLink);
		}
	}

	private void possiblyAddDiagramFactorLink(IdList factorLinkIdsBefore, IdList factorLinkIdsAfter) throws Exception
	{
		IdList addedFactorLinkIds = new IdList(factorLinkIdsAfter);
		addedFactorLinkIds.subtract(factorLinkIdsBefore);
				
		for (int i = 0; i < addedFactorLinkIds.size(); i++)
		{
			DiagramFactorLink diagramFactorLink = (DiagramFactorLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, addedFactorLinkIds.get(i)));
			model.addLinkToDiagram(diagramFactorLink);
		}
	}

	private void updateDiagramFactorInDiagramObject(String dataValueBefore, String dataValueAfter, String dataTag) throws Exception
	{
		if (! dataTag.equals(DiagramObject.TAG_DIAGRAM_FACTOR_IDS))
			return;
		
		IdList factorIdsBefore = new IdList(dataValueBefore);
		IdList factorIdsAfter = new IdList(dataValueAfter);

		possiblyAddDiagramFactorToModel(factorIdsBefore, factorIdsAfter);
		possiblyRemoveDiagramFactorFromModel(factorIdsBefore, factorIdsAfter);
	}

	private void possiblyRemoveDiagramFactorFromModel(IdList factorIdsBefore, IdList factorIdsAfter) throws Exception
	{
		IdList factorIdsToRemove = new IdList(factorIdsBefore);
		factorIdsToRemove.subtract(factorIdsAfter);

		for (int i = 0; i < factorIdsToRemove.size(); i++)
		{
			model.removeDiagramFactor(new DiagramFactorId(factorIdsToRemove.get(i).asInt()));
		}
	}

	private void possiblyAddDiagramFactorToModel(IdList factorIdsBefore, IdList factorIdsAfter) throws Exception
	{
		IdList addedFactorIds = new IdList(factorIdsAfter);
		addedFactorIds.subtract(factorIdsBefore);

		for (int i = 0; i < addedFactorIds.size(); i++)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, addedFactorIds.get(i)));
			model.addDiagramFactor(diagramFactor);
		}
	}
	
	private Project project;
	private DiagramModel model;
	private DiagramObject diagramObject;
}
