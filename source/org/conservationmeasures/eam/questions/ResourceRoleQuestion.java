/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;



public class ResourceRoleQuestion extends StaticChoiceQuestion
{
	public ResourceRoleQuestion(String tagToUse)
	{
		super(tagToUse, "Role", getRoleChoices());
	}

	static ChoiceItem[] getRoleChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(TeamMemberRoleCode, "Team Member"),
				new ChoiceItem("Contact", "Team Contact"),
				new ChoiceItem("Leader", "Leader/Manager"),
				new ChoiceItem("Facilitator", "Process Facilitator"),
				new ChoiceItem("Advisor", "Project Advisor"),
				new ChoiceItem("Stakeholder", "Stakeholder"),
		};
	}
	public static final String TeamMemberRoleCode = "TeamMember";
}
