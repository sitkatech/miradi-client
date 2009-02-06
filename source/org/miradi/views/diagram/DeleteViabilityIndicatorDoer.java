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

import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.views.targetviability.doers.AbstractKeyEcologicalAttributeDoer;

public class DeleteViabilityIndicatorDoer extends AbstractKeyEcologicalAttributeDoer
{	
	public Vector<Integer> getRequiredObjectTypes()
	{
		Vector<Integer> types = new Vector(1);
		types.add(Indicator.getObjectType());

		return types;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			TreeTableNode selectedIndicatorNode = getSelectedTreeNodes()[0];
			Command[] commands = createDeleteCommands(getProject(), selectedIndicatorNode); 
			getProject().executeCommandsWithoutTransaction(commands);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public static Command[] createDeleteCommands(Project project, TreeTableNode selectedIndicatorNode) throws Exception
	{
		TreeTableNode parentNode = selectedIndicatorNode.getParentNode();
		BaseObject thisAnnotation = selectedIndicatorNode.getObject();
		return DeleteIndicator.buildCommandsToDeleteAnnotation(project, parentNode.getObject(), getIndicatorListTag(parentNode.getObject()), thisAnnotation);
	}
}