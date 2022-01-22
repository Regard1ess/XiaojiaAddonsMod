package com.xiaojia.xiaojiaaddons.Objects;

import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Checker {
    //    private static final HashSet<String> pool = new HashSet<>(Arrays.asList(
//            "1c6d48a9-6cb3-4656-8138-2590ec82fa68", "95cea3a6-9169-4f33-82a9-39ee8be011c3", "ed255f13-e4b2-4557-89c0-4d304a512f5e",
//            "cd5aa0c4-7939-4e8b-812d-1a755e450ef8", "ee7ac315-0e8b-4a5f-88d4-15c1f7a44a0a", "a4e72cfc-2347-4b56-802e-fe11f24e9546",
//            "0b524cc5-edb7-45d1-ba3b-675e15079137", "63179ad0-1e84-4a40-adfc-0f7deedbf2fe", "0a6c811b-0d22-4601-ab24-837260afbace",
//            "b8c75c35-d9ce-4b2b-b132-eada4f62ee83", "685b13b0-842f-4fb4-abc0-479da40e6f4b", "56f0a325-2201-495f-a472-cb3a4ba2c16b",
//            "7d4b729c-d267-49da-8cd3-4ad9dc8aedd6", "b770c257-0a35-4eff-92fe-4edaec09b129", "d71bd4bf-50ef-49aa-a443-aa346b37059a",
//            "a4c2ecd3-c4ec-41c5-b9cd-177cdd568962", "1f18edc8-4f79-435e-bd8b-9391ad765d9f", "94ddeb98-d8ca-47ba-8809-9adad50cd3fd",
//            "5ce7bdad-ba69-43a3-bfbf-91c93ef2bbdf", "128ee059-223a-4555-bd66-80a1d29564c7", "826d3f0d-30be-4238-b14f-f589fef59287",
//            "4c1367ce-9342-4eb2-9722-d15b2f10184e", "2d2fb6ac-e204-4bb2-b833-f453c8a4896c", "d77cb85a-0234-46a2-a143-bb8838e1526e",
//            "2bd10800-7bfb-40ef-8bf4-906004b5aec1", "5ec1ee03-4495-40f5-938d-220231dab730"
//    ));
    public static boolean enabled = false;
    private long lastCheck = 0;

//    @SubscribeEvent
//    public void onTick(TickEndEvent event) {
//        try {
//            enabled = pool.contains(getPlayer().getUniqueID().toString());  // && SkyblockUtils.isInSkyblock();
//        } catch (Exception e) {
//            enabled = false;
//        }
//    }

    @SubscribeEvent
    public void onTickCheck(TickEndEvent event) {
        long cur = TimeUtils.curTime();
        if (getPlayer() == null) return;
        if (getWorld() == null) return;
        if (cur - lastCheck > 30000) {
            lastCheck = cur;
            try {
                List<BasicNameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("uuid", XiaojiaChat.getUUID()));
                list.add(new BasicNameValuePair("version", XiaojiaAddons.VERSION));

                String permit = RemoteUtils.get("/xja/verify", list);

                JsonParser jsonParser = new JsonParser();
                enabled = jsonParser.parse(permit).getAsJsonObject().get("res").getAsBoolean();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
