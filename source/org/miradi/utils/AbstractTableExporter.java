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
package org.miradi.utils;

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public abstract class AbstractTableExporter
{
	abstract public int getMaxDepthCount();
	abstract public int getDepth(int row, int column);
	abstract public int getColumnCount();
	abstract public int getRowCount();
	abstract public String getTextAt(int row, int column);
	abstract public ChoiceItem getChoiceItemAt(int row, int column);
	abstract public int getRowType(int row);
	abstract public BaseObject getBaseObjectForRow(int row);
	abstract public String getHeaderFor(int column);
	
	//TODO these two methods were created to export details of tree or table.
	// we currently dont export details of tree or tables, and these methods might
	// need to be removed
	abstract public Vector<Integer> getAllTypes();
	abstract public ORefList getAllRefs(int objectType);
	
	public String getStyleTagAt(int row, int column)
	{
		return "";
	}
	
	public String getSafeValue(Object object)
	{
		if (object == null)
			return "";
		
		return object.toString();
	}
	
	protected String createExportableCodeList(CodeList codeList, ChoiceQuestion question)
	{
		StringBuffer codeListAsString = new StringBuffer();
		for (int index = 0; index < codeList.size(); ++index)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(codeList.get(index));
			codeListAsString.append(choiceItem.getLabel());
			codeListAsString.append(CODE_LIST_SEPERATOR);
		}
		
		return codeListAsString.toString();
	}
	
	private static final String CODE_LIST_SEPERATOR = ";";
}
