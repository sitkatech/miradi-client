/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.icons;

import org.miradi.diagram.DiagramConstants;
import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.RectangleRenderer;

import javax.swing.*;
import java.awt.*;

public class AssumptionIcon extends AbstractShapeIcon
{
    @Override
    FactorRenderer getRenderer()
    {
        return new RectangleRenderer();
    }

    @Override
    Color getIconColor()
    {
        return DiagramConstants.DEFAULT_ASSUMPTION_COLOR;
    }

    static public Icon createDisabledIcon()
    {
        return new AssumptionDisabledIcon();
    }

    private static final class AssumptionDisabledIcon extends AssumptionIcon
    {
        @Override
        Color getIconColor()
        {
            return Color.LIGHT_GRAY;
        }
    }
}