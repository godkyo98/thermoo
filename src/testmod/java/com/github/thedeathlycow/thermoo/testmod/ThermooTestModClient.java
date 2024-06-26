package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class ThermooTestModClient implements ClientModInitializer {

    public static final Identifier HEART_OVERLAY_TEXTURE = Thermoo.id("textures/gui/fire_heart_overlay.png");

    private static final int TEXTURE_WIDTH = 18;
    private static final int TEXTURE_HEIGHT = 30;

    @Override
    public void onInitializeClient() {
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(ThermooTestModClient::renderFireHeartBar);
        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.register(ThermooTestModClient::renderMountFireHeartBar);
    }

    public static void renderMountFireHeartBar(
            DrawContext context,
            PlayerEntity player,
            LivingEntity mount,
            Vector2i[] heartPositions,
            int displayHealth,
            int maxDisplayHealth
    ) {
        renderFireHeartBar(context, mount, heartPositions, maxDisplayHealth);
    }

    public static void renderFireHeartBar(
            DrawContext context,
            PlayerEntity player,
            Vector2i[] heartPositions,
            int displayHealth,
            int maxDisplayHealth
    ) {
        renderFireHeartBar(context, player, heartPositions, maxDisplayHealth);
    }

    private static void renderFireHeartBar(DrawContext context, LivingEntity mount, Vector2i[] heartPositions, int maxDisplayHealth) {
        int fireHeartPoints = getNumFirePoints(mount, maxDisplayHealth);
        int fireHearts = getNumFireHeartsFromPoints(fireHeartPoints, maxDisplayHealth);

        for (int m = 0; m < fireHearts; m++) {
            Vector2i heartPos = heartPositions[m];

            if (heartPos == null) {
                continue;
            }

            // is half heart if this is the last heart being rendered and we have an odd
            // number of frozen health points
            int x = heartPos.x;
            int y = heartPos.y - 1;
            boolean isHalfHeart = m + 1 >= fireHearts && (fireHeartPoints & 1) == 1; // is odd check

            int u = isHalfHeart ? 9 : 0;

            context.drawTexture(HEART_OVERLAY_TEXTURE, x, y, u, 0, 9, 10, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }

    private static int getNumFirePoints(@NotNull LivingEntity player, int maxDisplayHealth) {
        float tempScale = player.thermoo$getTemperatureScale();
        if (tempScale <= 0f) {
            return 0;
        }
        return (int) (tempScale * maxDisplayHealth);
    }

    private static int getNumFireHeartsFromPoints(int fireHealthPoints, int maxDisplayHealth) {
        // number of whole hearts
        int frozenHealthHearts = MathHelper.ceil(fireHealthPoints / 2.0f);

        return Math.min(maxDisplayHealth / 2, frozenHealthHearts);
    }
}
