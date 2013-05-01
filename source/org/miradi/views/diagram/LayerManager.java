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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.DiagramModeQuestion;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.utils.CodeList;

public class LayerManager
{
	public LayerManager(DiagramObject diagramObjectToUse)
	{
		diagramObject = diagramObjectToUse;
	
		hiddenORefs = new ORefList();
		mode = DiagramModeQuestion.MODE_DEFAULT;
	}
	
	public boolean isVisible(DiagramObject diagramObjectToUse, FactorCell node) throws Exception
	{
		if(hiddenORefs.contains(node.getWrappedFactorRef()))
			return false;
		
		boolean isDraft = node.getWrappedFactor().isStatusDraft();
		if (isDraft)
		{
			if (isResultsChain(diagramObjectToUse))
				return false;

			if(mode.equals(DiagramModeQuestion.MODE_STRATEGY_BRAINSTORM))
				return areDraftsVisible(node);
			
			return false;
		}
		
		return isTypeVisible(node.getWrappedFactor().getTypeName());
	}

	private boolean isHiddenInDiagramObject(DiagramObject diagramObjectToUse, String objectTypeName) throws Exception
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
		if (isSafeDiagramObject(diagramObjectToUse))
			return diagramObjectToUse.isResultsChain();
		
		return false;
	}

	public boolean areAllNodesVisible() throws Exception
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
	
	public boolean areGoalsVisible() throws Exception
	{
		return isTypeVisible(GoalSchema.OBJECT_NAME);
	}
	
	public boolean areObjectivesVisible() throws Exception
	{
		return isTypeVisible(ObjectiveSchema.OBJECT_NAME);
	}
	
	public void setGoalsVisible(boolean newSetting) throws Exception
	{
		setVisibility(GoalSchema.OBJECT_NAME, newSetting);
	}
	
	public void setObjectivesVisible(boolean newSetting) throws Exception
	{
		setVisibility(ObjectiveSchema.OBJECT_NAME, newSetting);
	}

	public void setVisibility(String typeName, boolean isVisible) throws Exception
	{
		CodeList hiddenTypes = getDiagramObject().getHiddenTypes();		
		if (isVisible && hiddenTypes.contains(typeName))
			hiddenTypes.removeCode(typeName);
		if (!isVisible)
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

	private boolean areDraftsVisible(FactorCell node) throws Exception
	{
		if (!node.isStrategy())
			throw new RuntimeException("Unexpected non strategy draft");
		
		return isTypeVisible(Strategy.OBJECT_NAME_DRAFT);
	}

	public boolean areIndicatorsVisible() throws Exception
	{
		return isTypeVisible(IndicatorSchema.OBJECT_NAME);
	}
	
	public void setIndicatorsVisible(boolean newSetting) throws Exception
	{
		setVisibility(IndicatorSchema.OBJECT_NAME, newSetting);
	}

	public boolean isTypeVisible(String objectTypeName) throws Exception
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
	private ORefList hiddenORefs;
	private String mode;
}
