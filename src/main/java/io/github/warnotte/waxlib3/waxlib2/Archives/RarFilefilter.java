/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.github.warnotte.waxlib3.waxlib2.Archives;

import java.io.File;
import java.io.FilenameFilter;

public class RarFilefilter  implements FilenameFilter {
	public boolean accept(File dir, String name) {
        return (name.endsWith(".rar"));
    }
}
