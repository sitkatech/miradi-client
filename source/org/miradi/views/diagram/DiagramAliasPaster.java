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

import org.miradi.commands.Command;
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
import org.miradi.objects.Factor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ViewData;
import org.miradi.utils.EnhancedJsonObject;

public class DiagramAliasPaster extends DiagramPaster
{
	public DiagramAliasPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(diagramPanelToUse, modelToUse, transferableListToUse);
	}
	
	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{
		pasteFactors(startPoint);
		createNewDiagramLinks();
		updateAutoCreatedThreatStressRatings();
		selectNewlyPastedItems();
	}
	
	public void pasteDiagramLinks() throws Exception
	{
		createNewDiagramLinks();
	}

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
		Vector<Command> commands = new Vector();
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

	public ORef getDiagramFactorWrappedRef(ORef oldWrappedRef)
	{
		return oldWrappedRef;
	}
	
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
	
	protected boolean shouldCreateObject(ORef ref)
	{
		if (shouldCreateCopy(ref))
			return true;
		
		BaseObject foundObject = getProject().findObject(ref);
		return foundObject == null;
	}

	private boolean shouldCreateCopy(ORef ref)
	{
		return GroupBox.is(ref);
	}
}
