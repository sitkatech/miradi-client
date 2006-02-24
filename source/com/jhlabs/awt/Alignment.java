/*
 * This code was downloaded from: http://www.jhlabs.com/java/layout/index.html
 * on 2006-02-06. According to that page, here is the license:
 * 
 * 
 * License

 The downloadable source code on this page is released under the Apache License. 
 Basically, this means that you are free to do whatever you like with this code, 
 including commercial use, but it's not my fault if your satellite/nuclear 
 power station/missile system fails as a result.

 Licensed under the Apache License, Version 2.0 (the "License"); 
 you may not use this code except in compliance with the License. 
 You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 Unless required by applicable law or agreed to in writing, software distributed under 
 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 OF ANY KIND, either express or implied. See the License for the specific language 
 governing permissions and limitations under the License.
 */

package com.jhlabs.awt;

import java.awt.Rectangle;

public class Alignment implements Direction {

	public final static int FILL_NONE = 0;
	public final static int FILL_HORIZONTAL = 1;
	public final static int FILL_VERTICAL = 2;
	public final static int FILL_BOTH = 3;

	public static void alignInCell(Rectangle r, Rectangle cell, int alignment, int fill) {
		r.x = cell.x;
		r.y = cell.y;

		/* Horizontal fill */
		switch (fill) {
		  case FILL_BOTH:
		  case FILL_HORIZONTAL:
			r.width = cell.width;
			break;
		}

		/* Vertical fill */
		switch (fill) {
		  case FILL_BOTH:
		  case FILL_VERTICAL:
			r.height = cell.height;
			break;
		}

		/* Horizontal alignment */
		switch (alignment) {
		  case CENTER:
		  case NORTH:
		  case SOUTH:
			r.x += (cell.width - r.width)/2;
			break;
		  case WEST:
		  case NORTHWEST:
		  case SOUTHWEST:
			break;
		  case EAST:
		  case NORTHEAST:
		  case SOUTHEAST:
			r.x += cell.width - r.width;
			break;
		}

		/* Vertical alignment */
		switch (alignment) {
		  case CENTER:
		  case WEST:
		  case EAST:
			r.y += (cell.height - r.height)/2;
			break;
		  case NORTH:
		  case NORTHWEST:
		  case NORTHEAST:
			break;
		  case SOUTH:
		  case SOUTHWEST:
		  case SOUTHEAST:
			r.y += cell.height - r.height;
			break;
		}

	}

}
