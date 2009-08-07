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

import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Target;

public class ViabilityViewFontProvider extends FontForObjectTypeProvider
{
	public ViabilityViewFontProvider(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public Font getFont(BaseObject baseObject)
	{
		Font font = super.getFont(baseObject);
		if(Target.is(baseObject))
			return font.deriveFont(Font.BOLD);
		
		return font;
	}

}
