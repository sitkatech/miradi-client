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
import org.miradi.main.TransferableMiradiList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramAliasPaster;

public class ConceptualModelByTargetSplitter
{
	public ConceptualModelByTargetSplitter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void splitByTarget(ConceptualModelDiagram conceptualModel) throws Exception
	{
		setDiagramObject(conceptualModel);
		HashSet<DiagramFactor> targetDiagramFactors = conceptualModel.getFactorsFromDiagram(Target.getObjectType());
		for(DiagramFactor targetDiagramFactor : targetDiagramFactors)
		{
			createDiagramForTarget(targetDiagramFactor);
		}
	}
	
	private void createDiagramForTarget(DiagramFactor targetDiagramFactor) throws Exception
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		HashSet<DiagramFactor> diagramFactors = chainObject.buildNormalChainAndGetDiagramFactors(getDiagramObject(), targetDiagramFactor);
		HashSet<DiagramLink> diagramLinks = chainObject.buildNormalChainAndGetDiagramLinks(getDiagramObject(), targetDiagramFactor);
		
		TransferableMiradiList miradiList = createTransferable(diagramFactors, diagramLinks);
		ConceptualModelDiagram conceptualModelDiagram = createConceptualModelPage();
		DiagramModel toDiagramModel = createDiagramModel(conceptualModelDiagram);

		DiagramAliasPaster paster = new DiagramAliasPaster(getProject(), null, toDiagramModel, miradiList);
		paster.pasteFactors(PASTE_START_POINT);
		reloadDiagramModelToIncludeNewlyPastedFactors(toDiagramModel, conceptualModelDiagram);
		paster.pasteDiagramLinks();
	}
	
	private void reloadDiagramModelToIncludeNewlyPastedFactors(DiagramModel toDiagramModel, ConceptualModelDiagram conceptualModelDiagram) throws Exception
	{
		toDiagramModel.fillFrom(conceptualModelDiagram);
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
	
	private DiagramObject getDiagramObject()
	{
		return diagramObject;
	}
	
	private void setDiagramObject(DiagramObject diagramObjectToUse)
	{
		diagramObject = diagramObjectToUse;
	}
	
	private Project project;
	private DiagramObject diagramObject;
	private static final Point PASTE_START_POINT = new Point(0, 0);
}
