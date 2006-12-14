/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.questions;

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
				new ChoiceItem("Contact", "Team Contact"),
				new ChoiceItem(TeamMemberRoleCode, "Team Member"),
				new ChoiceItem("Leader", "Leader/Manager"),
				new ChoiceItem("Facilitator", "Process Facilitator"),
				new ChoiceItem("Advisor", "Project Advisor"),
				new ChoiceItem("Stakeholder", "Stakeholder"),
		};
	}
	public static final String TeamMemberRoleCode = "TeamMember";
}
