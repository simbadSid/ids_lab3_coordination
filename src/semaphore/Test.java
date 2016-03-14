package semaphore;







public class Test
{
	public static void main(String[] args) throws InterruptedException
	{
		int					nbrProducer			= Integer.parseInt(args[0]);
		int					nbrConsumer			= Integer.parseInt(args[1]);
		int					nbrAction			= Integer.parseInt(args[2]);
		Thread[]			producerTab			= new Thread[nbrProducer];
		Thread[]			consumerTab			= new Thread[nbrConsumer];
		SynchQueue<String>	synchronizedQueue	= new SynchQueue<String>();
		// Used to indicate that an agent has finished
//TODO	SemaphoreMozart		workingAgentSem		= new SemaphoreMozart(1 - (nbrConsumer+nbrProducer));
		SemaphoreMozart		workingAgentSem		= new SemaphoreMozart(5 - (nbrConsumer+nbrProducer));

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
