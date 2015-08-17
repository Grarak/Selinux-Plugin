package com.kerneladiutor.selinux_plugin;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kerneladiutor.library.Item;
import com.kerneladiutor.library.PluginManager;
import com.kerneladiutor.library.Tab;
import com.kerneladiutor.library.items.Popup;
import com.kerneladiutor.library.items.Simple;
import com.kerneladiutor.library.root.RootUtils;

import java.util.Arrays;

/**
 * Created by willi on 11.08.15.
 */
public class KAReceiver extends BroadcastReceiver {

    private enum SELINUX {
        NOSELINUX, DISABLED, PERMISSIVE, ENFORCING
    }

    private static final String RECEIVE_EVENT = "com.kerneladiutor.selinux_plugin.action.RECEIVE_EVENT";

    @Override
    public void onReceive(final Context context, Intent intent) {

        final Tab tab = new Tab(context);
        tab.setTitle(context.getString(R.string.tab_name));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(RECEIVE_EVENT), 0);

        SELINUX selinuxState = getSelinuxState();
        switch (selinuxState) {
            case PERMISSIVE:
            case ENFORCING:
                Popup mSelinuxChanger = new Popup(Arrays.asList(context.getResources()
                        .getStringArray(R.array.selinux_items)));
                mSelinuxChanger.setTitle(context.getString(R.string.selinux));
                mSelinuxChanger.setDescription(context.getString(R.string.selinux_summary));
                mSelinuxChanger.setItem(selinuxState == SELINUX.PERMISSIVE ? 0 : 1);
                mSelinuxChanger.setOnItemSelectedPendingListener(pendingIntent, "selinuxchanger");

                tab.addItem(mSelinuxChanger);
                break;
            case NOSELINUX:
                Simple mNoSelinux = new Simple();
                mNoSelinux.setDescription(context.getString(R.string.no_selinux));

                tab.addItem(mNoSelinux);
                break;
            case DISABLED:
                Simple mDisabledSelinux = new Simple();
                mDisabledSelinux.setDescription(context.getString(R.string.disabled_selinux));

                tab.addItem(mDisabledSelinux);
                break;
        }

        PluginManager.publishTab(tab);
    }

    private SELINUX getSelinuxState() {
        switch (RootUtils.runCommand("getenforce").toLowerCase()) {
            case "disabled":
                return SELINUX.DISABLED;
            case "permissive":
                return SELINUX.PERMISSIVE;
            case "enforcing":
                return SELINUX.ENFORCING;
            default:
                return SELINUX.NOSELINUX;
        }
    }

    public static class EventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String tag = Item.getTagEvent(intent);
            switch (tag) {
                case "selinuxchanger":
                    int position = Popup.getPositionEvent(intent);
                    PluginManager.executeCommand("setenforce " + position, Item.getTabEvent(intent), tag, context);
                    break;
            }
        }

    }

}
