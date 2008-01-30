/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class DiagramAliasPaster extends DiagramPaster
{
	public DiagramAliasPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		super(diagramPanelToUse, modelToUse, transferableListToUse);
	}
	
	public void pasteFactors(Point startPoint) throws Exception
	{
		dataHelper = new PointManipulater(startPoint, transferableList.getUpperMostLeftMostCorner());
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
			int type = json.getInt("Type");
			int id = json.getInt(BaseObject.TAG_ID);
			ORef ref = new ORef(type, new BaseId(id));
			if(Factor.isFactor(ref))
				pastedFactorRefs.add(ref);
		}
		
		return pastedFactorRefs;
	}

	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{
		pasteFactors(startPoint);
		createNewDiagramLinks();
		selectNewlyPastedItems();
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
	
	protected boolean canPastTypeInDiagram(int type)
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
}
