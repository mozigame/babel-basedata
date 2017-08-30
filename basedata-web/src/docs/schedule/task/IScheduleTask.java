package com.babel.fwork.schedule.task;

import java.util.Collection;

public interface IScheduleTask<T> {
	String getTaskType();
	Collection<T> loadData();
	Object execute(T obj);
}
