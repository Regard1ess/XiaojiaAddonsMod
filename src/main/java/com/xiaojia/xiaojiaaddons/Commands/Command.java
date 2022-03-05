package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.GolemAlert;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoItemFrame;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoTerminal;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.BloodAssist;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Data;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoBlaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESP;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.BugReport;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ItemRename;
import com.xiaojia.xiaojiaaddons.Features.QOL.BatchCommands;
import com.xiaojia.xiaojiaaddons.Features.QOL.InCombatQOL;
import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import com.xiaojia.xiaojiaaddons.Features.Remote.ShowItem;
import com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Tests.TestM7;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TabUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Command extends CommandBase {
    @Override
    public String getCommandName() {
        return "xiaojia";
    }

    @Override
    public ArrayList<String> getCommandAliases() {
        ArrayList<String> res = new ArrayList<String>();
        res.add("xj");
        return res;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return getUsage();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (!Checker.enabled) return;
        if (strings == null) return;
        if (strings.length == 0) {
            ChatLib.chat(getUsage());
            return;
        }
        int x, y, z;
        String arg = strings[0];
        String allArg = strings.length == 1 ? "" : String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));
        switch (arg) {
            case "curmap":
                ChatLib.chat(SkyblockUtils.getCurrentMap());
                break;
            case "s":
                XiaojiaAddons.guiToOpen = new ConfigGuiNew(XiaojiaAddons.mc.gameSettings.guiScale);
                XiaojiaAddons.mc.gameSettings.guiScale = 3;
                break;
//            case "300":
//                if (strings.length == 1) ChatLib.chat("announce300: " + Dungeon.message300);
//                else {
//                    Dungeon.message300 = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));
//                    ChatLib.chat("Successfully set announce300 message to: " + Dungeon.message300);
//                }
//                break;
            case "show":
                String str = strings[1];
                String[] toShow = null;
                if (str.equals("dungarmor")) toShow = InCombatQOL.dungArmor;
                if (str.equals("dungtrash")) toShow = InCombatQOL.dungTrash;
                if (str.equals("runes")) toShow = InCombatQOL.runes;
                if (toShow != null) {
                    ChatLib.chat(Arrays.toString(toShow));
                    break;
                }
            case "report":
                Dungeon.showDungeonInfo();
                System.out.println(AutoBlaze.log.toString());
                XiaojiaAddons.guiToOpen = new BugReport();
                break;
            case "commands":
                BatchCommands.execute();
                break;
            case "copy":
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ChatLib.removeColor(allArg)), null);
                break;
            case "rename":
                ItemRename.process(strings);
                break;
            case "showitem":
                ShowItem.show();
                break;
            case "etherwarp":
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        ControlUtils.etherWarp(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                    } catch (Exception ignored) {
                    }
                }).start();
                break;
            case "blaze":
                System.out.println(AutoBlaze.log.toString());
//                AutoBlaze.test();
                break;
            case "cache":
                ColorName.showCache();
                break;
//            case "clear":
//                Fishing.clearBubbles();
//                break;
            case "core":
                x = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                z = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                ChatLib.chat(String.format("core for current (%d %d) is: %d", x, z,
                        new Room(x, z, new Data(
                                "unknown", "", 0, null
                        )).core));
                break;
            case "star":
                StarredMobESP.show();
                break;
            case "get":
                String url = strings[1];
                ChatLib.chat(RemoteUtils.get(url, new ArrayList<>(), false));
                break;
//            case "term":
//                ShortbowUtils.testTerminator();
//                break;
//            case "ena":
//                ShortbowUtils.enabled = !ShortbowUtils.enabled;
//                break;
            // debug commands
            case "debug":
                XiaojiaAddons.setDebug();
                break;
            case "tab":
                TabUtils.printTab();
                break;
            case "room":
                ChatLib.chat("Current Room: " + Dungeon.currentRoom);
                break;
            case "rooms":
                Dungeon.showRooms();
                break;
            case "map":
                Dungeon.showMap();
                break;
            case "p3":
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);
                z = Integer.parseInt(strings[3]);
                AutoItemFrame.setPosition(x, y, z);
                break;
            case "golem":
                GolemAlert.golemWarn();
                break;
            case "block":
                if (strings.length == 1) {
                    x = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                    y = MathUtils.floor(MathUtils.getY(MinecraftUtils.getPlayer()));
                    z = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                } else {
                    x = Integer.parseInt(strings[1]);
                    y = Integer.parseInt(strings[2]);
                    z = Integer.parseInt(strings[3]);
                }
                IBlockState iBlockState = BlockUtils.getBlockStateAt(new BlockPos(x, y, z));
                if (iBlockState == null) return;
                int meta = iBlockState.getBlock().getMetaFromState(iBlockState);
                ChatLib.chat(iBlockState.getBlock() + ", meta: " + meta);
                break;
            case "entities":
                List<Entity> list = getWorld().loadedEntityList;
                for (Entity entity : list) {
                    ChatLib.chat(entity.hasCustomName() + ", " + entity.getName() + ", " + MathUtils.getPosString(entity));
                }
                break;
            case "players":
                Dungeon.showPlayers();
                break;
            case "blood":
                BloodAssist.showBloodMobs();
                break;
            case "guitest":
                GuiTest.changeShow();
                break;
            case "ping":
                XiaojiaChat.ping();
                break;
            case "reconnect":
                ClientSocket.reconnect();
                break;
            case "shoot":
                ShortbowUtils.testEnderCrystals(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                break;
            case "m7":
                TestM7.m7();
                break;
            case "armorstand":
                TestM7.show();
                break;
//            case "click":
//                ClickTest.setEnabled();
//                break;
//            case "face":
//                ControlUtils.face(0, 65, 0);
//                break;
            default:
                if (XiaojiaAddons.isDebug()) SkyblockUtils.setCurrentMap(String.join(" ", strings));
                else ChatLib.chat(getUsage());
                break;
        }
    }

    private String getUsage() {
//        return "/xj curmap for current map information.\n/xj debug to debug.\n/xj s to open gui settings";
        return "" +
//                "&c/xj curmap&b for current map information.\n" +
                "&c/xj s&b to open gui settings.\n" +
//                "&c/xj 300&b to see, and &c/xj 300 message&b to set announce300 message.\n" +
                "&c/xj show dungarmor&b, &c/xj show dungtrash&b, and &c/xj show runes&b to show auto-sell dungeon armors/dungeon trash/runes seperately.\n" +
                "&c/xj rename&b to rename items.\n" +
                "&c/xj showitem&b to show held item.\n" +
                "&c/xj report&b to report a bug.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
