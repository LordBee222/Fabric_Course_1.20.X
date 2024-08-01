package net.mac.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mac.mccourse.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class NbtGetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment){

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("nbt")
                .then(CommandManager.literal("get").executes(NbtGetCommand::run)));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity playerEntity = context.getSource().getPlayer();
        ItemStack stack = playerEntity.getMainHandStack();
        NbtCompound nbtCommand = stack.getOrCreateNbt();
        if (stack == null){
            context.getSource().sendFeedback(() -> Text.literal("No Item Selected!"), true);
            return -1;
        } else if (nbtCommand == null){
            context.getSource().sendFeedback(() -> Text.literal("Item Contains No NBT"), true);
            return -1;
        } else {
            context.getSource().sendFeedback(() -> Text.literal(nbtCommand.asString()), true);
            return 1;
        }
    }
}
