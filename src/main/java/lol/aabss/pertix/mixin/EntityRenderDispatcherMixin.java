package lol.aabss.pertix.mixin;

import lol.aabss.pertix.elements.HidePlayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lol.aabss.pertix.util.PlayerArgumentType.isNpc;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        if (player.isMainPlayer() || !HidePlayers.playershidden){
            return;
        }
        if (HidePlayers.getWhitelistedPlayers().contains(Text.of(player.getName().getString().toLowerCase()))) {
            return;
        }
        if (isNpc(player.getName())){
            return;
        }
        ci.cancel();
    }
}
