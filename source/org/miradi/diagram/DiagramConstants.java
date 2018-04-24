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
package org.miradi.diagram;

import java.awt.Color;


public class DiagramConstants
{
	public static final int BORDER_SELECTED_STROKE_WIDTH = 3;
	public static final Color BORDER_COLOR = Color.LIGHT_GRAY;
	public static final Color BORDER_SELECTED_COLOR = Color.BLACK;

	public static final Color GROUP_BOX_COLOR = new Color(175, 175, 175);
	public static final Color TEXT_BOX_COLOR = new Color(230, 230, 230);
	public static final Color COLOR_STRESS = new Color(150, 150, 255);
	public static final Color COLOR_DRAFT_STRATEGY = new Color(255, 255, 190);
	public static final Color COLOR_FACTOR_CLUSTER = Color.LIGHT_GRAY;

	// legacy default colors
	public static final Color LEGACY_DEFAULT_TARGET_COLOR = new Color(200, 255, 200);
	public static final Color LEGACY_DEFAULT_BIODIVERSITY_TARGET_SCOPE_COLOR = new Color(128, 255, 128);
	public static final Color LEGACY_DEFAULT_HUMAN_WELFARE_TARGET_COLOR = new Color(180, 150, 110);
	public static final Color LEGACY_DEFAULT_HUMAN_WELFARE_SCOPE_COLOR = new Color(150, 120, 70);
	public static final Color LEGACY_DEFAULT_DIRECT_THREAT_COLOR = new Color(255, 150, 150);
	public static final Color LEGACY_DEFAULT_BIOPHYSICAL_FACTOR_COLOR = new Color(195, 190, 131);
	public static final Color LEGACY_DEFAULT_BIOPHYSICAL_RESULT_COLOR = new Color(204, 204, 255);
	public static final Color LEGACY_DEFAULT_CONTRIBUTING_FACTOR_COLOR = new Color(255, 190, 0);
	public static final Color LEGACY_DEFAULT_STRATEGY_COLOR = new Color(255, 255, 0);
	public static final Color LEGACY_DEFAULT_INTERMEDIATE_RESULT_COLOR = new Color(150, 240, 255);
	public static final Color LEGACY_DEFAULT_THREAT_REDUCTION_RESULT_COLOR = new Color(240, 150, 255);
	public static final Color LEGACY_DEFAULT_INDICATOR_COLOR = new Color(204,153,255);
	public static final Color LEGACY_DEFAULT_OBJECTIVE_COLOR = new Color(204,238,255);

	// new default colors as of Miradi 4.5
	public static final Color DEFAULT_TARGET_COLOR = new Color(0xbae5ba);
	public static final Color DEFAULT_BIODIVERSITY_TARGET_SCOPE_COLOR = new Color(0x8adb8a);
	public static final Color DEFAULT_HUMAN_WELFARE_TARGET_COLOR = new Color(0xccbaa6);
	public static final Color DEFAULT_HUMAN_WELFARE_SCOPE_COLOR = new Color(0xb29872);
	public static final Color DEFAULT_DIRECT_THREAT_COLOR = new Color(0xdb9595);
	public static final Color DEFAULT_BIOPHYSICAL_FACTOR_COLOR = new Color(0xc3be83);
	public static final Color DEFAULT_BIOPHYSICAL_RESULT_COLOR = new Color(0xadabed);
	public static final Color DEFAULT_CONTRIBUTING_FACTOR_COLOR = new Color(0xf4bd37);
	public static final Color DEFAULT_STRATEGY_COLOR = new Color(0xfbee69);
	public static final Color DEFAULT_INTERMEDIATE_RESULT_COLOR = new Color(0x75cfdb);
	public static final Color DEFAULT_THREAT_REDUCTION_RESULT_COLOR = new Color(0xc48ed6);
	public static final Color DEFAULT_ACTIVITIES_COLOR = new Color(0xfbee69);
	public static final Color DEFAULT_MONITORING_ACTIVITIES_COLOR = new Color(204,153,255);
	public static final Color DEFAULT_INDICATOR_COLOR = new Color(0xc48ed6);
	public static final Color DEFAULT_OBJECTIVE_COLOR = new Color(0xcae8ea);

