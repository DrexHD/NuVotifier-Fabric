package com.vexsoftware.votifier.fabric.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.vexsoftware.votifier.fabric.NuVotifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.net.VotifierSession;
import com.vexsoftware.votifier.util.ArgsToVote;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.function.Predicate;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class NuVotifierCommand {

    private static NuVotifier plugin;
    private static final Permission RELOAD = new Permission.Atom(Identifier.fromNamespaceAndPath("nuvotifier", "reload"));
    private static final Permission TESTVOTE = new Permission.Atom(Identifier.fromNamespaceAndPath("nuvotifier", "testvote"));

    public static void register(NuVotifier plugin, CommandDispatcher<CommandSourceStack> dispatcher) {
        NuVotifierCommand.plugin = plugin;
        Predicate<CommandSourceStack> reloadPerm = src -> src.permissions().hasPermission(RELOAD) || src.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS));
        Predicate<CommandSourceStack> testVotePerm = src -> src.permissions().hasPermission(TESTVOTE) || src.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS));
        dispatcher.register(
            literal("nuvotifier").requires(reloadPerm.or(testVotePerm))
                .then(
                    literal("reload").requires(reloadPerm)
                        .executes(NuVotifierCommand::reload)
                ).then(
                    literal("testvote").then(
                        argument("args", StringArgumentType.greedyString()).requires(testVotePerm)
                            .executes(NuVotifierCommand::sendTestVote)
                    )
                )
        );
    }

    private static int reload(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSuccess(() -> Component.literal("Reloading NuVotifier...").withStyle(ChatFormatting.GRAY), false);
        if (plugin.reload()) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int sendTestVote(CommandContext<CommandSourceStack> ctx) {
        Vote v;
        try {
            String args = StringArgumentType.getString(ctx, "args");
            v = ArgsToVote.parse(args.split(" "));
        } catch (IllegalArgumentException e) {
            ctx.getSource().sendFailure(Component.literal("Error while parsing arguments to create test vote: " + e.getMessage()).withStyle(ChatFormatting.DARK_RED));
            ctx.getSource().sendFailure(Component.literal("Usage hint: /testvote [username] [serviceName=?] [username=?] [address=?] [localTimestamp=?] [timestamp=?]").withStyle(ChatFormatting.GRAY));
            return 0;
        }
        plugin.onVoteReceived(v, VotifierSession.ProtocolVersion.TEST, "localhost.test");

        return 1;
    }

}
