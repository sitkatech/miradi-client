/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;

public class IconManager
{
	public static Image getImage(String objectTypeName)
	{
		if (objectTypeName.equals(Indicator.OBJECT_NAME))
			return getIndicatorIcon();
		
		if (objectTypeName.equals(Strategy.OBJECT_NAME))
			return getStrategyIcon();
		
		if (objectTypeName.equals(Measurement.OBJECT_NAME))
			return getMeasurementIcon();
		
		if (objectTypeName.equals(Goal.OBJECT_NAME))
			return getGoalIcon();
		
		if (objectTypeName.equals(Objective.OBJECT_NAME))
			return getObjectiveIcon();
		
		if (objectTypeName.equals(Target.OBJECT_NAME))
			return getTargetIcon();
		
		if (objectTypeName.equals(KeyEcologicalAttribute.OBJECT_NAME))
			return getKeyEcologicalAttributeIcon();
		
		if (objectTypeName.equals(Task.OBJECT_NAME))
			return getTaskIcon();
		
		if (objectTypeName.equals(Task.METHOD_NAME))
			return getMethodIcon();
		
		if (objectTypeName.equals(Task.ACTIVITY_NAME))
			return getActivityIcon();
		
		if (objectTypeName.equals(ConceptualModelDiagram.OBJECT_NAME))
			return getConceptualModelIcon();
		
		if (objectTypeName.equals(ResultsChainDiagram.OBJECT_NAME))
			return getConceptualModelIcon();

		
		return null;
	}
	
	public static Image getKeyEcologicalAttributeIcon()
	{
		return convertToImage(new KeyEcologicalAttributeIcon());
	}
	
	public static Image getMeasurementIcon()
	{
		return convertToImage(new MeasurementIcon());
	}
	
	public static Image getGoalIcon()
	{
		return convertToImage(new GoalIcon());
	}
	
	public static Image getObjectiveIcon()
	{
		return convertToImage(new ObjectiveIcon());
	}
	
	public static Image getIndicatorIcon()
	{
		return convertToImage(new IndicatorIcon());
	}
	
	public static Image getTargetIcon()
	{
		return convertToImage(new TargetIcon());
	}

	public static Image getStrategyIcon()
	{
		return convertToImage(new StrategyIcon());
	}
	
	public static Image getDraftStrategyIcon()
	{
		return convertToImage(new DraftStrategyIcon());
	}

	public static Image getTaskIcon()
	{
		return convertToImage(new TaskIcon());
	}

	public static Image getMethodIcon()
	{
		return convertToImage(new MethodIcon());
	}

	public static Image getActivityIcon()
	{
		return convertToImage(new ActivityIcon());
	}
	
	public static Image getConceptualModelIcon()
	{
		return convertToImage(new ConceptualModelIcon());
	}
	
	public static Image getResultsChainIcon()
	{
		return convertToImage(new ResultsChainIcon());
	}
	
	

	private static Image convertToImage(Icon icon)
	{
		final int PADDING = 5;
		BufferedImage img = new BufferedImage(icon.getIconWidth() + PADDING, icon.getIconHeight() + PADDING, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		icon.paintIcon(new JLabel(), g, 2, 2);
		
		return new ImageIcon(img).getImage();
	}
}
