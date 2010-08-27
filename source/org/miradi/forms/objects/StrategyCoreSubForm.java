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
package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;

public class StrategyCoreSubForm extends FieldPanelSpec
{
	public StrategyCoreSubForm()
	{
		int type = Strategy.getObjectType();
		
		addLabelAndFieldsWithLabels(EAM.text("Strategy"), type, new String[]{Strategy.TAG_SHORT_LABEL, Strategy.TAG_LABEL});
		addLabelAndField(type, Factor.TAG_TEXT);
		addLabelAndField(type, Strategy.TAG_TAXONOMY_CODE);
		addLabelAndFieldsWithLabels(EAM.text("Priority"), type, new String[]{Strategy.TAG_IMPACT_RATING, Strategy.TAG_FEASIBILITY_RATING, Strategy.PSEUDO_TAG_RATING_SUMMARY});
		addLabelAndField(type, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		addLabelAndFieldWithLabel(EAM.text("Progress"), type, Strategy.TAG_PROGRESS_REPORT_REFS);
	}
}
