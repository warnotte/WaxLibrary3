package io.github.warnotte.waxlib3.core.MiniDB;

import java.awt.AWTEvent;

public class DBChangedEvent extends AWTEvent 
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4201905891811106951L;

	public DBChangedEvent(Object target, int id)
	{
		super(target, id);
	}

}
