package io.github.warnotte.waxlib3.core.Identifiable;

import java.util.List;

public class ArrayListIDTools {

	public static long getNextFreeId(List<? extends Identifiable> list)
	{
		long MaxId = 0;
		for (int i = 0 ; i < list.size(); i++)
		{
			Identifiable lc = list.get(i);
			long idp = lc.getId();
			if (idp>=MaxId) MaxId=idp;
		}
		long sz = MaxId+1;
		return sz;
		
	}

	public static long getLowestId(List<? extends Identifiable> list)
	{
		long MaxId = 99999999;
		for (int i = 0 ; i < list.size(); i++)
		{
			Identifiable lc = list.get(i);
			long idp = lc.getId();
			if (idp<=MaxId) MaxId=idp;
		}
		return MaxId;
	}

	public static Identifiable getByIndex(List<? extends Identifiable> list, int index)
	{
		for (int i = 0 ; i < list.size(); i++)
		{
			Identifiable lc = list.get(i);
			long idp = lc.getId();
			if (idp==index) return lc;
		}
		return null;
	}
}
