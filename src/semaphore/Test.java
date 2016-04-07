package semaphore;

import java.net.URISyntaxException;

import org.mozartspaces.core.MzsCoreException;







public class Test
{
	public static void main(String[] args) throws InterruptedException, MzsCoreException, URISyntaxException
	{
		int nbrProducer=-1, nbrConsumer = -1, nbrAction = -1;

		try
		{
			nbrProducer			= Integer.parseInt(args[0]);
			nbrConsumer			= Integer.parseInt(args[1]);
			nbrAction			= Integer.parseInt(args[2]);
			
		}
		catch(Exception e)
		{
			System.out.println("Usage: <nbrProducer> <nbrConsumer> <nbrAction>");
			System.exit(0);
		}
		Thread[]			producerTab			= new Thread[nbrProducer];
		Thread[]			consumerTab			= new Thread[nbrConsumer];
		SynchQueue<String>	synchronizedQueue	= new SynchQueue<String>();
		// Used to indicate that an agent has finished
		SemaphoreMozart2	workingAgentSem		= new SemaphoreMozart2(4 - (nbrConsumer+nbrProducer));

		for (int i=0; i<nbrProducer; i++)
		{
			Agent a = new Agent("" + i, nbrAction, true, synchronizedQueue, workingAgentSem);
			producerTab[i] = new Thread(a);
			producerTab[i].start();
		}

		for (int i=0; i<nbrConsumer; i++)
		{
			Agent a = new Agent("" + i, nbrAction, false, synchronizedQueue, workingAgentSem);
			consumerTab[i] = new Thread(a);
			consumerTab[i].start();
		}

		workingAgentSem.acquire();
		workingAgentSem.release();
		workingAgentSem.close();
		System.out.println("****** End of test ******");
		System.exit(0);
	}

}
