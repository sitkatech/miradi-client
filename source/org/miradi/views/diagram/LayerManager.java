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

import java.util.HashSet;
import java.util.Set;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Objective;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ViewData;
import org.miradi.utils.CodeList;

public class LayerManager
{
	public LayerManager(DiagramObject diagramObjectToUse)
	{
		diagramObject = diagramObjectToUse;
	
		hiddenNodeTypes = new HashSet<Class>();
		hiddenORefs = new ORefList();
		mode = ViewData.MODE_DEFAULT;
		contributingFactorsVisibleFlag = true;
		directThreatsVisibleFlag = true;
		linkagesVisibleFlag = true;
		goalsVisibleFlag = true;
		objectivesVisibleFlag = true;
		indicatorsVisibleFlag = true;
		scopeBoxVisibleFlag = true;
		stressesVisibleFlag = true;
		activitiesVisibleFlag = true;
		intermediateResultFlag = true;
		threatReductionResultFlag = true;
		textBoxesVisibleFlag = true;
		groupBoxesVisibleFlag = true;
		draftStrategyVisibleFlag = true;
	}
	
	public boolean isVisible(DiagramObject diagramObjectToUse, FactorCell node)
	{
		if (isHiddenInDiagramObject(diagramObjectToUse, node.getWrappedFactor().getTypeName()))
			return false;
		
		if(hiddenORefs.contains(node.getWrappedFactorRef()))
			return false;
		
		boolean isDraft = node.getWrappedFactor().isStatusDraft();
		if (isDraft)
		{
			if (isResultsChain(diagramObjectToUse))
				return false;

			if(mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
				return areDraftsVisible(node);
			
			return false;
		}
		
		if(node.isContributingFactor())
			return areContributingFactorsVisible();
		
		if(node.isDirectThreat())
			return areDirectThreatsVisible();
		
		if (node.isIntermediateResult())
			return areIntermediateResultsVisible();
		
		if (node.isThreatRedectionResult())
			return areThreatReductionResultsVisible();

		if (node.isStress())
			return areStressesVisible();
		
		if (node.isActivity())
			return areActivitiesVisible();

		if(isTypeVisible(node.getWrappedFactor().getTypeName()))
			return true;
		
		return false;
	}

	private boolean isHiddenInDiagramObject(DiagramObject diagramObjectToUse, String objectTypeName)
	{
		if (isSafeDiagramObject(diagramObjectToUse))
			return diagramObjectToUse.getHiddenTypes().contains(objectTypeName);
		
		return false;
	}

	private boolean isSafeDiagramObject(DiagramObject diagramObjectToUse)
	{
		return diagramObjectToUse != null;
	}

	private boolean isResultsChain(DiagramObject diagramObjectToUse)
	{
		if (diagramObjectToUse == null)
			return false;
		
		return diagramObjectToUse.isResultsChain();
	}

	public void setVisibility(Class nodeClass, boolean newVisibility)
	{
		if(newVisibility)
			hiddenNodeTypes.remove(nodeClass);
		else
			hiddenNodeTypes.add(nodeClass);
	}
	
	public boolean areAllNodesVisible()
	{
		return getDiagramObject().getHiddenTypes().isEmpty() && hiddenORefs.isEmpty();
	}
	
	public void setHiddenORefs(ORefList oRefsToHide)
	{
		hiddenORefs = new ORefList(oRefsToHide);
	}
	
	public void setMode(String newMode)
	{
		mode = newMode;
	}
	
	public boolean areContributingFactorsVisible()
	{
		return isTypeVisible(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
	}
	
	public void setContributingFactorsVisible(boolean newSetting)
	{
		contributingFactorsVisibleFlag = newSetting;
	}
	
	public boolean areDirectThreatsVisible()
	{
		return isTypeVisible(Cause.OBJECT_NAME_THREAT);
	}
	
	public void setDirectThreatsVisible(boolean newSetting)
	{
		directThreatsVisibleFlag = newSetting;
	}
	
	public boolean areFactorLinksVisible()
	{
		return isTypeVisible(FactorLink.OBJECT_NAME);
	}
	
	public void setFactorLinksVisible(boolean newSetting)
	{
		linkagesVisibleFlag = newSetting;
	}

	public boolean areGoalsVisible()
	{
		return isTypeVisible(Goal.OBJECT_NAME);
	}
	
	public boolean areObjectivesVisible()
	{
		return isTypeVisible(Objective.OBJECT_NAME);
	}
	
	public void setGoalsVisible(boolean newSetting)
	{
		setVisibility(Goal.OBJECT_NAME, newSetting);
	}
	
	public void setObjectivesVisible(boolean newSetting)
	{
		setVisibility(Objective.OBJECT_NAME, newSetting);
	}

	public void setVisibility(String typeName, boolean isVisible)
	{
		CodeList hiddenTypes = getDiagramObject().getHiddenTypes();		
		if (isVisible)
			hiddenTypes.removeCode(typeName);
		else
			hiddenTypes.add(typeName);
		
		saveVisibility(hiddenTypes);
	}
	
	private void saveVisibility(CodeList currentHiddenTypes)
	{
		try
		{
			CommandSetObjectData updateHiddenTypes = new CommandSetObjectData(getDiagramObject(), DiagramObject.TAG_HIDDEN_TYPES, currentHiddenTypes.toString());
			getDiagramObject().getProject().executeCommand(updateHiddenTypes);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public boolean areTextBoxesVisible()
	{
		return isTypeVisible(TextBox.OBJECT_NAME);
	}
	
	public boolean areGroupBoxesVisible()
	{
		return isTypeVisible(GroupBox.OBJECT_NAME);
	}
	
	private boolean areDraftsVisible(FactorCell node)
	{
		if (!node.isStrategy())
			throw new RuntimeException("Unexpected non strategy draft");
		
		return areDraftStrategiesVisible();
	}

	public boolean areDraftStrategiesVisible()
	{
		return isTypeVisible(Strategy.OBJECT_NAME_DRAFT);
	}
	
	public void setDraftStrategiesVisible(boolean newSetting)
	{
		draftStrategyVisibleFlag = newSetting;
	}

	public boolean areIndicatorsVisible()
	{
		return isTypeVisible(Indicator.OBJECT_NAME);
	}
	
	public void setIndicatorsVisible(boolean newSetting)
	{
		setVisibility(Indicator.OBJECT_NAME, newSetting);
	}

	public boolean isScopeBoxVisible()
	{
		return isTypeVisible(ScopeBox.OBJECT_NAME);
	}
	
	public void setScopeBoxVisible(boolean newSetting)
	{
		scopeBoxVisibleFlag = newSetting;
	}
	
	public boolean areStressesVisible()
	{
		return isTypeVisible(Stress.OBJECT_NAME);
	}
	
	public void setStressesVisible(boolean newSetting)
	{
		stressesVisibleFlag = newSetting;
	}
	
	public boolean areActivitiesVisible()
	{
		return isTypeVisible(Task.ACTIVITY_NAME);
	}
	
	public void setActivitiesVisible(boolean newSetting)
	{
		activitiesVisibleFlag = newSetting;
	}
	
	public boolean areIntermediateResultsVisible()
	{
		return isTypeVisible(IntermediateResult.OBJECT_NAME);
	}
	
	public void setIntermediateResultVisible(boolean newSetting)
	{
		intermediateResultFlag = newSetting;
	}
	
	public boolean areThreatReductionResultsVisible()
	{
		return isTypeVisible(ThreatReductionResult.OBJECT_NAME);
	}
	
	public void setThreatReductionResultVisible(boolean newSetting)
	{
		threatReductionResultFlag = newSetting;
	}
	
	public boolean isTypeVisible(String objectTypeName)
	{
		return !isHiddenInDiagramObject(getDiagramObject(), objectTypeName);
	}
		
	private DiagramObject getDiagramObject()
	{
		return diagramObject;
	}
	
	public void setDiagramObject(DiagramObject diagramContentsToUse)
	{
		diagramObject = diagramContentsToUse;
	}
	
	private DiagramObject diagramObject;
	private Set<Class> hiddenNodeTypes;
	private ORefList hiddenORefs;
	private String mode;
	boolean contributingFactorsVisibleFlag;
	boolean directThreatsVisibleFlag;
	boolean linkagesVisibleFlag;
	boolean goalsVisibleFlag;
	boolean objectivesVisibleFlag;
	boolean indicatorsVisibleFlag;
	boolean scopeBoxVisibleFlag;
	boolean stressesVisibleFlag;
	boolean activitiesVisibleFlag;
	boolean intermediateResultFlag;
	boolean threatReductionResultFlag;
	boolean textBoxesVisibleFlag;
	boolean groupBoxesVisibleFlag;
	boolean draftStrategyVisibleFlag;
}
