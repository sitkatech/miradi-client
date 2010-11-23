/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;
import org.miradi.main.EAM;

public class OpenStandardsAnalyzeUseAndAdaptQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		StaticLongDescriptionProvider provider = new StaticLongDescriptionProvider();
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("AnalyzeUseAndAdapt", EAM.text("Menu|4. Analyze, Use, Adapt"), provider);
		
		ChoiceItemWithChildren processStep4a = new ChoiceItemWithChildren("ProcessStep4", EAM.text("ProcessStep|4A. Prepare Data for Analysis"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep4a);
		processStep4a.addChild(new ChoiceItem("DevelopSystemsForRecordingStoringProcessingAndBackingUpProjectData", EAM.text("Develop systems for recording, storing, processing and backing up project data")));
		
		ChoiceItemWithChildren processStep4bChoiceItem = new ChoiceItemWithChildren("ProcessStep4B", EAM.text("ProcessStep|4B. Analyze Results"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep4bChoiceItem);
		processStep4bChoiceItem.addChild(new ChoiceItem("AnalyzeProjectResultsSndSssumptions", (EAM.text("Analyze project results and assumptions"))));
		processStep4bChoiceItem.addChild(new ChoiceItem("AnalyzeOperationalAndFinancialData", (EAM.text("Analyze operational and financial data"))));
		processStep4bChoiceItem.addChild(new ChoiceItem("DocumentDiscussionsAndDecisions", (EAM.text("Document discussions and decisions"))));

		
		ChoiceItemWithChildren processStep4cChoiceItem = new ChoiceItemWithChildren("ProcessStep4C", EAM.text("ProcessStep|4C. Adapt Project Plan"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep4cChoiceItem);
		processStep4cChoiceItem.addChild(new ChoiceItem("ReviseProjectPlanStrategicMonitoringOperationalAndWorkPlans", EAM.text("Revise project plan: strategic, monitoring, operational, and work plans")));
		
		return headerChoiceItem;
	}
}
