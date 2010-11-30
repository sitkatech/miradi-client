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
import java.util.HashMap;
import java.util.Vector;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.diagram.DiagramModel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ViewData;
import org.miradi.utils.CommandVector;
import org.miradi.utils.EnhancedJsonObject;

public class DiagramAsSharedPaster extends DiagramPaster
{
	public DiagramAsSharedPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
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
		deleteOrphansCreatedDuringPaste();
	}
	
	private void deleteOrphansCreatedDuringPaste() throws Exception
	{
		RemoveDeletionsFromMapHandler removeDeletionsFromMapHandler = new RemoveDeletionsFromMapHandler();
		getProject().addCommandExecutedListener(removeDeletionsFromMapHandler);
		try
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
		finally
		{
			getProject().removeCommandExecutedListener(removeDeletionsFromMapHandler);
		}
	}
	
	class RemoveDeletionsFromMapHandler implements CommandExecutedListener
	{
		public RemoveDeletionsFromMapHandler()
		{
			newToOldORefMap = createInvertedMap(getOldToNewObjectRefMap());
		}
		
		public void commandExecuted(CommandExecutedEvent event)
		{
			if(event.isDeleteObjectCommand())
				removeObjectFromMap((CommandDeleteObject)event.getCommand());
		}

		private HashMap<ORef, ORef> createInvertedMap(HashMap<ORef, ORef> originalMap)
		{
			HashMap<ORef, ORef> invertedMap = new HashMap<ORef, ORef>();
			for(ORef oldRef : originalMap.keySet())
			{
				invertedMap.put(getOldToNewObjectRefMap().get(oldRef), oldRef);
			}
			return invertedMap;
		}
		
		private void removeObjectFromMap(CommandDeleteObject command)
		{
			ORef newRef = command.getObjectRef();
			ORef oldRef = newToOldORefMap.get(newRef);
			if(oldRef != null)
				getOldToNewObjectRefMap().remove(oldRef);
		}

		private HashMap<ORef, ORef> newToOldORefMap;
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
	protected boolean shouldCreateObject(ORef ref, EnhancedJsonObject json)
	{
		if(!isTypeThatCanBeShared(ref.getObjectType()))
			return true;
		
		if(!getDiagramObject().canContainFactorType(ref.getObjectType()))
			return true;
		
		BaseObject foundObject = getProject().findObject(ref);
		if(foundObject == null)
			return true;
		
		return false;
	}
	
	private boolean isTypeThatCanBeShared(int objectType)
	{
		if(Strategy.is(objectType))
			return true;
		if(Target.is(objectType))
			return true;
		if(HumanWelfareTarget.is(objectType))
			return true;
		if(Cause.is(objectType))
			return true;
		if(IntermediateResult.is(objectType))
			return true;
		if(ThreatReductionResult.is(objectType))
			return true;
		if(Stress.is(objectType))
			return true;
		if(Task.is(objectType))
			return true;
		
		return false;
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
}
