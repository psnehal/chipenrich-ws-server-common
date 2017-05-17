package org.ncibi.db.ws;

public enum TaskType
{
    CHIPENRICH, LRPATH,CHIPENRICH_JAVAX, LRPATH_CONCEPTGEN, LRPATH_JAVAX, SEGMENT_SENTENCES, STOP, MXTERMINATOR, STANFORD_PARSER, GSEA_THINK, LRPATH_THINK;

    public static TaskType toTaskType(String taskType)
    {
        for (TaskType t : TaskType.values())
        {
            if (t.toString().equalsIgnoreCase(taskType))
            {
                return t;
            }
        }

        return null;
    }
}
