/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
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

		//FIXME: Looks like this code is duplicated in eihter the chain manager and or Delete Annotation Doer (Richard)
		try
		{
			Project project = getProject();
			ChainManager chainManager = new ChainManager(project);
			EAMObject object = getObjects()[0];
			if (object.getType() == ObjectType.TASK)
			{
				DeleteActivity.deleteTask(project, (Task)object);
			}
			else if (object.getType() == ObjectType.INDICATOR)
			{
				EAMObject owner = chainManager.getOwner(object.getRef());
				if (owner.getType() == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
				{
					deleteAnnotation(project, object, owner, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
				}
				else if(owner.getType() == ObjectType.FACTOR)
				{
					deleteAnnotation(project, object, owner, Factor.TAG_INDICATOR_IDS);
				}
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void deleteAnnotation(Project project, EAMObject objectToDelete, EAMObject owner, final String tagOfIdList) throws CommandFailedException
	{
		DeleteAnnotationDoer.deleteAnnotationViaCommands(project, owner, (Indicator)objectToDelete, tagOfIdList, getConfirmDialogText());
	}

	private String[] getConfirmDialogText()
	{
		return new String[] {EAM.text("Are you sure you want to delete?")};
	}
}
