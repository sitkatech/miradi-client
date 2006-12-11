/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

public class DeleteWorkPlanTreeNode extends WorkPlanDoer
{
	public boolean isAvailable()
	{
		TreeTableNode selectedNode = getSelectedObject();
		if (selectedNode == null)
			return false;
		
		int selectedNodeType = selectedNode.getObjectReference().getObjectType();
		if (selectedNodeType == ObjectType.FAKE)
			return false;
		
		return (selectedNodeType == ObjectType.TASK || selectedNodeType == ObjectType.INDICATOR);
	}

	public void doIt()
	{
		if (!isAvailable())
			return;

		try
		{
			Project project = getProject();
			ORef selectedRef = getSelectedObject().getObjectReference();
			if (selectedRef.getObjectType() == ObjectType.TASK)
			{
				DeleteActivity.deleteTask(project, (Task)getSelectedObject().getObject());
			}
			else if (selectedRef.getObjectType() == ObjectType.INDICATOR)
			{
				Factor factor = getFactor(project, getSelectedObject().getObjectReference().getObjectId());
				DeleteAnnotationDoer.deleteAnnotationViaCommands(project, factor, (Indicator)getSelectedObject().getObject(), Factor.TAG_INDICATOR_IDS, getConfirmDialogText());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private Factor getFactor(Project project, BaseId indicatorId) throws Exception
	{
		ChainManager chainManager = new ChainManager(project);
		FactorSet factorSet = chainManager.findFactorsThatUseThisIndicator(indicatorId);
		return (Factor)factorSet.iterator().next();
	}

	private String[] getConfirmDialogText()
	{
		return new String[] {EAM.text("Are you sure you want to delete?")};
	}
}
