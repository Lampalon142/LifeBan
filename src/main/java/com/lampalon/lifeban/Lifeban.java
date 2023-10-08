package com.lampalon.lifeban;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class Lifeban extends Plugin {
    private static ConfigManager configManager;
    private static Lifeban instance;
    @Override
    public void onEnable() {
        instance = this;
    }
    PluginManager pm = instance.getProxy().getPluginManager();
    public void registerCommands(){

    }
    public void registerEvents(){

    }
    public static Lifeban getInstance() {
        return instance;
    }
    public enum TimeUnit
    {
        SECOND(new String[] { "s", "sec", "secs", "second", "seconds" }, 1L),  MINUTE(new String[] { "m", "min", "mins", "minute", "minutes" }, 60L),  HOUR(new String[] { "h", "hs", "hour", "hours" }, 3600L),  DAY(new String[] { "d", "ds", "day", "days" }, 86400L);

        private String[] names;
        private long seconds;

        private TimeUnit(String[] names, long seconds)
        {
            this.names = names;
            this.seconds = seconds;
        }

        public long getSeconds()
        {
            return this.seconds;
        }

        public String[] getNames()
        {
            return this.names;
        }

        public static TimeUnit getByString(String str)
        {
            for (TimeUnit timeUnit : TimeUnit.values()) {
                for (String name : timeUnit.getNames()) {
                    if (name.equalsIgnoreCase(str)) {
                        return timeUnit;
                    }
                }
            }
            return null;
        }
    }
}
