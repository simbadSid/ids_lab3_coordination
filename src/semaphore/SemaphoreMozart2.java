package semaphore;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.mozartspaces.capi3.FifoCoordinator;
import org.mozartspaces.capi3.FifoCoordinator.FifoSelector;
import org.mozartspaces.core.Capi;
import org.mozartspaces.core.ContainerReference;
import org.mozartspaces.core.DefaultMzsCore;
import org.mozartspaces.core.Entry;
import org.mozartspaces.core.MzsCore;
import org.mozartspaces.core.MzsCoreException;
import org.mozartspaces.core.TransactionReference;
import org.mozartspaces.core.MzsConstants.RequestTimeout;






public class SemaphoreMozart2 extends Semaphore
{
	private static final long serialVersionUID = 1L;

	// -----------------------------------
// Attributes
// -----------------------------------
	private static long nbrCreatedSemaphore = 0;

	private int						nbrFreeSlot;
	private MzsCore					core;
	private Capi					capi;
	private ContainerReference		container;
	private String					containerName;
	private TransactionReference	transaction;

// -----------------------------------
// Builder
// -----------------------------------
	public SemaphoreMozart2(int nbrFreeSlot) throws MzsCoreException
	{
		super(nbrFreeSlot);
		this.nbrFreeSlot	= nbrFreeSlot;
		this.containerName	= this.newContainerName();

        List<FifoCoordinator> coords = Collections.singletonList(new FifoCoordinator());

		// create an embedded space and construct a Capi instance for it
		this.core			= DefaultMzsCore.newInstance();
		this.capi			= new Capi(core);
        this.container		= capi.createContainer(this.containerName, null, 10, coords, null, transaction);
        this.transaction	= capi.createTransaction(5000, null);
	}

// -----------------------------------
// Local methods
// -----------------------------------

	public void close()
	{
		core.shutdown(true);
	}

	public synchronized void acquire() throws InterruptedException
	{
		List<FifoSelector> selectors = Collections.singletonList(FifoCoordinator.newSelector());

		try
		{
			capi.read(container, selectors, 1000, null);
		}
		catch (MzsCoreException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	public synchronized void release()
	{
		this.nbrFreeSlot ++;
		if (this.nbrFreeSlot <= 0) return;

		String name = "Semaphore: " + this.containerName + ", rank: " + this.nbrFreeSlot;
		try
		{
	        capi.write(container, RequestTimeout.DEFAULT, transaction, new Entry(name));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

// -----------------------------------
// Private methods
// -----------------------------------
	private String newContainerName()
	{
		String res = "" + SemaphoreMozart2.nbrCreatedSemaphore;

		SemaphoreMozart2.nbrCreatedSemaphore ++;
		return res;
	}
}