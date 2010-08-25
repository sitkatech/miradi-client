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
package org.miradi.views.planning.doers;

import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;

public class PlanningColumnsEditorDoer extends AbstractPlanningViewConfigurationCodeListEditorDoer
{
	@Override
	protected int getGridColumnCount()
	{
		return 1;
	}
	
	@Override
	protected String getConfigurationTag()
	{
		return ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION;
	}

	@Override
	protected ChoiceQuestion getConfigurationQuestion()
	{
		return getProject().getQuestion(CustomPlanningColumnsQuestion.class);
	}
}
