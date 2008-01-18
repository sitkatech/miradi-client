/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.util.HashMap;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.SimpleThreatRatingFramework;

public class CommandSetThreatRating extends Command
{
	public CommandSetThreatRating(FactorId threatIdToUpdate, FactorId targetIdToUpdate, BaseId criterionIdToUpdate, BaseId valueIdToUse)
	{
		threatId = threatIdToUpdate;
		targetId = targetIdToUpdate;
		criterionId = criterionIdToUpdate;
		valueId = valueIdToUse;
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
		previousValueId = setBundleValue(target, valueId, BaseId.INVALID);
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandSetThreatRating(getThreatId(), getTargetId(), getCriterionId(), getPreviousValueId());
	}

	private BaseId setBundleValue(Project target, BaseId newValueId, BaseId expectedValueId) throws CommandFailedException
	{
		try
		{
			SimpleThreatRatingFramework framework = target.getSimpleThreatRatingFramework();
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

	public FactorId getThreatId()
	{
		return threatId;
	}
	
	public FactorId getTargetId()
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

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("THREAT_ID", threatId);
		dataPairs.put("TARGET_ID", targetId);
		dataPairs.put("CRITERION_ID", criterionId);
		dataPairs.put("VALUE_ID", valueId);
		return dataPairs;
	}

	public static final String COMMAND_NAME = "SetThreatRatingValue";

	FactorId threatId;
	FactorId targetId;
	BaseId criterionId;
	BaseId valueId;
	BaseId previousValueId;
}
