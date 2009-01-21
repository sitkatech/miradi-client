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
package org.miradi.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ProjectNameRestrictedTextField extends PanelTextField
{
	public ProjectNameRestrictedTextField()
	{
		this("");
	}

	public ProjectNameRestrictedTextField(String initialValue)
	{
		super(Project.MAX_PROJECT_FILENAME_LENGTH);
		
		initialize(initialValue);
	}

	private void initialize(String initialValue)
	{
		setDocument(new RestrictedDocument());
		setText(initialValue);
	}

	public class RestrictedDocument extends PlainDocument
	{
		public void insertString(int offset, String value, AttributeSet as) throws BadLocationException
		{
			String newValue = removeIllegalCharacters(value);
			super.insertString(offset, newValue, as);
		}

		private String removeIllegalCharacters(String value) throws BadLocationException
		{
			StringBuffer newValue = new StringBuffer();
			for (int index = 0; index < value.length(); ++index)
			{
				char character = value.charAt(index);
				if (EAM.isValidCharacter(character))
				{
					newValue.append(character);
				}
			}
			
			return newValue.toString();	
		}
	}
}
