package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.AutoWalk;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoMiNi extends AutoWalk {
    private static final BlockPos center = new BlockPos(326, 146, 200);

    private static final ArrayList<BlockPos> positions = new ArrayList<>(Arrays.asList(
            center.add(-4, 0, -1),
            center.add(-3, 0, -3),
            center.add(-1, 0, -4),
            center.add(1, 0, -4),
            center.add(3, 0, -3),
            center.add(4, 0, -1),
            center.add(4, 0, 1),
            center.add(3, 0, 3),
            center.add(1, 0, 4),
            center.add(-1, 0, 4),
            center.add(-3, 0, 3),
            center.add(-4, 0, 1)
    ));

    private static final ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>(Arrays.asList(
            new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
            new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8),
            new Pair<>(8, 9), new Pair<>(9, 10), new Pair<>(10, 11), new Pair<>(11, 0)
    ));

    @Override
    public ArrayList<BlockPos> getPositions() {
        return positions;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getEdges() {
        return edges;
    }

    @Override
    public boolean enabled() {
        return Configs.AutoMiNi;
    }

    @Override
    public String getName() {
        return "Auto Mi Ni";
    }

    @Override
    public KeyBind getKeyBind() {
        return new KeyBind(getName(), Keyboard.KEY_NONE);
    }

    @Override
    public double getJudgeDistance() {
        return 2;
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!enabled()) return;
        if (!should) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.startsWith("死亡 >> 玩家 " + getPlayer().getName())) {
            CommandsUtils.addCommand("/back");
        }
    }
}