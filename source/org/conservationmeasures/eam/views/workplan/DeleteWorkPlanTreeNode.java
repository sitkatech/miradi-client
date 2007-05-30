/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

public class DeleteWorkPlanTreeNode extends AbstractTaskTreeDoer
{
	public boolean isAvailable()
	{
		BaseObject[] selected = getObjects();
		if(selected == null || selected.length != 1)
			return false;
	
		return (selected[0].getType() == ObjectType.TASK || selected[0].getType() == ObjectType.INDICATOR);
	}

	public void doIt()
	{
		if (!isAvailable())
			return;

		//FIXME: Richard: After overhaul of object update management: 
		// Looks like this code is duplicated
		// in eihter the chain manager and or Delete Annotation Doer 
		try
		{
			Project project = getProject();
			BaseObject object = getObjects()[0];
			if (object.getType() == ObjectType.TASK)
			{
				DeleteActivity.deleteTask(project, (Task)object);
			}
			else if (object.getType() == ObjectType.INDICATOR)
			{
				BaseObject owner = object.getOwner();
				
				if (owner.getType() == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
				{
					deleteAnnotation(project, object, owner, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
				}
				else if(Factor.isFactor(owner.getType()))
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

	private void deleteAnnotation(Project project, BaseObject objectToDelete, BaseObject owner, final String tagOfIdList) throws CommandFailedException
	{
		DeleteAnnotationDoer.deleteAnnotationViaCommands(project, owner, objectToDelete, tagOfIdList, getConfirmDialogText());
	}

	private String[] getConfirmDialogText()
	{
		return new String[] {EAM.text("Are you sure you want to delete?")};
	}
}
