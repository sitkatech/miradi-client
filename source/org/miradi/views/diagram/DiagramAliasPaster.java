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

import java.awt.Point;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.miradi.diagram.DiagramModel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ViewData;
import org.miradi.utils.CommandVector;
import org.miradi.utils.EnhancedJsonObject;

public class DiagramAliasPaster extends DiagramPaster
{
	public DiagramAliasPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(diagramPanelToUse, modelToUse, transferableListToUse);
	}
	
	@Override
	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{
		pasteFactors(startPoint);
		createNewFactorLinks();
		createNewDiagramLinks();
		updateAutoCreatedThreatStressRatings();
		selectNewlyPastedItems();

		deleteOrphansCreatedDuringPaste();
	}
	
	private void deleteOrphansCreatedDuringPaste() throws Exception
	{
		Vector<String> factorDeepCopies = getFactorDeepCopies();
		for(String jsonString : factorDeepCopies)
		{
			EnhancedJsonObject json = new EnhancedJsonObject(jsonString);
			int type = getTypeFromJson(json);
			int id = json.getInt(BaseObject.TAG_ID);
			ORef oldRef = new ORef(type, new BaseId(id));
			ORef newRef = getOldToNewObjectRefMap().get(oldRef);
			if (newRef == null)
				continue;
			
			BaseObject baseObject = BaseObject.find(getProject(), newRef);
			ORefList allReferrers = baseObject.findObjectsThatReferToUs();
			if (allReferrers.isEmpty())
			{
				CommandVector commandsToDeleteOrphan = baseObject.createCommandsToDeleteChildrenAndObject();
				getProject().executeCommandsWithoutTransaction(commandsToDeleteOrphan);
			} 
		}
	}

	public void pasteDiagramLinks() throws Exception
	{
		createNewDiagramLinks();
	}

	@Override
	public void pasteFactors(Point startPoint) throws Exception
	{
		dataHelper = new PointManipulater(startPoint, transferableList.getUpperMostLeftMostCorner());
		createNewFactorsAndContents();
		createNewDiagramFactors();
		
		ORefList pastedFactorRefs = getPastedFactorRefs();
		
		String mode = getProject().getDiagramViewData().getData(ViewData.TAG_CURRENT_MODE);
		if(mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
			ensureVisible(pastedFactorRefs);
	}
	
	private void ensureVisible(ORefList refs) throws Exception
	{
		CommandVector commands = new CommandVector();
		ViewData viewData = getProject().getDiagramViewData();
		for(int i = 0; i < refs.size(); ++i)
		{
			ORef ref = refs.get(i);
			commands.addAll(Arrays.asList(viewData.buildCommandsToAddNode(ref)));
		}
		
		getProject().executeCommandsWithoutTransaction(commands);
	}

	private ORefList getPastedFactorRefs() throws ParseException, Exception
	{
		ORefList pastedFactorRefs = new ORefList();
		Vector<String> factorDeepCopies = getFactorDeepCopies();
		for(String jsonString : factorDeepCopies)
		{
			EnhancedJsonObject json = new EnhancedJsonObject(jsonString);
			int type = getTypeFromJson(json);
			int id = json.getInt(BaseObject.TAG_ID);
			ORef ref = new ORef(type, new BaseId(id));
			if(Factor.isFactor(ref))
				pastedFactorRefs.add(ref);
		}
		
		return pastedFactorRefs;
	}

	@Override
	public ORef getCorrospondingNewRef(ORef oldWrappedRef)
	{
		if(BaseObject.find(getProject(), oldWrappedRef) != null)
			return oldWrappedRef;
		
		ORef newRef = oldToNewPastedObjectMap.get(oldWrappedRef);
		return newRef;
	}
	
	@Override
	public ORef getFactorLinkRef(ORef oldWrappedFactorLinkRef)
	{
		return oldWrappedFactorLinkRef;
	}
	
	protected int[] getResultsChainPastableTypes()
	{
		return new int[] {
				ObjectType.THREAT_REDUCTION_RESULT,
				ObjectType.INTERMEDIATE_RESULT, 
				};
	}
	
	@Override
	protected boolean canPasteTypeInDiagram(int type)
	{
		if (isPastingInSameDiagramType())
			return true;
		
		boolean isResultsChain = ResultsChainDiagram.is(getDiagramObject().getType());
		if (isResultsChain && Cause.is(type))
			return false;
		
		boolean isConceptualModel = ConceptualModelDiagram.is(getDiagramObject().getType());
		if (isConceptualModel && containsType(getResultsChainPastableTypes(), type))
			return false;
		
		return true;
	}
	
	@Override
	protected boolean shouldCreateObject(ORef ref, EnhancedJsonObject json)
	{
		if (shouldCreateCopy(ref, json))
			return true;
		
		BaseObject foundObject = getProject().findObject(ref);
		return foundObject == null;
	}
	
	@Override
	protected FactorLink createFactorLink(EnhancedJsonObject json) throws Exception
	{
		ORef newFromRef = getFixedupRef(getOldToNewObjectRefMap(), json, FactorLink.TAG_FROM_REF);
		ORef newToRef = getFixedupRef(getOldToNewObjectRefMap(), json, FactorLink.TAG_TO_REF);	
		
		FactorLink existingLink = findFactorLink(newFromRef, newToRef);
		if(existingLink != null)
			return existingLink;
		
		return super.createFactorLink(json);
	}
	
	private boolean shouldCreateCopy(ORef ref, EnhancedJsonObject json)
	{
		if(GroupBox.is(ref))
			return true;
		
		if(DiagramFactor.is(ref))
		{
			ORef wrappedRef = json.optRef(DiagramFactor.TAG_WRAPPED_REF);
			if(GroupBox.is(wrappedRef))
				return true;
		}
		
		return false;
	}
}
