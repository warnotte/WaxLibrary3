package io.github.warnotte.waxlib3.waxlib2.MiniDB;

import java.util.EventListener;


public interface DBChangedEventListener extends EventListener {
	public abstract void DBEventOccurred(DBChangedEvent evt);
}
