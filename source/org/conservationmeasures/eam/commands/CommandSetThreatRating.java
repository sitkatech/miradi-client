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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class CommandSetThreatRating extends Command
{
	public CommandSetThreatRating(int threatIdToUpdate, int targetIdToUpdate, int criterionIdToUpdate, int valueIdToUse)
	{
		threatId = threatIdToUpdate;
		targetId = targetIdToUpdate;
		criterionId = criterionIdToUpdate;
		valueId = valueIdToUse;
	}
	
	public CommandSetThreatRating(DataInputStream dataIn) throws IOException
	{
		threatId = dataIn.readInt();
		targetId = dataIn.readInt();
		criterionId = dataIn.readInt();
		valueId = dataIn.readInt();
		previousValueId = dataIn.readInt();
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
		previousValueId = setBundleValue(target, valueId, IdAssigner.INVALID_ID);
	}

	public void undo(Project target) throws CommandFailedException
	{
		setBundleValue(target, previousValueId, valueId);
	}
	
	private int setBundleValue(Project target, int newValueId, int expectedValueId) throws CommandFailedException
	{
		try
		{
			ThreatRatingFramework framework = target.getThreatRatingFramework();
			ThreatRatingBundle bundle = framework.getBundle(getThreatId(), getTargetId());
			int oldValueId = bundle.getValueId(criterionId);
			if(expectedValueId != IdAssigner.INVALID_ID && expectedValueId != oldValueId)
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
		dataOut.writeInt(getThreatId());
		dataOut.writeInt(getTargetId());
		dataOut.writeInt(getCriterionId());
		dataOut.writeInt(getValueId());
		dataOut.writeInt(getPreviousValueId());
	}

	public int getThreatId()
	{
		return threatId;
	}
	
	public int getTargetId()
	{
		return targetId;
	}
	
	public int getCriterionId()
	{
		return criterionId;
	}
	
	public int getValueId()
	{
		return valueId;
	}

	public int getPreviousValueId()
	{
		return previousValueId;
	}


	public static final String COMMAND_NAME = "SetThreatRatingValue";

	int threatId;
	int targetId;
	int criterionId;
	int valueId;
	int previousValueId;
}
