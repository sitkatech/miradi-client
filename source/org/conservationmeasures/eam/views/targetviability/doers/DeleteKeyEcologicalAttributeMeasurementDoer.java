package org.conservationmeasures.eam.views.targetviability.doers;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeIndicatorNode;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeMeasurementNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;

public class DeleteKeyEcologicalAttributeMeasurementDoer extends AbstractKeyEcologicalAttributeDoer
{
	public int getRequiredObjectType()
	{
		return ObjectType.MEASUREMENT;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			deleteMeasurement();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	//FIXME This code looks geared towards "owns", not "refers". Needs a significant
	//overhaul, I (kevin) think. (Could be wrong, of course).
	public void deleteMeasurement() throws CommandFailedException
	{
		KeyEcologicalAttributeMeasurementNode measurementNode = (KeyEcologicalAttributeMeasurementNode) getSelectedTreeNodes()[0];
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			Command[] commands = measurementNode.getObject().createCommandsToClear(); 
			getProject().executeCommandsWithoutTransaction(commands);
			
			KeyEcologicalAttributeIndicatorNode indicatorNode = (KeyEcologicalAttributeIndicatorNode) measurementNode.getParentNode();
			Indicator indicator = (Indicator) indicatorNode.getObject();
			CommandSetObjectData removeMeasurement = CommandSetObjectData.createRemoveORefCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, measurementNode.getObjectReference());
			getProject().executeCommand(removeMeasurement);
			
			CommandDeleteObject deleteMeasurement = new CommandDeleteObject(measurementNode.getObjectReference());
			getProject().executeCommand(deleteMeasurement);
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
}
