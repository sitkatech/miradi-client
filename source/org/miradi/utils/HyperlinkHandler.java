/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit.LinkController;

public class HyperlinkHandler extends LinkController 
{
	public void activateHyperlink(JEditorPane editor, Point clickPosition) {
		
		int pos = editor.viewToModel(clickPosition);

		activateLink(pos, editor);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		JEditorPane editor = (JEditorPane) e.getSource();
		if (e.isControlDown() || (e.getClickCount() == 2))
		{
			editor.setEditable(false);
			super.mouseClicked(e);
			editor.setEditable(true);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		JEditorPane editor = (JEditorPane) e.getSource();
		if (editor.isEditable()) 
		{
			editor.setEditable(false);
			super.mouseMoved(e);
			editor.setEditable(true);
		}
	}
}

