/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.ViewDoer;

public abstract class CreateAnnotationDoer extends ViewDoer
{
	
	abstract int getAnnotationType();
	abstract String getAnnotationIdListTag();


	public boolean isAvailable()
	{
		return (getSelectedFactor() != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Factor factor = getSelectedFactor();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(getAnnotationType());
			getProject().executeCommand(create);
			BaseId createdId = create.getCreatedId();
			getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(factor, getAnnotationIdListTag(), createdId));
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
	
	public Factor getSelectedFactor()
	{
		EAMObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(selected.getType() != ObjectType.FACTOR)
			return null;
		
		return (Factor)selected;
	}

}