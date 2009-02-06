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
		Vector<ChoiceItem> choiceItems = new Vector();
		choiceItems.add(new ChoiceItem(TeamMemberRoleCode, TEAM_MEMBER_LABEL));
		choiceItems.addAll(getGenericChoiceItems());
		
		return choiceItems;
	}

	public static Vector<ChoiceItem> getGenericChoiceItems()
	{
		Vector<ChoiceItem> choiceItems = new Vector();
		choiceItems.add(new ChoiceItem("Contact", EAM.text("Team Contact")));
		choiceItems.add(new ChoiceItem(TeamLeaderCode, EAM.text("Leader/Manager")));
		choiceItems.add(new ChoiceItem("Facilitator", EAM.text("Process Facilitator")));
		choiceItems.add(new ChoiceItem("Advisor", EAM.text("Project Advisor")));
		choiceItems.add(new ChoiceItem("Stakeholder", EAM.text("Stakeholder")));
		
		return choiceItems;
	}
	
	public static final String TEAM_MEMBER_LABEL = EAM.text("Team Member");
	public static final String TeamMemberRoleCode = EAM.text("TeamMember");
	public static final String TeamLeaderCode = EAM.text("Leader");
}
