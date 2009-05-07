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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Dimension;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.dialogs.treetables.MultiTreeTablePanel.ScrollPaneWithHideableScrollBar;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.TableSettings;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class AbstractAssignmentEditorComponent extends MultiTablePanel  implements CommandExecutedListener
{
	public AbstractAssignmentEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse)
	{
		super(mainWindowToUse);
		
		objectPicker = objectPickerToUse;
		
		getProject().addCommandExecutedListener(this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		becomeInactive();
		getProject().removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_DATE_UNIT_LIST_DATA))
				respondToExpandOrCollapseColumnEvent();

			if (event.isSetDataCommand())
				dataWasChanged();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An unexpected error has occurred"));
		}		
	}
	
	protected Actions getActions()
	{
		return getMainWindow().getActions();
	}
		
	protected ObjectPicker getPicker()
	{
		return objectPicker;
	}
	
	static class AssignmentsComponentTableScrollPane extends ScrollPaneWithHideableScrollBar
	{
		public AssignmentsComponentTableScrollPane(AbstractComponentTable contents)
		{
			super(contents);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
			widthSetter = new PersistentWidthSetterComponent(contents.getMainWindow(), this, contents.getUniqueTableIdentifier(), getPreferredSize().width);
		}
		
		public PersistentWidthSetterComponent getWidthSetterComponent()
		{
			return widthSetter;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			final Dimension size = super.getPreferredSize();
			if(widthSetter != null)
				size.width = widthSetter.getControlledWidth();
			return size;
		}
		
		@Override
		public Dimension getSize()
		{
			final Dimension size = super.getSize();
			if(widthSetter != null)
				size.width = widthSetter.getControlledWidth();
			return size;
		}
		
		private PersistentWidthSetterComponent widthSetter;
	}
	
	abstract protected void respondToExpandOrCollapseColumnEvent() throws Exception;
	
	abstract protected void dataWasChanged() throws Exception;
	
	private ObjectPicker objectPicker;
}
