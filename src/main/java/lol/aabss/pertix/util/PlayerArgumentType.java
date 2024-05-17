package lol.aabss.pertix.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerArgumentType implements ArgumentType<PlayerEntity> {

    public static final DynamicCommandExceptionType INVALID_PLAYER = new DynamicCommandExceptionType(o -> Text.literal("Invalid player: " + o));
    @Override
    public PlayerEntity parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        String name = reader.getString().substring(argBeginning).split(" ")[0];
        reader.readString();
        List<AbstractClientPlayerEntity> players = MinecraftClient.getInstance().world.getPlayers();
        for (PlayerEntity plr : players){
            if(plr.getName().getString().equals(name) && !isNpc(plr.getName())){
                return plr;
            }
        }
        throw INVALID_PLAYER.createWithContext(reader,name);

    }


    public static boolean isNpc(Text p){
        return (p.contains(Text.literal("[")) ||
                p.contains(Text.literal("]")) ||
                p.contains(Text.literal(" ")) ||
                p.contains(Text.literal("-"))
        );
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof FabricClientCommandSource source) {
            List<String> names = new ArrayList<>();
            source.getPlayer().clientWorld.getPlayers().forEach(pl -> {
                if (!isNpc(pl.getName())) {
                    names.add(pl.getName().getString());
                }
            });
            return CommandSource.suggestMatching(names, builder);
        }
        return Suggestions.empty();
    }

    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }



    public static <S> PlayerEntity getPlayer(CommandContext<S> context, String name) {
        return context.getArgument(name, PlayerEntity.class);
    }
}
