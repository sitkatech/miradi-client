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
package org.miradi.objects;

import java.awt.Color;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class ValueOption extends BaseObject
{
	public ValueOption(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		color = Color.BLACK;
	}
	
	
	public ValueOption(ObjectManager objectManager, BaseId idToUse, String labelToUse, int numericToUse, Color colorToUse) throws Exception
	{
		this(objectManager, idToUse);
		setData(TAG_LABEL, labelToUse);
		numeric = numericToUse;
		color = colorToUse;
	}
	
	public ValueOption(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
		numeric = json.getInt(TAG_NUMERIC);
		color = new Color(json.getInt(TAG_COLOR));
	}
	
	
	public int getType()
	{	
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.VALUE_OPTION;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public int getNumericValue()
	{
		return numeric;
	}
	
	// TODO: This is a hack that allows us to override any colors that might
	// be in the ValueOption objects. Eventually we will completely replace 
	// ValueOptions and RatingCriteria with the new RatingQuestion classes
	public Color getColor()
	{
		switch(getNumericValue())
		{
			case -1: return INVALID_GRAY;
			case 0: return Color.WHITE;
			case 1: return ChoiceQuestion.COLOR_GREAT;
			case 2: return ChoiceQuestion.COLOR_OK;
			case 3: return ChoiceQuestion.COLOR_CAUTION;
			case 4: return ChoiceQuestion.COLOR_ALERT;
		}
		EAM.logDebug("ValueOption.getColor for unknown numeric value: " + getNumericValue());
		return Color.BLACK;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_NUMERIC.equals(fieldTag))
			numeric = Integer.parseInt(dataValue);
		else if(TAG_COLOR.equals(fieldTag))
			color = new Color(Integer.parseInt(dataValue));
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_NUMERIC.equals(fieldTag))
			return Integer.toString(getNumericValue());
		else if(TAG_COLOR.equals(fieldTag))
			return Integer.toString(getColor().getRGB());

		return super.getData(fieldTag);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_NUMERIC, numeric);
		json.put(TAG_COLOR, color.getRGB());
		
		return json;
	}
	
	final public static String TAG_NUMERIC = "Numeric";
	final public static String TAG_COLOR = "Color";
	final private static Color INVALID_GRAY = new Color(200,200,200);
	
	static final String OBJECT_NAME = "ValueOption";

	int numeric;
	Color color;
}
