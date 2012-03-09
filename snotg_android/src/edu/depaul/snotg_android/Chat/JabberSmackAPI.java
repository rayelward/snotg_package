package edu.depaul.snotg_android.Chat;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import edu.depaul.snotg_android.SnotgAndroidConstants;

import android.util.Log;
/**
 * @author 			Milad
 * Date: 			2/21/2012
 * Description: 	This class handle all the calls to the server using the Jabber smack framework 
 * 					and also handles recieving any calls from other users.
 */
public class JabberSmackAPI implements MessageListener {
	private static final String TAG = "JabberSmackAPI";

	XMPPConnection connection;
	private XMPPClient xmppClient;

	public JabberSmackAPI(XMPPClient xmppClient) {
		super();
		this.xmppClient = xmppClient;

	}
	/**
	 * @author 			Milad
	 * Date: 			2/21/2012
	 * Description: 	This method logins us into the server useing user name and password sent to it.
	 */
	public void login(String userName, String password) throws XMPPException {
		ConnectionConfiguration config = 
				new ConnectionConfiguration( SnotgAndroidConstants.CHAT_HOST ,SnotgAndroidConstants.CHAT_PORT_NUMBER,SnotgAndroidConstants.CHAT_SERVICE_NAME);
	    config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(false);
		connection = new XMPPConnection(config);
		connection.connect();
		connection.login(userName, password);
		setConnection(connection);
	}
	/**
	 * @author 			Milad
	 * Date: 			2/21/2012
	 * Description: 	This method sends any message to a specific user.
	 */
	public void sendMessage(String message, String to) throws XMPPException {
		Chat chat = connection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}
	/**
	 * @author 			Milad
	 * Date: 			2/21/2012
	 * Description: 	This method disconnects us from the chat server.
	 */
	public void disconnect() {
		connection.disconnect();
	}
	/**
	 * @author 			Milad
	 * Date: 			2/21/2012
	 * Description: 	This method process incoming messages from users, but is not being used.
	 */
	public void processMessage(Chat chat, Message message) {
		// if(message.getType() == Message.Type.chat)
		// xmppClient.getMessage(message.getBody(),message.getFrom());
	}
	/**
	 * @author 			Milad
	 * Date: 			2/21/2012
	 * Description: 	This method sets up connection using xmppconnection object
	 */
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.i(TAG, "Got text [" + message.getBody()
								+ "] from [" + fromName + "]");
						xmppClient.getMessage(message.getBody(), fromName);
					}
				}

			}, filter);

		}
	}
}
