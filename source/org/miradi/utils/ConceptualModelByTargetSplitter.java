/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.utils;

import java.awt.Point;
import java.util.HashSet;

import org.miradi.commands.CommandCreateObject;
import org.miradi.diagram.DiagramChainObject;
import org.miradi.diagram.DiagramModel;
import org.miradi.main.MainWindow;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramAliasPaster;
import org.miradi.views.diagram.DiagramClipboard;

public class ConceptualModelByTargetSplitter
{
	public ConceptualModelByTargetSplitter(MainWindow mainWindowToUse, Project projectToUse)
	{
		mainWindow = mainWindowToUse;
		project = projectToUse;
	}
	
	public void splitByTarget(ConceptualModelDiagram conceptualModel) throws Exception
	{
		setDiagramObject(conceptualModel);
		HashSet<DiagramFactor> targetDiagramFactors = conceptualModel.getFactorsFromDiagram(Target.getObjectType());
		for(DiagramFactor targetDiagramFactor : targetDiagramFactors)
		{
			createSplitForTarget(targetDiagramFactor);
		}
	}
	
	private void createSplitForTarget(DiagramFactor targetDiagramFactor) throws Exception
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		HashSet<DiagramFactor> diagramFactors = chainObject.buildNormalChainAndGetDiagramFactors(getDiagramObject(), targetDiagramFactor);
		HashSet<DiagramLink> diagramLinks = chainObject.buildNormalChainAndGetDiagramLinks(getDiagramObject(), targetDiagramFactor);
		
		TransferableMiradiList miradiList = createTransferable(diagramFactors, diagramLinks);
		ConceptualModelDiagram conceptualModelDiagram = createConceptualModelPage();
		DiagramModel toDiagramModel = createDiagramModel(conceptualModelDiagram);
		DiagramAliasPaster paster = new DiagramAliasPaster(null, toDiagramModel, miradiList);
		//FIXME change the constructor of paster to accpet project instead of setting it this way.  paster gets project from model
		// we dont want that because paster needs to use this project.
		paster.setProject(getProject());
		//FIXME find a better location to start paste from,  or maybe dont need a location
		paster.pasteFactorsAndLinks(new Point(100, 100));
	}

	private ConceptualModelDiagram createConceptualModelPage() throws Exception
	{
		CommandCreateObject createPage = new CommandCreateObject(ConceptualModelDiagram.getObjectType());
		getProject().executeCommand(createPage);
		
		return ConceptualModelDiagram.find(getProject(), createPage.getObjectRef());
	}

	private TransferableMiradiList createTransferable(HashSet<DiagramFactor> diagramFactors, HashSet<DiagramLink> diagramLinks)
	{
		TransferableMiradiList miradiList = new TransferableMiradiList(getProject(), getDiagramObject().getRef());
		miradiList.storeData(diagramFactors, diagramLinks);
		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(miradiList, getMainWindow());
		return miradiList;
	}
	
	private DiagramModel createDiagramModel(ConceptualModelDiagram conceptualModel) throws Exception
	{
		DiagramModel diagramModel = new DiagramModel(getProject());
		diagramModel.fillFrom(conceptualModel);
		
		return diagramModel;
	}

	private Project getProject()
	{
		return project;
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private DiagramObject getDiagramObject()
	{
		return diagramObject;
	}
	
	private void setDiagramObject(DiagramObject diagramObjectToUse)
	{
		diagramObject = diagramObjectToUse;
	}
	
	private MainWindow mainWindow;
	private Project project;
	private DiagramObject diagramObject;
}
