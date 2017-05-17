package org.ncibi.mqueue.task;

import org.hibernate.Session;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.Task;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;
import org.ncibi.mqueue.Message;
import org.ncibi.mqueue.MessageQueue;

public class PersistentTaskQueuer<T> implements TaskQueuer<T>
{
    private final MessageQueue queue;
    private final PersistenceSession persistence;

    public PersistentTaskQueuer(MessageQueue queue, PersistenceSession persistence)
    {
        this.queue = queue;
        this.persistence = persistence;
    }

    @Override
    public void queue(final Task task, final T args)
    {
        //addTaskAndArgsToDatabase(task, args);
        //addTaskToQueue(task);
    	 int count = 0;
	     boolean ok = false;
	     while (!ok && (count < 6)) {
		        try {
		        	count++;
		        	addTaskAndArgsToDatabase(task, args);
		        	addTaskToQueue(task);
		        	ok = true;
		        } catch (Throwable t){
		        	System.out.println("Error in add task and args to databse " + t.getMessage());
		        	t.printStackTrace();
		        	ok=false;
		        }
	     }
	     if (!ok) {
	    	 System.out.println("Failed to save args.");
	     }
     
    }

    private void addTaskAndArgsToDatabase(final Task task, final T args)
    {
        Sessions.withSession(persistence.session(), new SessionProcedure()
        {
            @Override
            public void apply(Session session)
            {
                session.saveOrUpdate(task);
                session.saveOrUpdate(args);
            }
        });
    }
    
    private void addTaskToQueue(Task task)
    {
        Message m = new Message(task.getUuid());
        queue.putMessage(m);
    }
}
