package com.z_PORTFOLIO;

import java.lang.management.*;					
import java.util.concurrent.TimeUnit;

/////////////////////////////////////////////////////////////////////////////////////
//
//  This class is responsible for all time operations. It can measure:
//		CPU time    ==> App time + System Time.
// 		App time    ==> Time spent running the application's own code.
//		System Time ==> time spent running OS code on behalf of the application.
//						(such as for I/O).
//
//  Due to possible lack of precision, all measured times are
//  converted from nanoseconds to milliseconds.
//
///////////////////////////////////////////////////////////////////////////////////

public class PortTime{					
	private static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
												
	public PortTime(){
		bean = ManagementFactory.getThreadMXBean( );
	}
	
	public static long getCpuTime( ){
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        nanoToMilli(bean.getCurrentThreadCpuTime( )) : 0L;
	}

	public static long getPEBSTime( ){
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        nanoToMilli(bean.getCurrentThreadUserTime( )) : 0L;
	}

	public static long getSystemTime( ) {
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        nanoToMilli((bean.getCurrentThreadCpuTime( ) - bean.getCurrentThreadUserTime( ))) : 0L;
	}
	
	private static long nanoToMilli(long time){
		return TimeUnit.NANOSECONDS.toMillis(time);
	}
}