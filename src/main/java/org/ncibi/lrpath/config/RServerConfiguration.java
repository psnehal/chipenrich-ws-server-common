package org.ncibi.lrpath.config;

import org.apache.commons.lang.math.NumberUtils;
import org.ncibi.commons.config.ProjectConfiguration;

public class RServerConfiguration
{
    public static int rserverPort()
    {
        return getDefaultedIntegerProperty("rserver.port", 6311);
    }
    
    public static String rserverAddress()
    {
        return ProjectConfiguration.getDefaultedProjectProperty("rserver.address", "127.0.0.1");
    }
    
    private static int getDefaultedIntegerProperty(String property, int defaultValue)
    {
        return NumberUtils.toInt(ProjectConfiguration.getProjectProperty(property), defaultValue);
    }
}
