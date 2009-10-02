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

public class StrategyObjectSchemaElement extends FactorObjectSchemaElement
{
	public StrategyObjectSchemaElement()
	{
		super("Strategy");
		
		createIdListField(Factor.TAG_OBJECTIVE_IDS, XmlSchemaCreator.OBJECTIVE_ID_ELEMENT_NAME);
		createIdListField(Strategy.TAG_ACTIVITY_IDS, XmlSchemaCreator.ACITIVTY_ID_ELEMENT_NAME);
		createTextField(Strategy.TAG_STATUS);
		createCodeField(XmlSchemaCreator.STRATEGY_TAXONOMY_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_STRATEGY_TAXONOMY_CODE);
		createCodeField(XmlSchemaCreator.STRATEGY_IMPACT_RATING_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_STRATEGY_IMAPACT_RATING_CODE);
		createCodeField(XmlSchemaCreator.STRATEGY_FEASIBILITY_RATING_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_STRATEGY_FEASIBILITY_RATING_CODE);
		createTextField(Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		createIdListField(Strategy.TAG_PROGRESS_REPORT_REFS, XmlSchemaCreator.PROGRESS_REPORT_ID_ELEMENT_NAME);
		createIdListField(Strategy.TAG_EXPENSE_ASSIGNMENT_REFS, XmlSchemaCreator.EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME);
		createIdListField(Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, XmlSchemaCreator.RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME);
	}
}
