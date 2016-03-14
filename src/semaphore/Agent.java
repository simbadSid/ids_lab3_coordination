package semaphore;

import java.util.concurrent.Semaphore;





public class Agent implements Runnable
{
// -----------------------------------
// Attributes
// -----------------------------------
	private String				id;
	private int					nbrAction;
	private SynchQueue<String>	queue;
	private boolean				isProducer;
	private Semaphore			workingAgentSem;

// -----------------------------------
// Builder
// -----------------------------------
	public Agent(String id, int nbrAction, boolean isProducer, SynchQueue<String> queue, Semaphore workingAgentSem)
	{
		this.id					= new String(id);
		this.nbrAction			= nbrAction;
		this.isProducer			= isProducer;
		this.queue				= queue;
		this.workingAgentSem	= workingAgentSem;
	}

// -----------------------------------
// LocalMethode
// -----------------------------------
	@Override
	public void run()
	{
		if (this.isProducer)	this.runProducer();
		else					this.runConsumer();

		this.workingAgentSem.release();
	}

	public void runProducer()
	{
		for (int i=0; i<this.nbrAction; i++)
		{
			String message = "Producer [" + this.id + "] has producted " + i;
			this.queue.produce(message);
			System.out.println(message);
		}
	}

	public void runConsumer()
	{
		for (int i=0; i<this.nbrAction; i++)
		{
			String message = this.queue.consume();
			System.out.println("Consumer [" + this.id + "] has consumed \"" + message + "\"");
		}
	}
}
