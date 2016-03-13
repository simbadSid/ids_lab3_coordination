package ringTopology;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;






public class Test
{
	public static void main(String[] args) throws IOException, TimeoutException
	{
		int nbrNode = Integer.parseInt(args[0]);
		String queueIp	= args[1];
		Node[] nodeTab = new Node[nbrNode];

		for (int i=nbrNode-1; i>=0; i--)
		{
			String	nodeId				= "Node_" 	+ i;
			String	waitingQueueName	= "Queue_"	+ i;
			String	sendingQueueName	= "Queue_"	+ ((i+1)%nbrNode) ;
			nodeTab[i] = new Node(nodeId, waitingQueueName, sendingQueueName, queueIp);
		}

		Scanner sc = new Scanner(System.in);
		int nodeIndex = -1;
		String message = null;

		while(true)
		{
			try
			{
				System.out.print("Select the the index of a node: ");
				nodeIndex = sc.nextInt();
				if ((nodeIndex < 0) || (nodeIndex >= nbrNode))
					throw new Exception();
				System.out.print("Select a message to send      : ");
				message = sc.next();
				break;
			}
			catch(InputMismatchException e)
			{
				System.out.println("\t*** Wrong choice. Try again");
			}
			catch(Exception e)
			{
				break;
			}
		}

		nodeTab[nodeIndex].sndMsg(message);
		sc.close();

		for (int i=nbrNode-1; i>=0; i--)
		{
//			nodeTab[i].close();
		}
	}

}
