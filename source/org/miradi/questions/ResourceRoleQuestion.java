/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.questions;

import java.util.Vector;

import org.miradi.main.EAM;



public class ResourceRoleQuestion extends StaticChoiceQuestion
{
	public ResourceRoleQuestion()
	{
		super(getRoleChoices());
	}

	private static Vector<ChoiceItem> getRoleChoices()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		choiceItems.add(new ChoiceItem(TEAM_MEMBER_ROLE_CODE, getTeamMemberLabel()));
		choiceItems.addAll(getGenericChoiceItems());
		
		return choiceItems;
	}

	private static String getTeamMemberLabel()
	{
		return EAM.text("Team Member");
	}

	public static Vector<ChoiceItem> getGenericChoiceItems()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		choiceItems.add(new ChoiceItem(CONTACT_CODE, EAM.text("Team Contact")));
		choiceItems.add(new ChoiceItem(TEAM_LEADER_CODE, EAM.text("Leader/Manager")));
		choiceItems.add(new ChoiceItem(FACILITATOR_CODE, EAM.text("Process Facilitator")));
		choiceItems.add(new ChoiceItem(ADVISER_CODE, EAM.text("Project Advisor")));
		choiceItems.add(new ChoiceItem(STAKEHOLDER_CODE, EAM.text("Stakeholder")));
		
		return choiceItems;
	}
	
	public static final String TEAM_MEMBER_ROLE_CODE = "TeamMember";
	
	public static final String CONTACT_CODE = "Contact";
	public static final String TEAM_LEADER_CODE = "Leader";
	public static final String FACILITATOR_CODE = "Facilitator";
	public static final String ADVISER_CODE = "Advisor"; 
	public static final String STAKEHOLDER_CODE = "Stakeholder";
}
