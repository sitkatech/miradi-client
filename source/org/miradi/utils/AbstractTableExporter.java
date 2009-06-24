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
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public abstract class AbstractTableExporter
{
	public AbstractTableExporter(Project projectToUse)
	{
		this(projectToUse, NO_UNIQUE_MODEL_IDENTIFIER);
	}
	
	public AbstractTableExporter(Project projectToUse, String uniqueModelIdentifierToUse)
	{
		project = projectToUse;
		uniqueModelIdentifier = uniqueModelIdentifierToUse;
		
		CodeList arrangedColumnCodes = ColumnSequenceSaver.calculateArrangedColumnCodes(getArrangedColumnCodes(), getModelColumnSequence());
		CodeList modelColumnCodes = getModelColumnSequence();
		modelColumnIndexes  = buildModelColumnIndexArray(arrangedColumnCodes, modelColumnCodes);
	}
		
	public int convertToModelColumn(int tableColumn)
	{
		return modelColumnIndexes[tableColumn];
	}

	private CodeList getArrangedColumnCodes()
	{
		CodeList desiredColumnCodes = ColumnSequenceSaver.getDesiredColumnCodes(getProject(), getUniqueModelIdentifier());
		if (desiredColumnCodes != null)
			return desiredColumnCodes;
		
		return getModelColumnSequence();
	}

	public static int[] buildModelColumnIndexArray(CodeList desiredSequenceCodes, CodeList modelColumnCodes)
	{
		int[] thisModelColumnIndexes = new int[modelColumnCodes.size()];
		for (int tableColumn = 0; tableColumn < modelColumnCodes.size(); ++tableColumn)
		{
			String modelColumnName = modelColumnCodes.get(tableColumn);
			int indexOfModelColumn = desiredSequenceCodes.find(modelColumnName);
			if (indexOfModelColumn < 0)
				indexOfModelColumn = tableColumn;
			
			thisModelColumnIndexes[tableColumn] = indexOfModelColumn;
		}
		
		return thisModelColumnIndexes;
	}
	
	private CodeList getModelColumnSequence()
	{
		CodeList currentColumnTagSequences = new CodeList();
		for (int tableColumn = 0; tableColumn < getColumnCount(); ++tableColumn)
		{	
			currentColumnTagSequences.add(getModelColumnName(tableColumn));
		}
		
		return currentColumnTagSequences;
	}
	
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
	
	private Project getProject()
	{
		return project;
	}
	
	private String getUniqueModelIdentifier()
	{
		return uniqueModelIdentifier;
	}
	
//FIXME urgen uncomment after rename	
//	public int getModelDepth(int row, int tableColumn)
//	{
//		int modelColumn = convertToModelColumn(tableColumn);
//		return getDepth(row, modelColumn);
//	}
//	
//	public String getModelTextAt(int row, int tableColumn)
//	{
//		int modelColumn = convertToModelColumn(tableColumn);
//		return getTextAt(row, modelColumn);
//	}
//	
//	public ChoiceItem getModelChoiceItemAt(int row, int tableColumn)
//	{
//		int modelColumn = convertToModelColumn(tableColumn);
//		return getChoiceItemAt(row, modelColumn);
//	}
//	
//	public String getModelColumnName(int tableColumn)
//	{
//		int modelColumn = convertToModelColumn(tableColumn);
//		return getColumnName(modelColumn);
//	}
//	
	abstract public int getMaxDepthCount();
	abstract public int getModelDepth(int row, int tableColumn);
	abstract public int getColumnCount();
	abstract public int getRowCount();
	abstract public String getModelTextAt(int row, int tableColumn);
	abstract public ChoiceItem getModelChoiceItemAt(int row, int tableColumn);
	abstract public int getRowType(int row);
	abstract public BaseObject getBaseObjectForRow(int row);
	abstract public String getModelColumnName(int tableColumn);
	
	//TODO these two methods were created to export details of tree or table.
	// we currently dont export details of tree or tables, and these methods might
	// need to be removed
	abstract public Vector<Integer> getAllTypes();
	abstract public ORefList getAllRefs(int objectType);
	
	private static final String CODE_LIST_SEPERATOR = ";";
	public static final String NO_UNIQUE_MODEL_IDENTIFIER = "";
	private String uniqueModelIdentifier;
	private Project project;
	private int[] modelColumnIndexes;  
}
