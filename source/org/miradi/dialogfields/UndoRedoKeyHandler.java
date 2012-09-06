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
package org.miradi.dialogfields;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.actions.MiradiAction;
import org.miradi.main.EAM;

public class UndoRedoKeyHandler extends KeyAdapter
{
	public UndoRedoKeyHandler(Actions actionsToUse)
	{
		actions = actionsToUse;
	}
	
	@Override
	public void keyTyped(KeyEvent event)
	{
		try
		{
			char keyChar = event.getKeyChar();
			if(keyChar == ctrl('Z'))
			{
				FieldSaver.savePendingEdits();
				getUndoAction().doAction();
			}
			if(keyChar == ctrl('Y'))
			{
				FieldSaver.savePendingEdits();
				getRedoAction().doAction();
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}

	private int ctrl(char letter)
	{
		return Character.toUpperCase(letter) - '@';
	}
	
	private MiradiAction getUndoAction()
	{
		return getActions().get(ActionUndo.class);
	}
	
	private MiradiAction getRedoAction()
	{
		return getActions().get(ActionRedo.class);
	}
	
	private Actions getActions()
	{
		return actions;
	}
	
	private Actions actions;
}

