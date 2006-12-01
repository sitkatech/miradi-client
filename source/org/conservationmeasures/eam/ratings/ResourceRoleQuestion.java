/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class ResourceRoleQuestion extends ChoiceQuestion
{
	public ResourceRoleQuestion(String tagToUse)
	{
		super(tagToUse, "Role", getRoleChoices());
	}

	static ChoiceItem[] getRoleChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("contact", "Team Contact"),
				new ChoiceItem("days", "Team Member"),
				new ChoiceItem("weeks", "Leader/Manager"),
				new ChoiceItem("months", "Process Facilitator"),
				new ChoiceItem("each", "Project Advisor"),
				new ChoiceItem("each", "Stakeholder"),
		};
	}
}
