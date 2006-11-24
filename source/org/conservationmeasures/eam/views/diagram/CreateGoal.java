package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;

public class CreateGoal  extends CreateAnnotationDoer
{
	int getAnnotationType()
	{
		return ObjectType.GOAL;
	}
	String getAnnotationIdListTag()
	{
		return Factor.TAG_GOAL_IDS;
	}
}