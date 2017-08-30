package com.babel.basedata;

import java.util.Properties;

import org.springframework.core.task.TaskExecutor;

import com.babel.common.core.util.TaskExecutorUtils;

public class TestTaskExecutorUtils {
	public static void main(String[] args) throws Exception {
		Properties poolProperties=TaskExecutorUtils.getPoolInfo(1, 10, 90);
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance("lawHelp", TestTaskExecutorUtils.class, null, poolProperties);
		
		for(int i=0; i<1000; i++){
			final int t=i;
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(1000l);
						System.out.println("-----i="+t+" threadName="+Thread.currentThread().getName()+" threadId="+Thread.currentThread().getId());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
		}
		Thread.sleep(3000l);
		System.out.println(TaskExecutorUtils.getTaskExecutors().keySet());
	}
}
