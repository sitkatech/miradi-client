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
package org.miradi.views.umbrella.doers;

import org.miradi.objects.Desire;
import org.miradi.objects.ProgressPercent;
import org.miradi.views.diagram.doers.CreateAnnotationWithFactorParent;

//FIXME urgent - rename in next commit
public class AbstractCreateProgressPercent extends CreateAnnotationWithFactorParent
{
	public String getAnnotationListTag()
	{
		return Desire.TAG_PROGRESS_PERCENT_REFS;
	}

	public int getAnnotationType()
	{
		return ProgressPercent.getObjectType();
	}
}
