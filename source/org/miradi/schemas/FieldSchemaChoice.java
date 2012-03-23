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

package org.miradi.schemas;

import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;

public class FieldSchemaChoice extends AbstractQuestionBasedFieldSchema
{
	public FieldSchemaChoice(final String tagToUse, final ChoiceQuestion questionToUse)
	{
		super(tagToUse, questionToUse);
	}

	@Override
	public ObjectData createField(final BaseObject baseObjectToUse)
	{
		return new ChoiceData(getTag(), getQuestion());
	}
}
