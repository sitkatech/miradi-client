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
package org.miradi.questions;

import java.awt.Color;

import org.miradi.diagram.DiagramConstants;
import org.miradi.main.EAM;

public class DiagramFactorBackgroundQuestion extends StaticChoiceQuestion
{
	public DiagramFactorBackgroundQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Light Gray (Default)"), DiagramConstants.TEXT_BOX_COLOR),

			new ChoiceItem(TARGET_LIGHT_GREEN_COLOR_CODE, EAM.text("Target Light Green"), new Color(0xdaedda)),
			new ChoiceItem(HWB_TARGET_LIGHT_TAN_COLOR_CODE, EAM.text("HWB Target Light Tan"), new Color(0xd8cbc0)),
			new ChoiceItem(BIOPHYSICAL_FACTOR_LIGHT_OLIVE_COLOR_CODE, EAM.text("Biophysical Factor Light Olive"), new Color(0xceccb6)),
			new ChoiceItem(THREAT_LIGHT_PINK_COLOR_CODE, EAM.text("Threat Light Pink"), new Color(0xd8b2b2)),
			new ChoiceItem(CONTRIBUTING_FACTOR_LIGHT_ORANGE_COLOR_CODE, EAM.text("Contributing Factor Light Orange"), new Color(0xefd18e)),
			new ChoiceItem(STRATEGY_ACTIVITY_LIGHT_YELLOW_COLOR_CODE, EAM.text("Strategy/Activity Light Yellow"), new Color(0xfdf9ce)),
			new ChoiceItem(BIOPHYSICAL_RESULT_LIGHT_LAVENDER_COLOR_CODE, EAM.text("Biophysical Result Light Lavender"), new Color(0xbfbfe8)),
			new ChoiceItem(THREAT_REDUCTION_RESULT_LIGHT_PURPLE_COLOR_CODE, EAM.text("Threat Reduction Result Light Purple"), new Color(0xd0b4db)),
			new ChoiceItem(INTERMEDIATE_RESULT_LIGHT_BLUE_COLOR_CODE, EAM.text("Intermediate Result Light Blue"), new Color(0xb3dde0)),
			new ChoiceItem(OBJECTIVE_GOAL_BLUE_COLOR_CODE, EAM.text("Objective/Goal Blue"), new Color(0xcae8ea)),
			new ChoiceItem(INDICATOR_PURPLE_COLOR_CODE, EAM.text("Indicator Purple"), new Color(0xaa6eae)),

			new ChoiceItem(WHITE_COLOR_CODE, EAM.text("White"), new Color(255, 255, 255)),
			new ChoiceItem(PINK_COLOR_CODE, EAM.text("Pink"), new Color(255, 0, 255)),
			new ChoiceItem(ORANGE_COLOR_CODE, EAM.text("Light Orange"), new Color(255, 128, 64)),
			new ChoiceItem(LIGHT_YELLOW_COLOR_CODE, EAM.text("Light Yellow"), new Color(255, 255, 205)),
			new ChoiceItem(LIGHT_GREEN_COLOR_CODE, EAM.text("Light Green"), new Color(95, 251, 23)),
			new ChoiceItem(LIGHT_BLUE_COLOR_CODE, EAM.text("Light Blue"), new Color(0, 204, 255)),
			new ChoiceItem(LIGHT_PURPLE_COLOR_CODE, EAM.text("Light Purple"), new Color(204, 153, 255)),
			new ChoiceItem(TAN_COLOR_CODE, EAM.text("Tan"), new Color(237, 226, 117)),
			new ChoiceItem(BLACK_COLOR_CODE, EAM.text("Black"), new Color(0, 0, 0)),
			new ChoiceItem(DARK_GRAY_COLOR_CODE, EAM.text("Dark Gray"), new Color(78, 72, 72)),

			new ChoiceItem(RED_COLOR_CODE, EAM.text("Dark Red"), new Color(255, 0, 0)),
			new ChoiceItem(DARK_ORANGE_COLOR_CODE, EAM.text("Dark Orange"), new Color(255, 102, 0)),
			new ChoiceItem(DARK_YELLOW_COLOR_CODE, EAM.text("Dark Yellow"), new Color(255, 204, 0)),
			new ChoiceItem(DARK_GREEN_COLOR_CODE, EAM.text("Dark Green"), new Color(0, 128, 0)),
			new ChoiceItem(DARK_BLUE_COLOR_CODE, EAM.text("Dark Blue"), new Color(0, 0, 204)),
			new ChoiceItem(DARK_PURPLE_COLOR_CODE, EAM.text("Dark Purple"), new Color(153, 0, 255)),
			new ChoiceItem(BROWN_COLOR_CODE, EAM.text("Brown"), new Color(200, 90, 23)),
		};
	}
	
	@Override
	public boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	public String getReadableAlternativeDefaultCode()
	{
		return "LightGray";
	}

	public static final String TARGET_LIGHT_GREEN_COLOR_CODE = "TargetLightGreen";
	public static final String HWB_TARGET_LIGHT_TAN_COLOR_CODE = "HWBTargetLightTan";
	public static final String BIOPHYSICAL_FACTOR_LIGHT_OLIVE_COLOR_CODE = "BiophysicalFactorLightOlive";
	public static final String THREAT_LIGHT_PINK_COLOR_CODE = "ThreatLightPink";
	public static final String CONTRIBUTING_FACTOR_LIGHT_ORANGE_COLOR_CODE = "ContributingFactorLightOrange";
	public static final String STRATEGY_ACTIVITY_LIGHT_YELLOW_COLOR_CODE = "StrategyActivityLightYellow";
	public static final String BIOPHYSICAL_RESULT_LIGHT_LAVENDER_COLOR_CODE = "BiophysicalResultLightLavender";
	public static final String THREAT_REDUCTION_RESULT_LIGHT_PURPLE_COLOR_CODE = "ThreatReductionResultLightPurple";
	public static final String INTERMEDIATE_RESULT_LIGHT_BLUE_COLOR_CODE = "IntermediateResultLightBlue";
	public static final String OBJECTIVE_GOAL_BLUE_COLOR_CODE = "ObjectiveGoalBlue";
	public static final String INDICATOR_PURPLE_COLOR_CODE = "IndicatorPurple";

	public static final String WHITE_COLOR_CODE = "White";
	public static final String PINK_COLOR_CODE = "Pink";
	public static final String ORANGE_COLOR_CODE = "Orange";
	public static final String LIGHT_YELLOW_COLOR_CODE = "LightYellow";
	public static final String LIGHT_GREEN_COLOR_CODE = "LightGreen";
	public static final String LIGHT_BLUE_COLOR_CODE = "LightBlue";
	public static final String LIGHT_PURPLE_COLOR_CODE = "LightPurple";
	public static final String TAN_COLOR_CODE = "Tan";
	public static final String BLACK_COLOR_CODE = "Black";
	public static final String DARK_GRAY_COLOR_CODE = "DarkGray";

	public static final String RED_COLOR_CODE = "Red";
	public static final String DARK_ORANGE_COLOR_CODE = "DarkOrange";
	public static final String DARK_YELLOW_COLOR_CODE = "DarkYellow";
	public static final String DARK_GREEN_COLOR_CODE = "DarkGreen";
	public static final String DARK_BLUE_COLOR_CODE = "DarkBlue";
	public static final String DARK_PURPLE_COLOR_CODE = "DarkPurple";
	public static final String BROWN_COLOR_CODE = "Brown";
}
