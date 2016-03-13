package ringTopology;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;







public class Node
{
// ------------------------------------
// Attributes
// ------------------------------------
	private String				nodeID;
	private Connection			connection;
	private String				emissionQueueName;
	private Channel				receptionChannel;
	private Channel				emissionChannel;

// ------------------------------------
// Builder emission
// ------------------------------------
	public Node(String nodeID, String waitingQueue, String sendingQueue, String queueIP) throws IOException, TimeoutException
	{
		this.nodeID				= new String(nodeID);
		this.emissionQueueName	= new String(sendingQueue);

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(queueIP);
		this.connection = factory.newConnection();

		this.emissionChannel	= connection.createChannel();
		this.receptionChannel	= connection.createChannel();
		this.emissionChannel .queueDeclare(sendingQueue, false, false, false, null);
		this.receptionChannel.queueDeclare(waitingQueue, false, false, false, null);

	    Consumer consumer = new DefaultConsumer(this.receptionChannel)
	    {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
	          String message = new String(body, "UTF-8");
	          receiveMsg(message);
	        }
	    };
	    this.receptionChannel.basicConsume(waitingQueue, true, consumer);
}

// ------------------------------------
// Local methods
// ------------------------------------
	public void receiveMsg(String message) throws IOException
	{
        System.out.println("\t[" + this.nodeID + "] Received '" + message + "'");
		String[] formatedMessage = message.split(" ", 2);

		if (formatedMessage[0].equals(this.nodeID))
		{
			System.out.println("\t[" + this.nodeID + "] Received my own message '" + formatedMessage[1] + "'");
		}
		else
		{
			this.transmitMsg(formatedMessage[0], formatedMessage[1]);
		}
	}

	/**
	 *  Used to send a message at the first time
	 */
	public void sndMsg(String message) throws IOException
	{
		this.transmitMsg(this.nodeID, message);
	}

	public void close() throws IOException, TimeoutException
	{
		this.emissionChannel.close();
		this.receptionChannel.close();
		this.connection.close();
	}

	/**
	 *  Used to transmit a message initialized by an other node
	 */
	private void transmitMsg(String id, String message) throws IOException
	{
		String formatedMessage = id + " " + message;
	    emissionChannel.basicPublish("", emissionQueueName, null, formatedMessage.getBytes());
	    System.out.println("\t[" + this.nodeID + "] Sent '" + message + "'");
		
	}
}