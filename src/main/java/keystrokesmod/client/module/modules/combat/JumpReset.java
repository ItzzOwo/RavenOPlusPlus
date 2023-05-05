package keystrokesmod.client.module.modules.combat;

import com.google.common.eventbus.Subscribe;
import keystrokesmod.client.event.impl.PacketEvent;
import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.lwjgl.input.Keyboard;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JumpReset extends Module {
    private KeyBinding JumpKey = new KeyBinding("Jump", Keyboard.KEY_SPACE, "Keystrokes");
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public JumpReset() {
        super("AutoJumpReset", ModuleCategory.combat);
        this.registerSetting(new DescriptionSetting("Auto Jump Reset that actually works."));
    }
    //actually works now
    @Subscribe
    public void onPacket(PacketEvent e) {
        if (e.isIncoming()) {
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
                if (((S12PacketEntityVelocity) e.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
                    Random random = new Random();
                    int value = random.nextInt(11) + 20;
                    executor.schedule(() -> KeyBinding.setKeyBindState(JumpKey.getKeyCode(), true), value, TimeUnit.MILLISECONDS);
                    int wait = random.nextInt(11) + 35;
                    executor.schedule(() -> KeyBinding.setKeyBindState(JumpKey.getKeyCode(), false), wait, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        executor.shutdownNow();
    }
}
