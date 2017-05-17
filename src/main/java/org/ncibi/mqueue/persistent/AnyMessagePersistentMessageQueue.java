package org.ncibi.mqueue.persistent;

import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.Queue;

public class AnyMessagePersistentMessageQueue extends AbstractPersistentMessageQueue
{
    public AnyMessagePersistentMessageQueue(PersistenceSession persistence, String queueName)
    {
        super(persistence, queueName);
        System.out.println("AnyMessagePersistentMessageQueue" +queueName);
    }
    
    @Override
    protected String hqlForNextTask(Queue queue)
    {
    	System.out.println("AnyMessagePersistentMessageQueue"+queue.getId()+"\n;");
        String hql = "from ws.QMessage where queueid = " + queue.getId();
        return hql;
    }
}
