/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz2.xmpz2schema;

import java.util.Vector;

import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.Utility;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

abstract public class Xmpz2GroupedConstants implements Xmpz2XmlConstants
{
	public static String createOredWrappableFactorNames()
	{
		return StringUtilities.joinListItems(getWrappableFactorNames(), PREFIX, "|", ID);
	}

	public static String[] getLinkableFactorNames()
	{
		return new String[]{
				BIODIVERSITY_TARGET,
				HUMAN_WELFARE_TARGET,
                BIOPHYSICAL_FACTOR,
                BIOPHYSICAL_RESULTS,
				CauseSchema.OBJECT_NAME,
				StrategySchema.OBJECT_NAME,
				THREAT_REDUCTION_RESULTS, 
				INTERMEDIATE_RESULTS,
				GROUP_BOX,
		};
	}

	public static String[] getWrappableFactorNames()
	{
		return new String[]{
				BIODIVERSITY_TARGET, 
				HUMAN_WELFARE_TARGET,
				BIOPHYSICAL_FACTOR,
				BIOPHYSICAL_RESULTS,
				CauseSchema.OBJECT_NAME,
				StrategySchema.OBJECT_NAME, 
				THREAT_REDUCTION_RESULTS, 
				INTERMEDIATE_RESULTS,
				GROUP_BOX,
				TEXT_BOX,
				SCOPE_BOX,
				ACTIVITY,
				STRESS,
		};
	}

	public static String[] getHtmlTagsAsElementNames()
	{
		return new String[] {"br", "b", "i", "u", "strike", "a", "ul", "ol", "div",};
	}

	public static Vector<String> getObjectTypeNamesToCreateIdSchemaElements()
	{
		final String[] objectTypeNames = new String[] {
		CONCEPTUAL_MODEL,
		RESULTS_CHAIN,
		DIAGRAM_FACTOR,
		DIAGRAM_LINK,
		BIODIVERSITY_TARGET,
		HUMAN_WELFARE_TARGET,
        BIOPHYSICAL_FACTOR,
        BIOPHYSICAL_RESULTS,
		CauseSchema.OBJECT_NAME,
		StrategySchema.OBJECT_NAME,
		THREAT_REDUCTION_RESULTS,
		INTERMEDIATE_RESULTS,
		GROUP_BOX,
		TEXT_BOX,
		SCOPE_BOX,
		ACTIVITY,
		STRESS,
		GOAL,
		OBJECTIVE,
		INDICATOR,
		KEY_ECOLOGICAL_ATTRIBUTE,
		TAGGED_OBJECT_SET_ELEMENT_NAME,
		SUB_TARGET,
		THREAT,
		ACCOUNTING_CODE,
		FUNDING_SOURCE,
		BUDGET_CATEGORY_ONE,
		BUDGET_CATEGORY_TWO,
		PROGRESS_REPORT,
		PROGRESS_PERCENT,
		EXPENSE_ASSIGNMENT,
		RESOURCE_ASSIGNMENT,
		RESOURCE_PLAN,
		RESOURCE_ID_ELEMENT_NAME,
		MEASUREMENT,
		METHOD,
		SUB_TASK,
		FUTURE_STATUS,
		};
		
		return Utility.convertToVector(objectTypeNames);
	}
}
