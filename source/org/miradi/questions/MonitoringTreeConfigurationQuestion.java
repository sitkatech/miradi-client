/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.EAM;

public class MonitoringTreeConfigurationQuestion extends StaticChoiceQuestion
{
	public MonitoringTreeConfigurationQuestion()
	{
		super(getStaticChoices());
	}

	private static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem(EFFECTIVE_MONITORING_PLAN_CODE, EAM.text("Effectiveness Monitoring Plan")),
			new ChoiceItem(RESULTS_MONITORING_PLAN_CODE, EAM.text("Results Monitoring Plan")),
		};
	}
	
	public static final String EFFECTIVE_MONITORING_PLAN_CODE = "";
	public static final String RESULTS_MONITORING_PLAN_CODE = "StrategiesContainObjectives";
}
