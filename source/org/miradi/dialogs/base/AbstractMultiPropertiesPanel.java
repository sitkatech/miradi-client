/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.base;

import org.miradi.dialogs.viability.FutureStatusPropertiesPanel;
import org.miradi.dialogs.viability.ViabilityFutureStatusPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.schemas.KeyEcologicalAttributeSchema;

abstract public class AbstractMultiPropertiesPanel extends OverlaidObjectDataInputPanel
{
	public AbstractMultiPropertiesPanel(MainWindow mainWindowToUse,	ORef orefToUse)
	{
		super(mainWindowToUse, orefToUse);
	}
	
	private boolean isVibilityFutureStatus(ORefList refList)
	{
		return refList.getFilteredBy(KeyEcologicalAttributeSchema.getObjectType()).hasRefs();
	}

	protected AbstractObjectDataInputPanel getFutureStatusForViabilityMode(ORefList refList) throws Exception
	{
		if (isVibilityFutureStatus(refList))
			return getViabilityFutureStatusPanel();
		
		return getFutureStatusPanel();
	}

	private AbstractObjectDataInputPanel getFutureStatusPanel() throws Exception
	{
		if (futureStatusPropertiesPanel == null)
		{
			futureStatusPropertiesPanel = new FutureStatusPropertiesPanel(getProject());
			addPanel(futureStatusPropertiesPanel);
		}
		
		return futureStatusPropertiesPanel;
	}

	private AbstractObjectDataInputPanel getViabilityFutureStatusPanel() throws Exception
	{
		if (viabilityFutureStatusPropertiesPanel == null)
		{
			viabilityFutureStatusPropertiesPanel = new ViabilityFutureStatusPropertiesPanel(getProject());
			addPanel(viabilityFutureStatusPropertiesPanel);
		}
		
		return viabilityFutureStatusPropertiesPanel;
	}

	private FutureStatusPropertiesPanel futureStatusPropertiesPanel;
	private ViabilityFutureStatusPropertiesPanel viabilityFutureStatusPropertiesPanel;
}
