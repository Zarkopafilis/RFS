package mc.alk.arena.objects.messaging.plugins;

import mc.alk.arena.objects.messaging.Channel;
import mc.alk.arena.util.MessageUtil;

public class HerochatChannel implements Channel {
	com.dthielke.herochat.Channel channel;

	public HerochatChannel(com.dthielke.herochat.Channel channel) {
		this.channel = channel;
	}

	
	public void broadcast(String msg) {
		if (msg == null) return;
		channel.announce(MessageUtil.colorChat(msg));
	}
}
