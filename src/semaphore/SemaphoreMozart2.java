package semaphore;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.mozartspaces.core.config.Configuration;






public class SemaphoreMozart2 extends Semaphore
{
	private static final long serialVersionUID = 1L;

// -----------------------------------
// Attributes
// -----------------------------------
	private static int			nbrCreatedSemaphore	= 0;
	private static final int	defaultPort			= 2222;

	private int						nbrFreeSlot;
	private String					containerName;
	private int						port;
private static MzsCore					core;
	private Capi					capi;
	private ContainerReference		container;
	private TransactionReference	transaction;

// -----------------------------------
// Builder
// -----------------------------------
	public SemaphoreMozart2(int nbrFreeSlot) throws MzsCoreException, URISyntaxException
	{
		super(nbrFreeSlot);
		this.nbrFreeSlot	= nbrFreeSlot;
		this.initLocalConfig();

		List<FifoCoordinator> coords = Collections.singletonList(new FifoCoordinator());
		// Create an embedded space and construct a Capi instance for it
if (SemaphoreMozart2.core == null)
{
	SemaphoreMozart2.core = DefaultMzsCore.newInstance();
}

		this.capi			= new Capi(core);
		this.container		= capi.createContainer(this.containerName, null, 1000, coords, null, transaction);
		this.transaction	= capi.createTransaction(RequestTimeout.INFINITE, null);
System.out.println("**** Space = " + core.getConfig().getSpaceUri());
	}

// -----------------------------------
// Local methods
// -----------------------------------
	public void close()
	{
		core.shutdown(true);
	}

	public void acquire() throws InterruptedException
	{
		List<FifoSelector> selectors = Collections.singletonList(FifoCoordinator.newSelector());

		try
		{
			capi.read(container, selectors, RequestTimeout.INFINITE, null);
		}
		catch (MzsCoreException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void release()
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
	private void initLocalConfig()
	{
		this.containerName	= "" + SemaphoreMozart2.nbrCreatedSemaphore;
		this.port			= SemaphoreMozart2.defaultPort + SemaphoreMozart2.nbrCreatedSemaphore;

		SemaphoreMozart2.nbrCreatedSemaphore ++;
	}
}