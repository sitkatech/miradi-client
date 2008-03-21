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
package org.miradi.icons;

import java.awt.Image;

//FIXME the following code has been commented out Due to lack of time and icons being exported incorrectly (pickish color) 
public class IconManager
{
	public static Image getImage(String objectTypeName)
	{
//		if (objectTypeName.equals(Indicator.OBJECT_NAME))
//			return getIndicatorIcon();
//		
//		if (objectTypeName.equals(Strategy.OBJECT_NAME))
//			return getStrategyIcon();
//		
//		if (objectTypeName.equals(Measurement.OBJECT_NAME))
//			return getMeasurementIcon();
//		
//		if (objectTypeName.equals(Goal.OBJECT_NAME))
//			return getGoalIcon();
//		
//		if (objectTypeName.equals(Objective.OBJECT_NAME))
//			return getObjectiveIcon();
//		
//		if (objectTypeName.equals(Target.OBJECT_NAME))
//			return getTargetIcon();
//		
//		if (objectTypeName.equals(KeyEcologicalAttribute.OBJECT_NAME))
//			return getKeyEcologicalAttributeIcon();
//		
//		if (objectTypeName.equals(Task.OBJECT_NAME))
//			return getTaskIcon();
//		
//		if (objectTypeName.equals(Task.METHOD_NAME))
//			return getMethodIcon();
//		
//		if (objectTypeName.equals(Task.ACTIVITY_NAME))
//			return getActivityIcon();
//		
//		if (objectTypeName.equals(ConceptualModelDiagram.OBJECT_NAME))
//			return getConceptualModelIcon();
//		
//		if (objectTypeName.equals(ResultsChainDiagram.OBJECT_NAME))
//			return getConceptualModelIcon();
//
		
		return null;
	}
	
	public static Image getKeyEcologicalAttributeIcon()
	{
		return null;
		//return convertToImage(new KeyEcologicalAttributeIcon());
	}
	
	public static Image getMeasurementIcon()
	{
		return null;
		//return convertToImage(new MeasurementIcon());
	}
	
	public static Image getGoalIcon()
	{
		return null;
		//return convertToImage(new GoalIcon());
	}
	
	public static Image getObjectiveIcon()
	{
		return null;
		//return convertToImage(new ObjectiveIcon());
	}
	
	public static Image getIndicatorIcon()
	{
		return null;
		//return convertToImage(new IndicatorIcon());
	}
	
	public static Image getTargetIcon()
	{
		return null;
		//return convertToImage(new TargetIcon());
	}

	public static Image getStrategyIcon()
	{
		return null;
		//return convertToImage(new StrategyIcon());
	}
	
	public static Image getDraftStrategyIcon()
	{
		return null;
		//return convertToImage(new DraftStrategyIcon());
	}

	public static Image getTaskIcon()
	{
		return null;
		//return convertToImage(new TaskIcon());
	}

	public static Image getMethodIcon()
	{
		return null;
		//return convertToImage(new MethodIcon());
	}

	public static Image getActivityIcon()
	{
		return null;
		//return convertToImage(new ActivityIcon());
	}
	
	public static Image getConceptualModelIcon()
	{
		return null;
//		return convertToImage(new ConceptualModelIcon());
	}
	
	public static Image getResultsChainIcon()
	{
		return null;
		//return convertToImage(new ResultsChainIcon());
	}
	
	

//	private static Image convertToImage(Icon icon)
//	{
//		final int PADDING = 5;
//		BufferedImage img = new BufferedImage(icon.getIconWidth() + PADDING, icon.getIconHeight() + PADDING, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = img.createGraphics();
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		icon.paintIcon(new JLabel(), g, 2, 2);
//		
//		return new ImageIcon(img).getImage();
//	}
}
