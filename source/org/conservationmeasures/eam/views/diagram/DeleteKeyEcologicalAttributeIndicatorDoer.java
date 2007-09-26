/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeIndicatorNode;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

public class DeleteKeyEcologicalAttributeIndicatorDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects() == null)
			return false;
		
		if ((getObjects().length != 1))
			return false;
		
		if (getSelectedObjectType() != ObjectType.INDICATOR)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
	
		KeyEcologicalAttributeIndicatorNode selectedIndicatorNode = (KeyEcologicalAttributeIndicatorNode)getSelectedTreeNodes()[0];
		deleteIndicator(getProject(), selectedIndicatorNode);
	}

	public static void deleteIndicator(Project project, KeyEcologicalAttributeIndicatorNode selectedIndicatorNode) throws CommandFailedException
	{

		project.executeCommand(new CommandBeginTransaction());
		try
		{
			Command[] commands = createDeleteCommands(project, selectedIndicatorNode); 
			project.executeCommandsWithoutTransaction(commands);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	public static Command[] createDeleteCommands(Project project, KeyEcologicalAttributeIndicatorNode selectedIndicatorNode) throws Exception
	{
		KeyEcologicalAttributeNode  keaNode = (KeyEcologicalAttributeNode)selectedIndicatorNode.getParentNode();
		BaseObject thisAnnotation = project.findObject(ObjectType.INDICATOR, selectedIndicatorNode.getObject().getId());
		return DeleteIndicator.buildCommandsToDeleteAnnotation(project, keaNode.getObject(), KeyEcologicalAttribute.TAG_INDICATOR_IDS, thisAnnotation);
	}

	WorkPlanView view;
}