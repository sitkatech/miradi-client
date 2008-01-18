/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.targetviability.doers;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.viability.ViabilityIndicatorNode;
import org.conservationmeasures.eam.dialogs.viability.KeyEcologicalAttributeMeasurementNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;

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

	public void deleteMeasurement() throws CommandFailedException
	{
		KeyEcologicalAttributeMeasurementNode measurementNode = (KeyEcologicalAttributeMeasurementNode) getSelectedTreeNodes()[0];
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			Measurement measurement = (Measurement) measurementNode.getObject();
			ViabilityIndicatorNode indicatorNode = (ViabilityIndicatorNode) measurementNode.getParentNode();
			Indicator indicator = (Indicator) indicatorNode.getObject();
			CommandSetObjectData removeMeasurement = CommandSetObjectData.createRemoveORefCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, measurementNode.getObjectReference());
			getProject().executeCommand(removeMeasurement);
			
			ORefList referrerRefs = measurement.findObjectsThatReferToUs();
			if (referrerRefs.size() > 0)
				return;
			
			Command[] commandsToClear = measurement.createCommandsToClear();
			getProject().executeCommandsWithoutTransaction(commandsToClear);
			
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
