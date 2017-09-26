import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.UnsupportedEncodingException;
import com.amazonaws.samples.AwsConsoleApp;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.util.EC2MetadataUtils;

public class RPCServer {
	private static final Logger logger = LoggerFactory.getLogger(RPCServer.class);

	private final static String QUEUE_NAME = "rpc_queue";

	public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
		int threadNumber = 10;
		final ExecutorService threadPool = new ThreadPoolExecutor(threadNumber, threadNumber, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");

		final Connection connection = connectionFactory.newConnection();
		final Channel channel = connection.createChannel();

		logger.info(" [*] Waiting for messages. To exit press CTRL+C");

		registerConsumer(channel, 50000, threadPool);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Invoking shutdown hook...");
				threadPool.shutdown();
				try {
					while (!threadPool.awaitTermination(10, TimeUnit.SECONDS))
						;
				} catch (InterruptedException e) {
					logger.info("Interrupted while waiting for termination");
				}
				logger.info("Thread pool shut down.");
			}
		});
	}

	private static void registerConsumer(final Channel channel, final int timeout, final ExecutorService threadPool)
			throws IOException {
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, final AMQP.BasicProperties properties,
					final byte[] body) throws IOException {
				try {
					logger.info(
							String.format("Received (channel %d) %s", channel.getChannelNumber(), new String(body)));
					final AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
							.correlationId(properties.getCorrelationId()).build();

					threadPool.submit(new Runnable() {
						public void run() {
							String response = "Some Exception was caught";
							try {
								String message = new String(body);
								System.out.println("Creating ec2 instance to calculate pi value for " + message);
								AwsConsoleApp ec = new AwsConsoleApp();
								System.out.println("created Instance");
								ec.createInstance(Integer.parseInt(message));
								/*RunInstancesRequest run = new RunInstancesRequest();
								RunInstancesResult result = AwsConsoleApp.ec2client.runInstances(run);
								
								System.out.println("Instance description " + result.toString());
								
								String instanceId = result.getReservation().getInstances().get(0).getInstanceId();*/
								//System.out.println(instanceId);
								
								Thread.sleep(5000);
								int count = 0;
								while(count<50){
									try {
										if (AwsConsoleApp.s3client.doesObjectExist("cloudpiamit", message+"ref.out")) {
											System.out.println("object exists" + AwsConsoleApp.s3client.getObjectAsString("cloudpiamit", message+"ref.out"));
											response = (AwsConsoleApp.s3client.getObjectAsString("cloudpiamit", message+"ref.out")).toString();
											ec.terminateInstance();
											break;
										}
									} catch (Exception e) {
										e.printStackTrace();
										response = "Exception caught";
									}
									Thread.sleep(2000);
									count++;
								}
								
								System.out.println("Publishing result for input " + message);
							} catch (Exception e) {
								System.out.println("caught Exception while printing" + e);
							} finally {
								try {
									
									System.out.println("Result sent to browser");
									System.out.println("Server running...");
									channel.basicPublish("", properties.getReplyTo(), replyProps,
											response.getBytes());
									// channel.basicAck(envelope.getDeliveryTag(),
									// true);
								} catch (Exception e) {
									logger.error("", e);
									System.out.println("caught Exception in finally" + e);

								}
							}
						}
					});
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		};
		System.out.println("Server stated");
		channel.basicConsume(QUEUE_NAME, true /* auto-ack */, consumer);
		System.out.println("Waiting for resquest...");
	}
}

