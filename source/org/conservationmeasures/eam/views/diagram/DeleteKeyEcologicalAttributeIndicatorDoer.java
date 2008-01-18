/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.dialogs.viability.ViabilityIndicatorNode;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.targetviability.doers.AbstractKeyEcologicalAttributeDoer;

public class DeleteKeyEcologicalAttributeIndicatorDoer extends AbstractKeyEcologicalAttributeDoer
{	
	public int getRequiredObjectType()
	{
		return ObjectType.INDICATOR;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
	
		ViabilityIndicatorNode selectedIndicatorNode = (ViabilityIndicatorNode)getSelectedTreeNodes()[0];
		deleteIndicator(getProject(), selectedIndicatorNode);
	}

	public static void deleteIndicator(Project project, ViabilityIndicatorNode selectedIndicatorNode) throws CommandFailedException
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

	public static Command[] createDeleteCommands(Project project, ViabilityIndicatorNode selectedIndicatorNode) throws Exception
	{
		KeyEcologicalAttributeNode  keaNode = (KeyEcologicalAttributeNode)selectedIndicatorNode.getParentNode();
		BaseObject thisAnnotation = project.findObject(ObjectType.INDICATOR, selectedIndicatorNode.getObject().getId());
		return DeleteIndicator.buildCommandsToDeleteReferencedObject(project, keaNode.getObject(), KeyEcologicalAttribute.TAG_INDICATOR_IDS, thisAnnotation);
	}
}