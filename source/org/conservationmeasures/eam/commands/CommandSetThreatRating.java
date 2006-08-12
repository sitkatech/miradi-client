/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class CommandSetThreatRating extends Command
{
	public CommandSetThreatRating(BaseId threatIdToUpdate, BaseId targetIdToUpdate, BaseId criterionIdToUpdate, BaseId valueIdToUse)
	{
		threatId = threatIdToUpdate;
		targetId = targetIdToUpdate;
		criterionId = criterionIdToUpdate;
		valueId = valueIdToUse;
	}
	
	public CommandSetThreatRating(DataInputStream dataIn) throws IOException
	{
		threatId = new BaseId(dataIn.readInt());
		targetId = new BaseId(dataIn.readInt());
		criterionId = new BaseId(dataIn.readInt());
		valueId = new BaseId(dataIn.readInt());
		previousValueId = new BaseId(dataIn.readInt());
	}

	public String toString()
	{
		return getCommandName() + ": " + threatId + ", " + targetId + "," + criterionId + "," + valueId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousValueId = setBundleValue(target, valueId, new BaseId());
	}

	public void undo(Project target) throws CommandFailedException
	{
		setBundleValue(target, previousValueId, valueId);
	}
	
	private BaseId setBundleValue(Project target, BaseId newValueId, BaseId expectedValueId) throws CommandFailedException
	{
		try
		{
			ThreatRatingFramework framework = target.getThreatRatingFramework();
			ThreatRatingBundle bundle = framework.getBundle(getThreatId(), getTargetId());
			BaseId oldValueId = bundle.getValueId(criterionId);
			if(!expectedValueId.isInvalid() && !expectedValueId.equals(oldValueId))
				throw new Exception(getCommandName() + " expected " + expectedValueId + " but was " + oldValueId);
			bundle.setValueId(criterionId, newValueId);
			framework.saveBundle(bundle);
			return oldValueId;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getThreatId().asInt());
		dataOut.writeInt(getTargetId().asInt());
		dataOut.writeInt(getCriterionId().asInt());
		dataOut.writeInt(getValueId().asInt());
		dataOut.writeInt(getPreviousValueId().asInt());
	}

	public BaseId getThreatId()
	{
		return threatId;
	}
	
	public BaseId getTargetId()
	{
		return targetId;
	}
	
	public BaseId getCriterionId()
	{
		return criterionId;
	}
	
	public BaseId getValueId()
	{
		return valueId;
	}

	public BaseId getPreviousValueId()
	{
		return previousValueId;
	}


	public static final String COMMAND_NAME = "SetThreatRatingValue";

	BaseId threatId;
	BaseId targetId;
	BaseId criterionId;
	BaseId valueId;
	BaseId previousValueId;
}
