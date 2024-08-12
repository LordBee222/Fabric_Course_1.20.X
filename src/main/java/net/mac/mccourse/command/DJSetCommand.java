package net.mac.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.mac.mccourse.util.IPlayerDoubleJumpSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class DJSetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment){

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("dj")
                .then(CommandManager.literal("set").executes(DashSetCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IPlayerDoubleJumpSaver playerDoubleJumpData = (IPlayerDoubleJumpSaver) context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();
        playerDoubleJumpData.getDoubleJumpData().putInt(DashUtil.hasDashedKey, 5);
        return 1;
    }
}
