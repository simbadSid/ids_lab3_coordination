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
	}

// -----------------------------------
// Local methods
// -----------------------------------

	public void close()
	{
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