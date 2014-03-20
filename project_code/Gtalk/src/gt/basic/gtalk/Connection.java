package gt.basic.gtalk;

import java.io.File;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.Build;

public class Connection {
	private static String HOST = "talk.google.com";
	private static String SERV = "gmail.com";
	private static int PORT = 5222;

	private static XMPPConnection connect = null;
	private static MessageListener listener = null;
	private static Chat chat = null;

	static {
		listener = new MessageListener() {

			@Override
			public void processMessage(Chat arg0, Message arg1) {
				// TODO Auto-generated method stub
			    int kk=0;
			    String str=arg1.getBody();
			    kk++;
			}
		};
	}

	public static XMPPConnection getConnect() {
		if (connect == null) {
			ConnectionConfiguration conf = new ConnectionConfiguration(HOST,
					PORT, SERV);
			/*
			conf.setSASLAuthenticationEnabled(true);
			conf.setCompressionEnabled(true);
			conf.setSecurityMode(SecurityMode.enabled);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				conf.setTruststoreType("AndroidCAStore");
				conf.setTruststorePassword(null);
				conf.setTruststorePath(null);
			} else {
				conf.setTruststoreType("BKS");
				String path = System.getProperty("javax.net.ssl.trustStore");
				if (path == null)
					path = System.getProperty("java.home") + File.separator
							+ "etc" + File.separator + "security"
							+ File.separator + "cacerts.bks";
				conf.setTruststorePath(path);
			}
			*/
			connect = new XMPPConnection(conf);
		}
		if (!connect.isConnected()) {
			try {
				connect.connect();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return connect;
	}

	public static XMPPConnection login(String username, String password) {
		XMPPConnection c = Connection.getConnect();
		if (!username.equals(c.getUser())) {
			try {
				c.login(username, password);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return connect;
	}

	public static Roster getRoster() {
		Roster roster = null;
		if (connect.isConnected() && connect.isAuthenticated()) {
			roster = connect.getRoster();
		}
		return roster;
	}

	public static Chat getChat(String userJID) {
		if (chat == null && connect.isAuthenticated() && connect.isConnected()) {
			ChatManager cm = connect.getChatManager();
			chat = cm.createChat(userJID, listener);
		}
		return chat;
	}
}