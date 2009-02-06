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
package org.miradi.forms.summary;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;

public class PlanningTabFinancialSubPanelForm extends FieldPanelSpec
{
	public PlanningTabFinancialSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(EAM.text("Financial"));

		int type = ProjectMetadata.getObjectType();
		
		addLabelAndFieldsWithLabels(EAM.text("Label|Currency"), type, 
				new String[] {ProjectMetadata.TAG_CURRENCY_TYPE, ProjectMetadata.TAG_CURRENCY_SYMBOL});
		
		addLabelAndField(type, ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		addLabelAndField(type, ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		addLabelAndField(type, ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		addLabelAndField(type, ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		addLabelAndField(type, ProjectMetadata.TAG_FINANCIAL_COMMENTS);
		
	}
}
