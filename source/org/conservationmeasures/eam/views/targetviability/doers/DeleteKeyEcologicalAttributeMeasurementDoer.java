package org.conservationmeasures.eam.views.targetviability.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

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
	}
}
