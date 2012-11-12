/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.schemas.ThreatReductionResultSchema;

public class FactorNoderSorter extends NodeSorter
{
	@Override
	protected int[] getNodeSortOrder()
	{
		return new int[] {
			ScopeBoxSchema.getObjectType(),
			TargetSchema.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			StressSchema.getObjectType(),
			CauseSchema.getObjectType(),
			ThreatReductionResultSchema.getObjectType(),
			IntermediateResultSchema.getObjectType(),
			StrategySchema.getObjectType(),
			TaskSchema.getObjectType(),
			GroupBoxSchema.getObjectType(),
			TextBoxSchema.getObjectType(),
		};
	}
}
