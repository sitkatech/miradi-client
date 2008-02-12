/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.util.Vector;



public class ResourceRoleQuestion extends StaticChoiceQuestion
{
	public ResourceRoleQuestion()
	{
		super(getRoleChoices());
	}

	private static Vector<ChoiceItem> getRoleChoices()
	{
		Vector<ChoiceItem> choiceItems = new Vector();
		choiceItems.add(new ChoiceItem(TeamMemberRoleCode, "Team Member"));
		choiceItems.addAll(getGenericChoiceItems());
		
		return choiceItems;
	}

	public static Vector<ChoiceItem> getGenericChoiceItems()
	{
		Vector<ChoiceItem> choiceItems = new Vector();
		choiceItems.add(new ChoiceItem("Contact", "Team Contact"));
		choiceItems.add(new ChoiceItem("Leader", "Leader/Manager"));
		choiceItems.add(new ChoiceItem("Facilitator", "Process Facilitator"));
		choiceItems.add(new ChoiceItem("Advisor", "Project Advisor"));
		choiceItems.add(new ChoiceItem("Stakeholder", "Stakeholder"));
		
		return choiceItems;
	}
	
	public static final String TeamMemberRoleCode = "TeamMember";
}
