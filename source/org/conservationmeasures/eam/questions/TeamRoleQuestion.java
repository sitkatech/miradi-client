/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
