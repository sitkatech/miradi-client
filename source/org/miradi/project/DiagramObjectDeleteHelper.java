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
package org.miradi.project;

import java.util.Vector;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Slide;
import org.miradi.objects.ViewData;
import org.miradi.views.diagram.LinkDeletor;

public class DiagramObjectDeleteHelper
{
	public DiagramObjectDeleteHelper(Project projectToUse, DiagramPanel diagramPanelToUse)
	{
		project = projectToUse;
		diagramPanel = diagramPanelToUse;
	}
	
	public void deleteDiagram() throws Exception
	{
		DiagramObject diagramObject = diagramPanel.getDiagramObject();
		deleteAllDiagramFactorLinks();
		deleteAllDiagramFactors();
		deleteAllSlideReferences(diagramObject);
		clearObject(diagramObject);
		removeAsCurrentDiagram();
		deleteDiagramObject(diagramObject);
	}

	private void deleteDiagramObject(DiagramObject diagramObject) throws CommandFailedException
	{
		CommandDeleteObject deleteDiagramObject = new CommandDeleteObject(diagramObject.getRef());
		project.executeCommand(deleteDiagramObject);
	}

	private void removeAsCurrentDiagram() throws Exception, CommandFailedException
	{
		ViewData viewData = project.getCurrentViewData();
		String currentDiagramViewDataTag = diagramPanel.getDiagramSplitPane().getDiagramPageList().getCurrentDiagramViewDataTag();
		CommandSetObjectData setCurrentDiagramCommand = new CommandSetObjectData(viewData.getRef(), currentDiagramViewDataTag, ORef.INVALID);
		project.executeCommand(setCurrentDiagramCommand);
	}

	private void clearObject(DiagramObject diagramObject) throws CommandFailedException
	{
		CommandSetObjectData[] commands = diagramObject.createCommandsToClear();
		project.executeCommandsWithoutTransaction(commands);
	}
	
	private void deleteAllSlideReferences(DiagramObject diagramObject) throws Exception
	{
		ORefList list = diagramObject.findObjectsThatReferToUs(Slide.getObjectType());
		for (int i=0; i<list.size(); ++i)
		{
			project.executeCommand(new CommandSetObjectData(list.get(i), Slide.TAG_DIAGRAM_OBJECT_REF, ORef.INVALID));
		}
	}
	
	
	private void deleteAllDiagramFactors() throws Exception
	{
		PersistentDiagramModel model = diagramPanel.getDiagramModel();
		DiagramFactor[] allDiagramFactors = model.getAllDiagramFactorsAsArray();
		
		for (int i = 0; i < allDiagramFactors.length; i++)
		{
			DiagramFactor diagramFactorToDelete = allDiagramFactors[i];
			if (doesStillExist(model, diagramFactorToDelete))
				deleteFactorAndDiagramFactor(diagramFactorToDelete);
		}
	}
	
	private boolean doesStillExist(PersistentDiagramModel model, DiagramFactor diagramFactor)
	{
		Vector<DiagramFactor> currentDiagramFactors = model.getAllDiagramFactors();
		return currentDiagramFactors.contains(diagramFactor);
	}

	private void deleteFactorAndDiagramFactor(DiagramFactor diagramFactor) throws Exception
	{		
		DiagramComponent diagram = diagramPanel.getCurrentDiagramComponent();
		FactorDeleteHelper factorDeleteHelper = FactorDeleteHelper.createFactorDeleteHelper(diagram);
		factorDeleteHelper.deleteFactorAndDiagramFactor(diagramFactor);
	}

	private void deleteAllDiagramFactorLinks() throws Exception
	{
		PersistentDiagramModel model = diagramPanel.getDiagramModel();
		DiagramLink[] allDiagramLinks = model.getAllDiagramLinksAsArray();
		LinkDeletor linkDeletor = new LinkDeletor(project);
		
		Vector<DiagramFactor> allDiagramFactors = model.getAllDiagramFactors();
		for (int i = 0; i < allDiagramLinks.length; i++)	
		{ 
			deletDiagramLink(linkDeletor, allDiagramFactors, allDiagramLinks[i]);
		}
	}

	private void deletDiagramLink(LinkDeletor linkDeletor, Vector<DiagramFactor> allDiagramFactors, DiagramLink diagramLink) throws Exception
	{
		DiagramLink found = DiagramLink.find(project, diagramLink.getRef());
		if (found == null)
			return;
		
		if (diagramLink.isGroupBoxLink())
			linkDeletor.deleteFactorLinksAndGroupBoxDiagramLinks(allDiagramFactors, diagramLink);
		else
			linkDeletor.deleteDiagramLinkAndOrphandFactorLink(allDiagramFactors, diagramLink);
	}
	
	private Project project;
	private DiagramPanel diagramPanel;
}
