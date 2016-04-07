package semaphore;

import java.util.LinkedList;






public class SynchQueue <E>
{
// -----------------------------------
// Attributes
// -----------------------------------
	private SemaphoreMozart2	semaphoreProducer;
	private SemaphoreMozart2	semaphoreConsumer;
	private LinkedList<E>		queue;

// -----------------------------------
// Builder
// -----------------------------------
	public SynchQueue()
	{
		try
		{
			this.semaphoreProducer	= new SemaphoreMozart2(1);
			this.semaphoreConsumer	= new SemaphoreMozart2(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.queue = new LinkedList<E>();
	}

// -----------------------------------
// Local methods
// -----------------------------------
	public void produce(E elem)
	{
		try
		{
			this.semaphoreProducer.acquire();
			this.queue.addLast(elem);
			this.semaphoreConsumer.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	public E consume()
	{
		E res = null;

		try
		{
			this.semaphoreConsumer.acquire();
			res = this.queue.poll();
			this.semaphoreProducer.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		return res;
	}
}
