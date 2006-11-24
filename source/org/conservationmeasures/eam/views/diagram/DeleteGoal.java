/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.Factor;

public class DeleteGoal extends DeleteAnnotationDoer
{
	String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Goal?",};
	}

	String getAnnotationIdListTag()
	{
		return Factor.TAG_GOAL_IDS;
	}

}
