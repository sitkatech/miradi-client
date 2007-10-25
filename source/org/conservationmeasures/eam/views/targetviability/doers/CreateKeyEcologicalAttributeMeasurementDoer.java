package org.conservationmeasures.eam.views.targetviability.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class CreateKeyEcologicalAttributeMeasurementDoer extends AbstractKeyEcologicalAttributeDoer
{
	public int getRequiredObjectType()
	{
		return ObjectType.INDICATOR;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	}
}
