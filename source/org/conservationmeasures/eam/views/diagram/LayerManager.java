/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.HashSet;
import java.util.Set;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.ViewData;

public class LayerManager
{
	public LayerManager()
	{
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
		intermediateResult = true;
		threatReductionResult = true;
	}
	
	public boolean isVisible(FactorCell node)
	{
		if(hiddenORefs.contains(node.getWrappedORef()))
			return false;
		
		boolean isDraft = node.getUnderlyingObject().isStatusDraft();
		if(mode.equals(ViewData.MODE_DEFAULT) && isDraft)
			return false;
		
		if(node.isContributingFactor())
			return areContributingFactorsVisible();
		
		if(node.isDirectThreat())
			return areDirectThreatsVisible();
		
		if (node.isIntermediateResult())
			return isIntermediateResultVisible();
		
		if (node.isThreatRedectionResult())
			return isThreatReductionResultVisible();
		
		if(isTypeVisible(node.getClass()))
			return true;
		
		if(mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM) && isDraft)
			return true;
		
		return false;
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
		return linkagesVisibleFlag;
	}
	
	public void setFactorLinksVisible(boolean newSetting)
	{
		linkagesVisibleFlag = newSetting;
	}

	public boolean areTargetLinksVisible()
	{
		return targetLinkagesVisibleFlag;
	}
	
	public void setTargetLinksVisible(boolean newSetting)
	{
		targetLinkagesVisibleFlag = newSetting;
	}

	public boolean areGoalsVisible()
	{
		return goalsVisibleFlag;
	}
	
	public boolean areObjectivesVisible()
	{
		return objectivesVisibleFlag;
	}
	
	public void setGoalsVisible(boolean newSetting)
	{
		goalsVisibleFlag = newSetting;
	}
	
	public void setObjectivesVisible(boolean newSetting)
	{
		objectivesVisibleFlag = newSetting;
	}
	
	public boolean areIndicatorsVisible()
	{
		return indicatorsVisibleFlag;
	}
	
	public void setIndicatorsVisible(boolean newSetting)
	{
		indicatorsVisibleFlag = newSetting;
	}

	public boolean isScopeBoxVisible()
	{
		return scopeBoxVisibleFlag;
	}
	
	public void setScopeBoxVisible(boolean newSetting)
	{
		scopeBoxVisibleFlag = newSetting;
	}
	
	public boolean isIntermediateResultVisible()
	{
		return intermediateResult;
	}
	
	public void setIntermediateResultVisible(boolean newSetting)
	{
		intermediateResult = newSetting;
	}
	
	public boolean isThreatReductionResultVisible()
	{
		return threatReductionResult;
	}
	
	public void setThreatReductionResultVisible(boolean newSetting)
	{
		threatReductionResult = newSetting;
	}
	
	
	Set hiddenNodeTypes;
	ORefList hiddenORefs;
	String mode;
	boolean contributingFactorsVisibleFlag;
	boolean directThreatsVisibleFlag;
	boolean linkagesVisibleFlag;
	boolean targetLinkagesVisibleFlag;
	boolean goalsVisibleFlag;
	boolean objectivesVisibleFlag;
	boolean indicatorsVisibleFlag;
	boolean scopeBoxVisibleFlag;
	boolean intermediateResult;
	boolean threatReductionResult;
}
