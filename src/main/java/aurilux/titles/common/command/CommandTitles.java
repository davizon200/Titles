package aurilux.titles.common.command;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandTitles extends CommandBase {
    @Override
    public String getName() {
        return "titles";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.titles.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3) {
            throw new WrongUsageException("commands.titles.usage");
        }

        String commandName = args[0];
        EntityPlayerMP player = getPlayer(server, sender, args[1]);
        String titleKey = args[2];
        if (commandName.equals("add")) {
            TitlesAPI.addTitleToPlayer(player, titleKey, true);
        }
        else if (commandName.equals("remove")) {
            TitlesAPI.removeTitleFromPlayer(player, titleKey);
        }
        else {
            throw new WrongUsageException("commands.titles.usage");
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "add", "remove");
        }
        else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if (args.length == 3) {
            return getListOfStringsMatchingLastWord(args, TitlesAPI.getRegisteredTitles().keySet());
        }
        else {
            return Collections.emptyList();
        }
    }
}
