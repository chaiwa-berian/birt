/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SelectionBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeLocator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.SquareHandle;

/**
 * Table selection handle
 */
public class TableSelectionHandle extends SquareHandle
{

	//GraphicalEditPart editpart;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker( )
	{
		return null;
	}

	/**
	 * Constructor
	 * @param owner
	 * @param rect
	 */
	public TableSelectionHandle( GraphicalEditPart owner, Rectangle rect )
	{
		super( owner, new TableRelativeLocator( owner.getFigure( ), -1 ) );

		setOpaque( false );

		setPreferredSize( rect.getSize( ) );
		setLocation( rect.getLocation( ) );
		setSize( rect.getSize( ) );
		
		setBorder( new SelectionBorder(2) );
	}

	public TableSelectionHandle( GraphicalEditPart first, GraphicalEditPart end )
	{
		setOpaque( false );
	}

	public void paintFigure( Graphics g )
	{

//		Rectangle r = getBounds( );
//
//		try
//		{
//			g.setLineWidth( 2 );
//			g.drawLine( r.x, r.y + 2, r.right( ) - 1, r.y + 2 );
//			g.drawLine( r.x, r.bottom( ) - 1, r.right( ) - 1, r.bottom( ) - 1 );
//			g.drawLine( r.x + 1, r.y + 2, r.x + 1, r.bottom( ) - 3 );
//			g.drawLine( r.right( ) - 1,
//					r.bottom( ) - 2,
//					r.right( ) - 1,
//					r.y + 2 );
//		}
//		finally
//		{
//
//		}
	}

	public boolean containsPoint( int x, int y )
	{
		if ( !super.containsPoint( x, y ) )
			return false;
		return !Rectangle.SINGLETON.setBounds( getBounds( ) )
				.shrink( -2, -2 )
				.contains( x, y );
	}

	/**
	 * Table relative locator
	 */
	public static class TableRelativeLocator extends RelativeLocator

	{

		public TableRelativeLocator( IFigure reference, int location )
		{
			super( reference, location );
		}

		public void relocate( IFigure target )
		{

		}
	}
}