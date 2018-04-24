/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.utils.ColorManager;

public class ValueOption extends BaseObject
{
	public ValueOption(ObjectManager objectManager, BaseId idToUse) throws Exception
	{
		super(objectManager, idToUse, createSchema(objectManager));
		
		setData(TAG_COLOR, Integer.toString(Color.BLACK.getRGB()));
	}

	public static ValueOptionSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ValueOptionSchema createSchema(ObjectManager objectManager)
	{
		return (ValueOptionSchema) objectManager.getSchemas().get(ObjectType.VALUE_OPTION);
	}

	public ValueOption(ObjectManager objectManager, BaseId idToUse, String labelToUse, int numericToUse, Color colorToUse) throws Exception
	{
		this(objectManager, idToUse);

		setData(TAG_LABEL, labelToUse);
		setData(TAG_NUMERIC, Integer.toString(numericToUse));
		setData(TAG_COLOR, Integer.toString(colorToUse.getRGB()));
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public int getNumericValue()
	{
		return getIntegerData(TAG_NUMERIC);
	}
	
	// TODO: This is a hack that allows us to override any colors that might
	// be in the ValueOption objects. Eventually we will completely replace 
	// ValueOptions and RatingCriteria with the new RatingQuestion classes
	public Color getColor()
	{
		AppPreferences appPreferences = getAppPreferences();

		switch(getNumericValue())
		{
			case -1: return INVALID_GRAY;
			case 0: return Color.WHITE;
			case 1: if (appPreferences != null) return appPreferences.getColor(AppPreferences.TAG_COLOR_GREAT); else return ColorManager.LEGACY_DARK_GREEN;
			case 2: if (appPreferences != null) return appPreferences.getColor(AppPreferences.TAG_COLOR_OK); else return ColorManager.LEGACY_LIGHT_GREEN;
			case 3: if (appPreferences != null) return appPreferences.getColor(AppPreferences.TAG_COLOR_CAUTION); else return ColorManager.LEGACY_DARK_YELLOW;
			case 4: if (appPreferences != null) return appPreferences.getColor(AppPreferences.TAG_COLOR_ALERT); else return ColorManager.LEGACY_RED;
		}
		EAM.logDebug("ValueOption.getColor for unknown numeric value: " + getNumericValue());
		return Color.BLACK;
	}

	private AppPreferences getAppPreferences()
	{
		MainWindow mainWindow = EAM.getMainWindow();
		if (mainWindow != null)
			return mainWindow.getAppPreferences();

		return null;
	}

	@Override
	public String toString()
	{
		return getLabel();
	}

	final public static String TAG_NUMERIC = "Numeric";
	final public static String TAG_COLOR = "Color";
	final private static Color INVALID_GRAY = new Color(200,200,200);
}
