/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.util.HashSet;
import java.util.Set;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.ViewData;

public class LayerManager
{
	public LayerManager()
	{
		hiddenNodeTypes = new HashSet();
		hiddenIds = new IdList();
		mode = ViewData.MODE_DEFAULT;
		indirectFactorsVisibleFlag = true;
		directThreatsVisibleFlag = true;
		linkagesVisibleFlag = true;
		desiresVisibleFlag = true;
		indicatorsVisibleFlag = true;
	}
	
	public boolean isVisible(DiagramNode node)
	{
		if(hiddenIds.contains(node.getDiagramNodeId()))
			return false;
		
		boolean isDraft = node.getUnderlyingObject().isStatusDraft();
		if(mode.equals(ViewData.MODE_DEFAULT) && isDraft)
			return false;
		
		if(node.isIndirectFactor())
			return areIndirectFactorsVisible();
		
		if(node.isDirectThreat())
			return areDirectThreatsVisible();
		
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
		return hiddenNodeTypes.isEmpty() && hiddenIds.isEmpty();
	}
	
	public void setHiddenIds(IdList idsToHide)
	{
		hiddenIds = new IdList(idsToHide);
	}
	
	public void setMode(String newMode)
	{
		mode = newMode;
	}
	
	public boolean areIndirectFactorsVisible()
	{
		return indirectFactorsVisibleFlag;
	}
	
	public void setIndirectFactorsVisible(boolean newSetting)
	{
		indirectFactorsVisibleFlag = newSetting;
	}
	
	public boolean areDirectThreatsVisible()
	{
		return directThreatsVisibleFlag;
	}
	
	public void setDirectThreatsVisible(boolean newSetting)
	{
		directThreatsVisibleFlag = newSetting;
	}
	
	public boolean areLinkagesVisible()
	{
		return linkagesVisibleFlag;
	}
	
	public void setLinkagesVisible(boolean newSetting)
	{
		linkagesVisibleFlag = newSetting;
	}
	
	public boolean areDesiresVisible()
	{
		return desiresVisibleFlag;
	}
	
	public void setDesiresVisible(boolean newSetting)
	{
		desiresVisibleFlag = newSetting;
	}
	
	public boolean areIndicatorsVisible()
	{
		return indicatorsVisibleFlag;
	}
	
	public void setIndicatorsVisible(boolean newSetting)
	{
		indicatorsVisibleFlag = newSetting;
	}

	Set hiddenNodeTypes;
	IdList hiddenIds;
	String mode;
	boolean indirectFactorsVisibleFlag;
	boolean directThreatsVisibleFlag;
	boolean linkagesVisibleFlag;
	boolean desiresVisibleFlag;
	boolean indicatorsVisibleFlag;
}
