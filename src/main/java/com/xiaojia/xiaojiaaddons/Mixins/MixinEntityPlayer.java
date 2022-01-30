package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {
    String lastString = null;
    IChatComponent lastResult = null;

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getDisplayName(IChatComponent component, CallbackInfoReturnable<IChatComponent> ci) {
//        String str = component.getFormattedText();
//        if (!Checker.enabled || !Configs.ColorNameNameTag) return;
//        if (!lastString.equals(str)) {
//            lastString = str;
//            lastResult = ColorName.convert(component);
//        }
//        ci.setReturnValue(lastResult);
    }
}