	public static final Color[] targetColorChoices = {
		DEFAULT_TARGET_COLOR,
		new Color(153, 255, 153),
		LEGACY_DEFAULT_TARGET_COLOR,
		new Color(80, 255, 80),
		new Color(64, 220, 64)
	};
	public static final Color[] biodiversityTargetScopeColorChoices = {
		DEFAULT_BIODIVERSITY_TARGET_SCOPE_COLOR,
		new Color(0, 255, 0),
		LEGACY_DEFAULT_BIODIVERSITY_TARGET_SCOPE_COLOR,
		new Color(0, 220, 0),
		new Color(0, 180, 0),
		new Color(0, 128, 0)
	};
	public static final Color[] humanWelfareTargetColorChoices = {
		DEFAULT_HUMAN_WELFARE_TARGET_COLOR,
		new Color(210, 180, 150),
		LEGACY_DEFAULT_HUMAN_WELFARE_TARGET_COLOR,
		new Color(150, 120, 70),
		new Color(210, 150, 70)
	};
	public static final Color[] humanWelfareScopeColorChoices = {
		DEFAULT_HUMAN_WELFARE_SCOPE_COLOR,
		new Color(180, 150, 110),
		LEGACY_DEFAULT_HUMAN_WELFARE_SCOPE_COLOR,
		new Color(210, 130, 20),
		new Color(210, 180, 140),
	};
	public static final Color[] directThreatColorChoices = {
		DEFAULT_DIRECT_THREAT_COLOR,
		LEGACY_DEFAULT_DIRECT_THREAT_COLOR,
		new Color(255, 128, 128),
		new Color(220, 150, 150),
		new Color(255, 200, 200)
	};
	public static final Color[] biophysicalFactorColorChoices = {
		DEFAULT_BIOPHYSICAL_FACTOR_COLOR,
		LEGACY_DEFAULT_BIOPHYSICAL_FACTOR_COLOR,
		new Color(180, 174, 100),
		new Color(162, 156, 78)
	};
	public static final Color[] biophysicalResultColorChoices = {
		DEFAULT_BIOPHYSICAL_RESULT_COLOR,
		LEGACY_DEFAULT_BIOPHYSICAL_RESULT_COLOR,
		new Color(159, 159, 255),
		new Color(119, 119, 253)
	};
	public static final Color[] contributingFactorColorChoices = {
		DEFAULT_CONTRIBUTING_FACTOR_COLOR,
		LEGACY_DEFAULT_CONTRIBUTING_FACTOR_COLOR,
		new Color(255, 128, 0),
		new Color(255, 220, 0),
		new Color(255, 190, 64),
		new Color(255, 240, 200)
	};
	public static final Color[] strategyColorChoices = {
		DEFAULT_STRATEGY_COLOR,
		LEGACY_DEFAULT_STRATEGY_COLOR,
		new Color(240, 240, 0), 
		new Color(255, 255, 128)
	};
	public static final Color[] intermediateResultChoices = {
		DEFAULT_INTERMEDIATE_RESULT_COLOR,
		new Color(100, 222, 255),
		new Color(80, 200, 220), 
		new Color(80, 200, 255), 
		new Color(200, 240, 255), 
		LEGACY_DEFAULT_INTERMEDIATE_RESULT_COLOR
    };
	public static final Color[] threatReductionResultChoices = {
		DEFAULT_THREAT_REDUCTION_RESULT_COLOR,
		new Color(222, 100, 255), 
		new Color(200, 80, 220), 
		new Color(200, 80, 255), 
		new Color(240, 200, 255), 
		LEGACY_DEFAULT_THREAT_REDUCTION_RESULT_COLOR
    };
}
