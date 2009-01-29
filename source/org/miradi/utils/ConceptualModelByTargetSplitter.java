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
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramChainObject;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramAliasPaster;

public class ConceptualModelByTargetSplitter
{
	public ConceptualModelByTargetSplitter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void splitByTarget(ConceptualModelDiagram mainConceptualModelToSplit) throws Exception
	{
		setDiagramObjectLabel(mainConceptualModelToSplit.getRef(), "{" + EAM.text("All on One Page") + "}");
		setDiagramObjectToSplit(mainConceptualModelToSplit);
		HashSet<DiagramFactor> targetDiagramFactors = mainConceptualModelToSplit.getFactorsFromDiagram(Target.getObjectType());
		for(DiagramFactor targetDiagramFactor : targetDiagramFactors)
		{
			createDiagramForTarget(targetDiagramFactor);
		}
		
		hideLinkLayer(mainConceptualModelToSplit.getRef());
	}
	
	private void createDiagramForTarget(DiagramFactor targetDiagramFactor) throws Exception
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		HashSet<DiagramFactor> diagramFactors = chainObject.buildNormalChainAndGetDiagramFactors(getDiagramObjectBeingSplit(), targetDiagramFactor);
		HashSet<DiagramLink> diagramLinks = chainObject.buildNormalChainAndGetDiagramLinks(getDiagramObjectBeingSplit(), targetDiagramFactor);
		
		TransferableMiradiList miradiList = createTransferable(diagramFactors, diagramLinks);
		ConceptualModelDiagram conceptualModelDiagram = createConceptualModelPage(targetDiagramFactor.getWrappedFactor().toString());
		DiagramModel toDiagramModel = createDiagramModel(conceptualModelDiagram);

		DiagramAliasPaster paster = new DiagramAliasPaster(null, toDiagramModel, miradiList);
		paster.pasteFactors(PASTE_START_POINT);
		reloadDiagramModelToIncludeNewlyPastedFactors(toDiagramModel, conceptualModelDiagram);
		paster.pasteDiagramLinks();
	}
	
	private void reloadDiagramModelToIncludeNewlyPastedFactors(DiagramModel toDiagramModel, ConceptualModelDiagram conceptualModelDiagram) throws Exception
	{
		toDiagramModel.fillFrom(conceptualModelDiagram);
	}

	private ConceptualModelDiagram createConceptualModelPage(String targetNameUsedAsDiagramName) throws Exception
	{
		CommandCreateObject createPage = new CommandCreateObject(ConceptualModelDiagram.getObjectType());
		getProject().executeCommand(createPage);
		
		ORef newConceptualModelRef = createPage.getObjectRef();
		setDiagramObjectLabel(newConceptualModelRef, targetNameUsedAsDiagramName);
		updateDiagramTags(newConceptualModelRef);
		
		return ConceptualModelDiagram.find(getProject(), newConceptualModelRef);
	}

	private void updateDiagramTags(ORef newConceptualModelRef) throws Exception
	{
		ORefList onlyTagsWithFactorsInDiagram = getTaggedObjectSetsWithFactorsThatAppearInDiagram(newConceptualModelRef);
		String tagRefsAsString = onlyTagsWithFactorsInDiagram.toString();
		CommandSetObjectData setTags = new CommandSetObjectData(newConceptualModelRef, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, tagRefsAsString);
		getProject().executeCommand(setTags);
	}

	private ORefList getTaggedObjectSetsWithFactorsThatAppearInDiagram(ORef newConceptualModelRef)
	{
		ORefList selectedTaggedObjectSetRefs = getDiagramObjectBeingSplit().getSelectedTaggedObjectSetRefs();
		ORefList onlyTagsWithFactors = new ORefList();
		ConceptualModelDiagram newConceptualModel = ConceptualModelDiagram.find(getProject(), newConceptualModelRef);
		for (int index = 0; index < selectedTaggedObjectSetRefs.size(); ++index)
		{
			ORef taggedObjectSetRef = selectedTaggedObjectSetRefs.get(index);
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);
			ORefList taggedObjectRefs = taggedObjectSet.getTaggedObjectRefs();
			ORefList wrappedFactorRefs = new ORefList(newConceptualModel.getAllWrappedFactors());
			if (taggedObjectRefs.containsAnyOf(wrappedFactorRefs))
				onlyTagsWithFactors.add(taggedObjectSetRef);
		}
		return onlyTagsWithFactors;
	}

	private void setDiagramObjectLabel(ORef newConceptualModelRef, String targetNameUsedAsDiagramName) throws CommandFailedException
	{
		CommandSetObjectData setName = new CommandSetObjectData(newConceptualModelRef, DiagramObject.TAG_LABEL, targetNameUsedAsDiagramName);
		getProject().executeCommand(setName);
	}

	private void hideLinkLayer(ORef conceptualModelRef) throws Exception
	{
		CodeList codeListWithHiddenLinkLayer = new CodeList();
		codeListWithHiddenLinkLayer.add(FactorLink.OBJECT_NAME);
		
		CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(conceptualModelRef, DiagramObject.TAG_HIDDEN_TYPES, codeListWithHiddenLinkLayer.toString());
		getProject().executeCommand(setLegendSettingsCommand);
	}
	
	private TransferableMiradiList createTransferable(HashSet<DiagramFactor> diagramFactors, HashSet<DiagramLink> diagramLinks)
	{
		TransferableMiradiList miradiList = new TransferableMiradiList(getProject(), getDiagramObjectBeingSplit().getRef());
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
	
	private DiagramObject getDiagramObjectBeingSplit()
	{
		return diagramObjectBeingSplit;
	}
	
	private void setDiagramObjectToSplit(DiagramObject diagramObjectToUse)
	{
		diagramObjectBeingSplit = diagramObjectToUse;
	}
	
	private Project project;
	private DiagramObject diagramObjectBeingSplit;
	private static final Point PASTE_START_POINT = new Point(0, 0);
}
