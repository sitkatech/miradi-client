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
package org.miradi.dialogs.threatrating.upperPanel;

import java.util.Comparator;

import javax.swing.table.TableModel;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class TableModelChoiceItemComparator extends AbstractTableModelComparator
{
		public TableModelChoiceItemComparator(TableModel modelToUse, int columnToSort, ChoiceQuestion questionToUse)
		{	
			super(modelToUse, columnToSort);
			
			question = questionToUse;
		}
		
		@Override
		public int compare(Object object1, Object object2)
		{
			Integer row1 = (Integer)object1;
			Integer row2 = (Integer)object2;
			Object value1 = getValue(row1.intValue());
			Object value2 = getValue(row2.intValue());
			
			if (value1 == null || value2 == null)
				return -1;
			
			Comparable[] sortValues1 = new Comparable[]{((ChoiceItem) value1), getRefForRow(row1)};
			Comparable[] sortValues2 = new Comparable[]{((ChoiceItem) value2), getRefForRow(row2)};
			
			return compareDetails(sortValues1, sortValues2);
		}
		
		@Override
		protected int compareValues(Comparable element1, Comparable element2)
		{
			if (!(element1 instanceof ChoiceItem) || !(element2 instanceof ChoiceItem))
				return super.compareValues(element1, element2);
				
			ChoiceItem choiceItem1 = (ChoiceItem) element1;
			ChoiceItem choiceItem2 = (ChoiceItem) element2;
			Comparator<ChoiceItem> comparator = question.getComparator();
			
			return comparator.compare(choiceItem1, choiceItem2);
		}
		
		private ChoiceQuestion question;
}
