package semaphore;

import java.util.concurrent.Semaphore;







public class SemaphoreMozart extends Semaphore
{
	private static final long serialVersionUID = 1L;
// -----------------------------------
// Builder
// -----------------------------------
	public SemaphoreMozart(int maxNbrThread)
	{
		super(maxNbrThread);

		// create an embedded space and construct a Capi instance for it
//TODO		this.core = DefaultMzsCore.newInstance();
//TODO		this.capi = new Capi(core);
	}

// -----------------------------------
// Local methods
// -----------------------------------

	public void close()
	{
//TODO        core.shutdown(true);
	}

	public void acquire() throws InterruptedException
	{
		super.acquire();
	}

	public void release()
	{
		super.release();
	}
}