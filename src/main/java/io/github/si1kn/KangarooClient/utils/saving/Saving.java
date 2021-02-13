package io.github.si1kn.KangarooClient.utils.saving;


import io.github.si1kn.KangarooClient.KangaClient;
import io.github.si1kn.KangarooClient.keybinds.KeyBindHandler;
import io.github.si1kn.KangarooClient.keybinds.Keybind;
import io.github.si1kn.KangarooClient.mods.ModManager;
import io.github.si1kn.KangarooClient.mods.Module;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("all")
public class Saving {

    @Getter
    private final static Saving instance = new Saving(new File("KangaClient"));

    @Getter
    private File optionsFile;

    @SneakyThrows
    public Saving(File optionsFileIn) {
        this.optionsFile = new File(optionsFileIn, "Kanga.json");
        optionsFileIn.mkdir();

        if (!this.optionsFile.exists()) {
            optionsFile.createNewFile();
        }
    }

    @SneakyThrows
    public void saveOptions() {
        ModManager modManager = KangaClient.getInstance().getManager();

        if (!getOptionsFile().exists()) {
            return;
        }

        if (!this.optionsFile.exists()) {
            optionsFile.createNewFile();
        }


        JSONObject obj = new JSONObject();


        obj.put("fb", ModManager.getFullBright().isToggled());
        obj.put("motionBlur", ModManager.getMotionBlurMod().isEnabled());
        obj.put("motionBlurAmmount", ModManager.getMotionBlurMod().getBlurAmount());

        obj.put("modToggled" + this.getName(modManager.getFpsMod()), modManager.isModuleInList(modManager.getFpsMod()));
        obj.put("modToggled" + this.getName(modManager.getSpotifyMod()), modManager.isModuleInList(modManager.getSpotifyMod()));
        obj.put("modToggled" + this.getName(modManager.getCoordsMod()), modManager.isModuleInList(modManager.getCoordsMod()));
        obj.put("modToggled" + this.getName(modManager.getCpsMod()), modManager.isModuleInList(modManager.getCpsMod()));
        obj.put("modToggled" + this.getName(modManager.getPackDisplayMod()), modManager.isModuleInList(modManager.getPackDisplayMod()));


        KeyBindHandler keyBindHandler = KangaClient.getInstance().getHandler();
        for (Keybind key : keyBindHandler.keybinds.values()) {
            obj.put("keybind" + key.getKeyDescription().replace(" ", ""), key.getKeyCode());
        }

        for (Module m : KangaClient.getInstance().getManager().getHudMods()) {
            JSONArray company = new JSONArray();
            company.add(m.pos.getX());
            company.add(m.pos.getY());
            obj.put(m.getClass().getSimpleName(), company);
        }


        FileWriter file = null;
        try {
            file = new FileWriter(this.optionsFile);
            file.write(obj.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.err.println(obj);

    }

    @SneakyThrows
    public void loadSettings() {
        try {

            if (!getOptionsFile().exists()) {
                return;
            }

            if (!this.optionsFile.exists()) {
                optionsFile.createNewFile();
            }


            JSONObject jsonObject = (JSONObject) new JSONParser().parse(readFile(this.optionsFile.getPath(), Charset.defaultCharset()));

            ModManager modManager = KangaClient.getInstance().getManager();
            for (Module m : modManager.getHudMods()) {
                String[] str = jsonObject.get(m.getClass().getSimpleName()).toString().replace("[", "").replace("]", "").split(",");
                m.pos.setX(getFloatAsString(str[0]));
                m.pos.setY(getFloatAsString(str[1]));
            }

            modManager.getMotionBlurMod().setEnabled(getBooleanAsString(jsonObject.get("motionBlur").toString()));
            modManager.getMotionBlurMod().setBlurAmount(getFloatAsString(jsonObject.get("motionBlurAmmount").toString()));

            if (getBooleanAsString(jsonObject.get("modToggledFpsMod").toString())) {
                modManager.getHudMods().add(modManager.getFpsMod());
            }
            if (getBooleanAsString(jsonObject.get("modToggledCpsMod").toString())) {
                modManager.getHudMods().add(modManager.getCpsMod());
            }
            if (getBooleanAsString(jsonObject.get("modToggledCoordsMod").toString())) {
                modManager.getHudMods().add(modManager.getCoordsMod());
            }
            if (getBooleanAsString(jsonObject.get("modToggledPackDisplayMod").toString())) {
                modManager.getHudMods().add(modManager.getPackDisplayMod());
            }
            if (getBooleanAsString(jsonObject.get("modToggledSpotifyMod").toString())) {
                modManager.getHudMods().add(modManager.getSpotifyMod());
            }

            KeyBindHandler keyBindHandler = KangaClient.getInstance().getHandler();
            for (Keybind key : keyBindHandler.keybinds.values()) {
                key.setKeyCode((int) getFloatAsString(jsonObject.get("keybind" + key.getKeyDescription().replace(" ", "")).toString()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    private float getFloatAsString(String str) {
        return Float.parseFloat(str);
    }

    private boolean getBooleanAsString(String str) {
        return Boolean.parseBoolean(str);
    }

    public String getName(Module m) {
        return m.getClass().getSimpleName();
    }
}