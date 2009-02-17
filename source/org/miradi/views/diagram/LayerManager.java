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

import org.miradi.diagram.cells.FactorCell;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.TextBox;
import org.miradi.objects.ViewData;

public class LayerManager
{
	public LayerManager(DiagramObject diagramObjectToUse)
	{
		diagramObject = diagramObjectToUse;
	
		hiddenNodeTypes = new HashSet();
		hiddenORefs = new ORefList();
		mode = ViewData.MODE_DEFAULT;
		contributingFactorsVisibleFlag = true;
		directThreatsVisibleFlag = true;
		linkagesVisibleFlag = true;
		targetLinkagesVisibleFlag = true;
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

		if(isTypeVisible(node.getClass()))
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

	public boolean isTypeVisible(Class nodeClass)
	{
		return !hiddenNodeTypes.contains(nodeClass);
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
		return hiddenNodeTypes.isEmpty() && hiddenORefs.isEmpty();
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
		return contributingFactorsVisibleFlag;
	}
	
	public void setContributingFactorsVisible(boolean newSetting)
	{
		contributingFactorsVisibleFlag = newSetting;
	}
	
	public boolean areDirectThreatsVisible()
	{
		return directThreatsVisibleFlag;
	}
	
	public void setDirectThreatsVisible(boolean newSetting)
	{
		directThreatsVisibleFlag = newSetting;
	}
	
	public boolean areFactorLinksVisible()
	{
		return isTypeVisible(linkagesVisibleFlag, FactorLink.OBJECT_NAME);
	}
	
	public void setFactorLinksVisible(boolean newSetting)
	{
		linkagesVisibleFlag = newSetting;
	}

	public boolean areTargetLinksVisible()
	{
		return isTypeVisible(targetLinkagesVisibleFlag, FactorLink.OBJECT_NAME_TARGETLINK);
	}
	
	public void setTargetLinksVisible(boolean newSetting)
	{
		targetLinkagesVisibleFlag = newSetting;
	}

	public boolean areGoalsVisible()
	{
		return isTypeVisible(goalsVisibleFlag, Goal.OBJECT_NAME);
	}
	
	public boolean areObjectivesVisible()
	{
		return isTypeVisible(objectivesVisibleFlag, Objective.OBJECT_NAME);
	}
	
	public void setGoalsVisible(boolean newSetting)
	{
		goalsVisibleFlag = newSetting;
	}
	
	public void setObjectivesVisible(boolean newSetting)
	{
		objectivesVisibleFlag = newSetting;
	}
	
	public boolean areTextBoxesVisible()
	{
		return isTypeVisible(textBoxesVisibleFlag, TextBox.OBJECT_NAME);
	}

	public boolean areGroupBoxesVisible()
	{
		return isTypeVisible(groupBoxesVisibleFlag, GroupBox.OBJECT_NAME);
	}
	
	public boolean areDraftsVisible(FactorCell node)
	{
		if (!node.isStrategy())
			throw new RuntimeException("Unexpected non strategy draft");
		
		return areDraftStrategiesVisible();
	}

	public boolean areDraftStrategiesVisible()
	{
		return draftStrategyVisibleFlag;
	}
	
	public void setDraftStrategiesVisible(boolean newSetting)
	{
		draftStrategyVisibleFlag = newSetting;
	}

	public boolean areIndicatorsVisible()
	{
		return isTypeVisible(indicatorsVisibleFlag, Indicator.OBJECT_NAME);
	}
	
	public void setIndicatorsVisible(boolean newSetting)
	{
		indicatorsVisibleFlag = newSetting;
	}

	public boolean isScopeBoxVisible()
	{
		return isTypeVisible(scopeBoxVisibleFlag, DiagramLegendPanel.SCOPE_BOX);
	}
	
	public void setScopeBoxVisible(boolean newSetting)
	{
		scopeBoxVisibleFlag = newSetting;
	}
	
	public boolean areStressesVisible()
	{
		return stressesVisibleFlag;
	}
	
	public void setStressesVisible(boolean newSetting)
	{
		stressesVisibleFlag = newSetting;
	}
	
	public boolean areActivitiesVisible()
	{
		return activitiesVisibleFlag;
	}
	
	public void setActivitiesVisible(boolean newSetting)
	{
		activitiesVisibleFlag = newSetting;
	}
	
	public boolean areIntermediateResultsVisible()
	{
		return intermediateResultFlag;
	}
	
	public void setIntermediateResultVisible(boolean newSetting)
	{
		intermediateResultFlag = newSetting;
	}
	
	public boolean areThreatReductionResultsVisible()
	{
		return threatReductionResultFlag;
	}
	
	public void setThreatReductionResultVisible(boolean newSetting)
	{
		threatReductionResultFlag = newSetting;
	}
	
	private boolean isTypeVisible(boolean defaultVisibility, String objectTypeName)
	{
		if (isHiddenInDiagramObject(getDiagramObject(), objectTypeName))
			return false;
		
		return defaultVisibility;
	}
		
	public DiagramObject getDiagramObject()
	{
		return diagramObject;
	}
	
	public void setDiagramObject(DiagramObject diagramContentsToUse)
	{
		diagramObject = diagramContentsToUse;
	}
	
	private DiagramObject diagramObject;
	private Set hiddenNodeTypes;
	private ORefList hiddenORefs;
	private String mode;
	private boolean contributingFactorsVisibleFlag;
	private boolean directThreatsVisibleFlag;
	private boolean linkagesVisibleFlag;
	private boolean targetLinkagesVisibleFlag;
	private boolean goalsVisibleFlag;
	private boolean objectivesVisibleFlag;
	private boolean indicatorsVisibleFlag;
	private boolean scopeBoxVisibleFlag;
	private boolean stressesVisibleFlag;
	private boolean activitiesVisibleFlag;
	private boolean intermediateResultFlag;
	private boolean threatReductionResultFlag;
	private boolean textBoxesVisibleFlag;
	private boolean groupBoxesVisibleFlag;
	private boolean draftStrategyVisibleFlag;
}
