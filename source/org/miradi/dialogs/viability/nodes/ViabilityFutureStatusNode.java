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
package org.miradi.dialogs.viability.nodes;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.icons.FutureStatusIcon;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FutureStatus;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TextAndIconChoiceItem;
import org.miradi.schemas.FutureStatusSchema;

public class ViabilityFutureStatusNode extends TreeTableNode
{
	public ViabilityFutureStatusNode(TreeTableNode parentNodeToUse, FutureStatus futureStatusToUse) throws Exception
	{
		parentNode = parentNodeToUse;
		
		futureStatus = futureStatusToUse;
		rebuild();
	}
	
	@Override
	public BaseObject getObject()
	{
		return futureStatus;
	}

	@Override
	public ORef getObjectReference()
	{
		return getObject().getRef();
	}
	
	@Override
	public int getType()
	{
		return FutureStatusSchema.getObjectType();
	}

	@Override
	public int getChildCount()
	{
		return 0;
	}

	@Override
	public TreeTableNode getChild(int index)
	{
		return null;
	}
	
	@Override
	public TreeTableNode getParentNode()
	{
		return parentNode;
	}

	@Override
	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		String summaryData = getObject().getData(FutureStatusSchema.TAG_FUTURE_STATUS_SUMMARY);
		String statusData = getObject().getData(FutureStatusSchema.TAG_FUTURE_STATUS_RATING);
		TextAndIconChoiceItem textAndIconChoiceItem = new TextAndIconChoiceItem(summaryData, new FutureStatusIcon());		
		if (tag.equals(StatusQuestion.POOR) && StatusQuestion.POOR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(StatusQuestion.FAIR) && StatusQuestion.FAIR.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(StatusQuestion.GOOD) && StatusQuestion.GOOD.equals(statusData))
			return textAndIconChoiceItem;

		if (tag.equals(StatusQuestion.VERY_GOOD) && StatusQuestion.VERY_GOOD.equals(statusData))
			return textAndIconChoiceItem;
		
		return new EmptyChoiceItem();
	}

	@Override
	public void rebuild() throws Exception
	{
	}
	
	public static final String[] COLUMN_TAGS = {
		BaseObject.TAG_EMPTY,
		BaseObject.TAG_EMPTY,
		BaseObject.TAG_EMPTY,
		BaseObject.TAG_EMPTY,
		
		StatusQuestion.POOR,
		StatusQuestion.FAIR,
		StatusQuestion.GOOD,
	    StatusQuestion.VERY_GOOD,

	    BaseObject.TAG_EMPTY,
	    BaseObject.TAG_EMPTY,
	};
	
	private TreeTableNode parentNode;
	private FutureStatus futureStatus;
}
