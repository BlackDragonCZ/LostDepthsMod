package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

@Mod.EventBusSubscriber
public class UpdateConfigProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		String playerName = "";
		File file = new File("");
		com.google.gson.JsonObject jObject = new com.google.gson.JsonObject();
		playerName = entity.getDisplayName().getString();
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/LostDepths/"), File.separator + (playerName + ".json"));
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			//TODO: fix command last-seen
			jObject.addProperty("last-seen", new java.text.SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
			{
				Gson mainGSONBuilderVariable = new GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(mainGSONBuilderVariable.toJson(jObject));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/LostDepths/"), File.separator + (playerName + ".json"));
		if (file.exists()) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					jObject = new Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					jObject.addProperty("last-seen", new java.text.SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			{
				Gson mainGSONBuilderVariable = new GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(mainGSONBuilderVariable.toJson(jObject));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
