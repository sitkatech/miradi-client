package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class CreateGoal  extends CreateAnnotationDoer
{
	int getAnnotationType()
	{
		return ObjectType.GOAL;
	}
	String getAnnotationIdListTag()
	{
		return ConceptualModelNode.TAG_GOAL_IDS;
	}
}