/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

public class TeamRoleQuestion extends StaticChoiceQuestion
{
	public TeamRoleQuestion(String tagToUse)
	{
		super(tagToUse, "Role", getRoleChoices());
	}

	static Vector<ChoiceItem> getRoleChoices()
	{
		return ResourceRoleQuestion.getGenericChoiceItems();
	}
}
