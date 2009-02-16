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
package org.miradi.utils;

import java.awt.Point;
import java.util.HashSet;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramChainObject;
import org.miradi.diagram.PersistentDiagramModel;
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
import org.miradi.views.diagram.doers.CreateMarginDoer;

public class ConceptualModelByTargetSplitter
{
	public ConceptualModelByTargetSplitter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void splitByTarget(ConceptualModelDiagram mainConceptualModelToSplit, ORef highOrAboveRankedThreatsTagToUse) throws Exception
	{
		setDiagramObjectLabel(mainConceptualModelToSplit.getRef(), "{" + EAM.text("All on One Page") + "}");
		setDiagramObjectToSplit(mainConceptualModelToSplit);
		setHighOrAboveRankedThreatsTag(highOrAboveRankedThreatsTagToUse);
		
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
		PersistentDiagramModel toDiagramModel = createDiagramModel(conceptualModelDiagram);

		DiagramAliasPaster paster = new DiagramAliasPaster(null, toDiagramModel, miradiList);
		paster.pasteFactors(PASTE_START_POINT);
		reloadDiagramModelToIncludeNewlyPastedFactors(toDiagramModel, conceptualModelDiagram);
		paster.pasteDiagramLinks();
	}
	
	private void reloadDiagramModelToIncludeNewlyPastedFactors(PersistentDiagramModel toDiagramModel, ConceptualModelDiagram conceptualModelDiagram) throws Exception
	{
		toDiagramModel.fillFrom(conceptualModelDiagram);
	}

	private ConceptualModelDiagram createConceptualModelPage(String targetNameUsedAsDiagramName) throws Exception
	{
		CommandCreateObject createPage = new CommandCreateObject(ConceptualModelDiagram.getObjectType());
		getProject().executeCommand(createPage);
		
		ORef newConceptualModelRef = createPage.getObjectRef();
		setDiagramObjectLabel(newConceptualModelRef, targetNameUsedAsDiagramName);
		applyTagIfPossible(newConceptualModelRef);
		
		return ConceptualModelDiagram.find(getProject(), newConceptualModelRef);
	}

	private void applyTagIfPossible(ORef newConceptualModelRef) throws Exception
	{
		ConceptualModelDiagram newConceptualModel = ConceptualModelDiagram.find(getProject(), newConceptualModelRef);
		TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), getHighOrAboveRankedThreatsTag());
		if (diagramContainsAnyObjectsInTaggedSet(newConceptualModel, taggedObjectSet))
		{
			CommandSetObjectData applyTagCommand = createCommandToApplyTag(newConceptualModelRef, taggedObjectSet);
			getProject().executeCommand(applyTagCommand);
		}
	}

	private CommandSetObjectData createCommandToApplyTag(ORef newConceptualModelRef, TaggedObjectSet taggedObjectSet)
	{
		ORefList singleItemListWithHighVeryHighTaggedObjectSet = new ORefList(taggedObjectSet);
		String tagRefsAsString = singleItemListWithHighVeryHighTaggedObjectSet.toString();
		return new CommandSetObjectData(newConceptualModelRef, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, tagRefsAsString);
	}

	private boolean diagramContainsAnyObjectsInTaggedSet(DiagramObject diagramObject, TaggedObjectSet taggedObjectSet)
	{
		ORefList taggedObjectRefs = taggedObjectSet.getTaggedObjectRefs();
		ORefList factorRefs = diagramObject.getAllWrappedFactorRefSet().toRefList();
		
		return factorRefs.containsAnyOf(taggedObjectRefs);
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
	
	private PersistentDiagramModel createDiagramModel(ConceptualModelDiagram conceptualModel) throws Exception
	{
		PersistentDiagramModel diagramModel = new PersistentDiagramModel(getProject());
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
	
	private void setHighOrAboveRankedThreatsTag(ORef highOrAboveRankedThreatsTagToUse)
	{
		highOrAboveRankedThreatsTag = highOrAboveRankedThreatsTagToUse;
	}
	
	private ORef getHighOrAboveRankedThreatsTag()
	{
		return highOrAboveRankedThreatsTag;
	}
	
	private Project project;
	private DiagramObject diagramObjectBeingSplit;
	private ORef highOrAboveRankedThreatsTag;
	
	private static final Point PASTE_START_POINT = new Point(CreateMarginDoer.getLeftMargin(), CreateMarginDoer.getTopMargin());
}
