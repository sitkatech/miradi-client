/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.indicator;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class IndicatorPoolTablePanel extends ObjectPoolTablePanel
{
	public IndicatorPoolTablePanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, ObjectType.INDICATOR, 
				new IndicatorPoolTableModel(mainWindowToUse.getProject()), NAME_COLUMN_INDEX_TO_SORT_BY, UNIQUE_IDENTIFIER
		);
	}
	
	public static final String UNIQUE_IDENTIFIER = "IndicatorPoolTablePanel";
	private static final int NAME_COLUMN_INDEX_TO_SORT_BY = 1;
}
