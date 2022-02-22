package io.github.warnotte.waxlib3.core.MiniDB;

import java.util.EventListener;


public interface DBChangedEventListener extends EventListener {
	public abstract void DBEventOccurred(DBChangedEvent evt);
}
