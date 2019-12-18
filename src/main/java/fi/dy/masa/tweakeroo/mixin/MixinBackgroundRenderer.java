package fi.dy.masa.tweakeroo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import fi.dy.masa.tweakeroo.config.Configs;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import fi.dy.masa.tweakeroo.renderer.RenderUtils;

@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer
{
    @Inject(method = "applyFog(Lnet/minecraft/client/render/Camera;I)V", require = 0,
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/tag/FluidTags;LAVA:Lnet/minecraft/tag/Tag;")),
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;fogDensity(F)V",
                     ordinal = 0, shift = At.Shift.AFTER))
    private void onSetupLavaFog(Camera camera, int startCoords, CallbackInfo ci)
    {
        if (FeatureToggle.TWEAK_LAVA_VISIBILITY.getBooleanValue() &&
            Configs.Generic.LAVA_VISIBILITY_OPTIFINE.getBooleanValue() == false)
        {
            RenderUtils.overrideLavaFog(MinecraftClient.getInstance().getCameraEntity());
        }
    }
}