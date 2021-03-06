package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncUnlockedTitle implements IMessage {
    private String titleKey;

    public PacketSyncUnlockedTitle() {}

    public PacketSyncUnlockedTitle(String titleKey) {
        this.titleKey = titleKey;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        titleKey = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, titleKey);
    }

    public static class HandlerClient implements IMessageHandler<PacketSyncUnlockedTitle, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncUnlockedTitle message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TitlesAPI.addTitleToPlayer(Minecraft.getMinecraft().player, message.titleKey);
                }
            });
            return null;
        }
    }

    public static class HandlerServer implements IMessageHandler<PacketSyncUnlockedTitle, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncUnlockedTitle message, MessageContext ctx) {
            ctx.getServerHandler().player.server.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    TitlesAPI.addTitleToPlayer(player, message.titleKey);
                }
            });
            return null;
        }
    }
}