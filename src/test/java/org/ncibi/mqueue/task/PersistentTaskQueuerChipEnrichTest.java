package org.ncibi.mqueue.task;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Test;
import org.ncibi.commons.exception.PreConditionCheckFailedException;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.ws.ServiceArguments;
import org.ncibi.db.ws.Task;
import org.ncibi.db.ws.TaskType;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;
import org.ncibi.chipenrich.ChipEnrichInputArguments;
import org.ncibi.mqueue.MessageQueue;
import org.ncibi.mqueue.persistent.AnyMessagePersistentMessageQueue;

public class PersistentTaskQueuerChipEnrichTest
{
    private static final Task t = TaskUtil.newQueuedTask(TaskType.CHIPENRICH);

    private static final PersistenceSession persistence = new PersistenceUnit(
            EntityManagers.newEntityManagerFromProject("task"));

    private static final MessageQueue queue = new AnyMessagePersistentMessageQueue(persistence, "test");
    private static PersistentTaskQueuer2<ChipEnrichInputArguments> taskQueuer = new PersistentTaskQueuer2<ChipEnrichInputArguments>(
            queue, persistence);

    @Test
    public void testWithValidArguments()
    {
        taskQueuer.queue(t, new ChipEnrichInputArguments());
    }

    @Test(expected = PreConditionCheckFailedException.class)
    public void testNullTask()
    {
        taskQueuer.queue(null, new ChipEnrichInputArguments());
    }
    
    @Test(expected = PreConditionCheckFailedException.class)
    public void testNullArgs()
    {
        taskQueuer.queue(t, null);
    }

    @After
    public void cleanup()
    {
        Sessions.withSession(persistence.session(), new SessionProcedure()
        {
            @Override
            public void apply(Session session)
            {
                ServiceArguments a = (ServiceArguments) session.createQuery(
                        "from ws.ServiceArguments where uuid = '" + t.getUuid() + "'").uniqueResult();
                if (a == null)
                {
                    return;
                }
                session.delete(a);
                session.refresh(t);
                session.delete(t);
            }
        });
    }
}
