/**
 *
 * Copyright 2014 Cisco. All rights reserved.
 * UILog.java
 *
 */
package com.lskycity.support.utils;


import android.util.Log;



/**
 * for debug, this can print a method call stack
 * 
 *@author liuzhaofeng
 *@since Sep 12, 2014
 */
public class StackLog
{
    private static Throwable th = new Throwable();

    public static void print(String currentTagForLog)
    {
        th.fillInStackTrace();
        StackTraceElement[] stacks = th.getStackTrace();
        Log.v(StackLog.class.getSimpleName(), currentTagForLog + "---------->");
        String currentEntry = currentTagForLog + " -- ";
        for(StackTraceElement stack : stacks)
        {
            Log.v(StackLog.class.getSimpleName(), currentEntry + stack.toString());
        }
        Log.v(StackLog.class.getSimpleName(), currentTagForLog + "<----------");
    }
}
