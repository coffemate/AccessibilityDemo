package com.coffee.accessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by coffee on 2017/5/6.
 */

public class MyAccessibility extends AccessibilityService {
    private static final String TAG = "MyAccessibility";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.d(TAG, "onAccessibilityEvent");
        String eventText="";
        Log.i(TAG,"==========start===========");
        switch (eventType){
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
//                getRedPacket();
//                openReadPacket();
                Log.d(TAG,"event.getClassName()" + event.getClassName());
                Log.d(TAG, "event.getPackageName()" + event.getPackageName());
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
                boolean redPacket = isRedPacket(event);
                redPacket = false;
                if(redPacket) {
                    startNotification(event);
                } else {
                    Log.d(TAG, "not red packet");
                }
                break;
/*            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;*/
            default:
//                Log.d(TAG,"unresolve eventType=" + eventType);
                break;
        }

        Log.d(TAG, eventText);
        Log.d(TAG,"====================END==================");

    }

    private boolean isRedPacket(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();

        for(int i=0;i<texts.size();i++) {
            Log.d(TAG,texts.get(i).toString());
            if(texts.get(i).toString().contains("coffee")) {
                return true;
            }
        }
        return false;
    }

    private void startNotification(AccessibilityEvent event) {
        Notification notification = (Notification) event.getParcelableData();
        try {
            notification.contentIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void getRedPacket() {
        Log.d(TAG,"getRedPacket");
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> wxList = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        for(int i=0;i<wxList.size();i++) {

            Log.d(TAG,"getRedPacket current nodeinfo=" + wxList.get(i).getClassName().toString());
            Log.d(TAG,"getRedPacket get parent name=" + wxList.get(i).getPackageName().toString());
            wxList.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void openReadPacket() {
        Log.d(TAG,"openReadPacket");
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> wxList = nodeInfo.findAccessibilityNodeInfosByText("開");
        boolean isOpenPacketPage = false;
        for(int t=0;t<nodeInfo.getChildCount();t++) {
            Log.d(TAG,"current child=" + nodeInfo.getChild(t).getClassName().toString());
            if(nodeInfo.getChild(t).getClassName().toString().equals("android.widget.TextView")) {
                if(nodeInfo.getChild(t).getText() != null){
                    Log.d(TAG,nodeInfo.getChild(t).getText().toString());
                    if(nodeInfo.getChild(t).getText().toString().equals("给你发了一个红包")) {
                        isOpenPacketPage = true;
                    }
                }
            }
            if(isOpenPacketPage && nodeInfo.getChild(t).getClassName().toString().equals("android.widget.Button")) {
                nodeInfo.getChild(t).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

/*        for(int i=0;i<wxList.size();i++) {

            Log.d(TAG,"openReadPacket current nodeinfo=" + wxList.get(i).getClassName().toString());
            Log.d(TAG,"openReadPacket get parent name=" + wxList.get(i).getPackageName().toString());
//            wxList.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }*/
    }
    @Override
    public void onInterrupt() {

    }
}
