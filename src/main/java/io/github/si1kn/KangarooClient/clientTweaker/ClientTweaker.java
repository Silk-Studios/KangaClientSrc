package io.github.si1kn.KangarooClient.clientTweaker;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientTweaker implements ITweaker {


    private ArrayList<String> args = new ArrayList<>();

    private boolean isRunningOptifine = Launch.classLoader.getTransformers().stream()
            .anyMatch(p -> p.getClass().getName().toLowerCase(Locale.ENGLISH).contains("optifine"));

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
        this.args.addAll(args);

        addArg("gameDir", gameDir);
        addArg("assetsDir", assetsDir);
        addArg("version", profile);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {

        MixinBootstrap.init();
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
        Mixins.addConfiguration("mixins.kangaClient.json");

        if (isRunningOptifine) {
            environment.setObfuscationContext("notch");
        }

        if (environment.getObfuscationContext() == null) {
            environment.setObfuscationContext("notch");
        }

        try {
            classLoader.addURL(new File(System.getProperty("java.home"), "lib/ext/nashorn.jar").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        environment.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getLaunchArguments() {
        return isRunningOptifine ? new String[0] : args.toArray(new String[]{});
    }

    private void addArg(String label, Object value) {
        args.add("--" + label);
        args.add(value instanceof String ? (String) value : value instanceof File ? ((File) value).getAbsolutePath() : ".");
    }

    public boolean isUsingOptifine() {
        return isRunningOptifine;
    }


}
