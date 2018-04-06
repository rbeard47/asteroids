package messaging;

import java.util.Vector;

public class MessageManager {

    private static MessageManager instance = null;

    private Vector<IMessageReceiver> receiverList;
    private Vector<Message> messages;

    protected MessageManager() {
        receiverList = new Vector<>();
        messages = new Vector<>();
    }

    public static MessageManager getInstance() {

        if (instance == null) {
            instance = new MessageManager();
        }

        return instance;
    }

    public synchronized void RegisterForMessage(IMessageReceiver receiver) {
        if (!receiverList.contains(receiver)) {
            receiverList.add(receiver);
        }
    }

    public void PostMessage(Message message) {
        for (IMessageReceiver receiver : receiverList) {
            receiver.Receive(message);
        }
    }
}
