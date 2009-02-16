/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

public class DiagramModelUpdater
{

	public DiagramModelUpdater(Project projectToUse, PersistentDiagramModel modelToUse)
	{
		project = projectToUse;
		model = modelToUse;
		diagramObject = model.getDiagramObject();
	}

	public void commandSetObjectDataWasExecuted(CommandSetObjectData setObjectDataCommand)
	{
		try
		{
			if (! setObjectDataCommand.getObjectORef().equals(diagramObject.getRef()))
				return;
						
			updateFactors(setObjectDataCommand);
			updateLinks(setObjectDataCommand);
			model.updateVisibilityOfFactorsAndLinks();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void updateFactors(CommandSetObjectData setCommand) throws Exception
	{
		if (! setCommand.getFieldTag().equals(DiagramObject.TAG_DIAGRAM_FACTOR_IDS))
			return;

		String dataValueBefore = setCommand.getPreviousDataValue();
		String dataValueAfter = setCommand.getDataValue();
		IdList factorIdsBefore = new IdList(DiagramFactor.getObjectType(), dataValueBefore);
		IdList factorIdsAfter = new IdList(DiagramFactor.getObjectType(), dataValueAfter);

		IdList factorIdsToAdd = getAddedIds(factorIdsBefore, factorIdsAfter); 
		addDiagramFactors(factorIdsToAdd);
		
		IdList factorIdsToRemove = getRemovedIds(factorIdsBefore, factorIdsAfter);
		removeDiagramFactors(factorIdsToRemove);
	}
	
	private void updateLinks(CommandSetObjectData setCommand) throws Exception
	{
		if (! setCommand.getFieldTag().equals(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS))
			return;
		
		String dataValueBefore = setCommand.getPreviousDataValue();
		String dataValueAfter = setCommand.getDataValue();
		
		IdList factorLinkIdsBefore = new IdList(DiagramLink.getObjectType(), dataValueBefore);
		IdList factorLinkIdsAfter = new IdList(DiagramLink.getObjectType(), dataValueAfter);

		IdList addedLinkIds = getAddedIds(factorLinkIdsBefore, factorLinkIdsAfter);
		addDiagamLinks(addedLinkIds);
		
		IdList removedLinkIds = getRemovedIds(factorLinkIdsBefore, factorLinkIdsAfter);
		removeDiagramLinks(removedLinkIds);
	}

	private void removeDiagramLinks(IdList removedFactorLinkIds) throws Exception
	{
		for (int i = 0; i < removedFactorLinkIds.size(); i++)
		{
			DiagramLink diagramFactorLink = (DiagramLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, removedFactorLinkIds.get(i)));
			LinkCell linkCell = model.getDiagramFactorLink(diagramFactorLink);			
			clearDiagramSelection(linkCell);
			model.deleteDiagramFactorLink(diagramFactorLink);
		}
	}

	private void addDiagamLinks(IdList addedLinkIds) throws Exception
	{
		for (int i = 0; i < addedLinkIds.size(); i++)
		{
			DiagramLink diagramFactorLink = (DiagramLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, addedLinkIds.get(i)));
			model.addLinkToDiagram(diagramFactorLink);
		}
	}

	private void removeDiagramFactors(IdList factorIdsToRemove) throws Exception
	{
		for (int i = 0; i < factorIdsToRemove.size(); i++)
		{
			ORef diagramFactorRef = factorIdsToRemove.getRef(i);
			FactorCell factorCell = model.getFactorCellByRef(diagramFactorRef);
			clearDiagramSelection(factorCell);
			model.removeDiagramFactor(diagramFactorRef);
		}
	}
	
	//FIXME This is a hack and needs to have a better solution. 
	// after undoing a create link,  isAvailable was getting selected cells,
	// the delted link was included in the selected cells.  
	private void clearDiagramSelection(EAMGraphCell cell)
	{
		if (EAM.getMainWindow() == null)
			return;
		
		DiagramComponent diagram = EAM.getMainWindow().getCurrentDiagramComponent();		
		if (diagram == null)
			return;
		
		diagram.removeSelectionCell(cell);
	}

	private void addDiagramFactors(IdList addedFactorIds) throws Exception
	{
		for (int i = 0; i < addedFactorIds.size(); i++)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, addedFactorIds.get(i)));
			model.addDiagramFactor(diagramFactor);
		}
	}

	private IdList getAddedIds(IdList factorIdsBefore, IdList factorIdsAfter)
	{
		IdList addedIds = new IdList(factorIdsAfter);
		addedIds.subtract(factorIdsBefore);
		return addedIds;
	}
	
	private IdList getRemovedIds(IdList factorIdsBefore, IdList factorIdsAfter)
	{
		IdList removedIds = new IdList(factorIdsBefore);
		removedIds.subtract(factorIdsAfter);
		return removedIds;
	}
		
	private Project project;
	private PersistentDiagramModel model;
	private DiagramObject diagramObject;
}
