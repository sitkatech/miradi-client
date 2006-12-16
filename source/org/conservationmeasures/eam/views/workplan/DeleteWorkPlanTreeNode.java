/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

public class DeleteWorkPlanTreeNode extends AbstractTaskTreeDoer
{
	public boolean isAvailable()
	{
		EAMObject[] selected = getObjects();
		if(selected == null || selected.length != 1)
			return false;
	
		return (selected[0].getType() == ObjectType.TASK || selected[0].getType() == ObjectType.INDICATOR);
	}

	public void doIt()
	{
		if (!isAvailable())
			return;

		try
		{
			Project project = getProject();
			EAMObject object = getObjects()[0];
			if (object.getType() == ObjectType.TASK)
			{
				DeleteActivity.deleteTask(project, (Task)object);
			}
			else if (object.getType() == ObjectType.INDICATOR)
			{
				Factor factor = getFactor(project, object.getId());
				DeleteAnnotationDoer.deleteAnnotationViaCommands(project, factor, (Indicator)object, Factor.TAG_INDICATOR_IDS, getConfirmDialogText());
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
