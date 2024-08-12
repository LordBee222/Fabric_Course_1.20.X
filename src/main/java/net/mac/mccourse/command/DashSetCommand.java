package net.mac.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.GaleforceEnchantment;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IEntityDataSaver;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DashSetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment){

        serverCommandSourceCommandDispatcher.register(CommandManager.literal("dash")
                .then(CommandManager.literal("set").executes(DashSetCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IPlayerDashSaver player = (IPlayerDashSaver) context.getSource().getPlayer();
        PlayerEntity playerEntity = context.getSource().getPlayer();
        player.getDashData().putBoolean(DashUtil.hasDashedKey, false);
        return 1;
    }
}
