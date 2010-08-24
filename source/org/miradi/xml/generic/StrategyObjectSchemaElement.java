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

package org.miradi.xml.generic;

import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.xml.wcs.WcsXmlConstants;

public class StrategyObjectSchemaElement extends FactorObjectSchemaElement
{
	public StrategyObjectSchemaElement()
	{
		super(WcsXmlConstants.STRATEGY);
		
		createOptionalIdListField(Factor.TAG_OBJECTIVE_IDS, XmlSchemaCreator.OBJECTIVE_ID_ELEMENT_NAME);
		createOptionalIdListField(Strategy.TAG_ACTIVITY_IDS, XmlSchemaCreator.ACITIVTY_ID_ELEMENT_NAME);
		createOptionalTextField(Strategy.TAG_STATUS);
		createCodeField(XmlSchemaCreator.STRATEGY_TAXONOMY_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_STRATEGY_TAXONOMY_CODE);
		createCodeField(XmlSchemaCreator.STRATEGY_IMPACT_RATING_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_STRATEGY_IMAPACT_RATING_CODE);
		createCodeField(XmlSchemaCreator.STRATEGY_FEASIBILITY_RATING_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_STRATEGY_FEASIBILITY_RATING_CODE);
		createOptionalTextField(Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		createOptionalIdListField(WcsXmlConstants.PROGRESS_REPORT_IDS, XmlSchemaCreator.PROGRESS_REPORT_ID_ELEMENT_NAME);
		createOptionalIdListField(WcsXmlConstants.EXPENSE_IDS, XmlSchemaCreator.EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME);
		createOptionalIdListField(Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, XmlSchemaCreator.RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME);
		createOptionalIdListField(Factor.TAG_INDICATOR_IDS, INDICATOR_TYPE_NAME);
	}
}
