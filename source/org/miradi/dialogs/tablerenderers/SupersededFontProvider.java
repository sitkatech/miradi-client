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

package org.miradi.dialogs.tablerenderers;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.BaseObject;

public class SupersededFontProvider extends FontForObjectTypeProvider
{
	public SupersededFontProvider(MainWindow mainWindowToUse, DateUnit dateUnitToUse)
	{
		super(mainWindowToUse);
		
		dateUnit = dateUnitToUse;
	}
	
	@Override
	public Font getFont(BaseObject baseObject)
	{
		Font font = super.getFont(baseObject);
		if (isSuperseded(baseObject))
			font = createStrikethroughFont(font);
		
		return font;
	}

	private boolean isSuperseded(BaseObject baseObject)
	{
		try
		{
			return baseObject.isSuperseded(dateUnit);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private Font createStrikethroughFont(Font defaultFontToUse)
	{
		Map attributesMap = defaultFontToUse.getAttributes();
		attributesMap.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);

		return new Font(attributesMap);
	}
	
	private DateUnit dateUnit;
}
