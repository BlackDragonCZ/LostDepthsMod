package cz.blackdragoncz.lostdepths.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

// Definition of a single advancement. Everything is stored lazily because these are built during static init, before the item registry is populated.
public final class LostdepthsAdvancementBuilder {

	private final ResourceLocation id;
	@Nullable
	private final LostdepthsAdvancementBuilder parent;
	private final Supplier<? extends ItemLike> icon;
	private final FrameType frame;
	private final Map<String, Supplier<CriterionTriggerInstance>> criteria = new LinkedHashMap<>();

	private RequirementsStrategy strategy = RequirementsStrategy.AND;
	@Nullable
	private ResourceLocation background;
	private boolean showToast = true;
	private boolean announceChat = true;
	private boolean hidden = false;
	private int experience = 0;

	public LostdepthsAdvancementBuilder(@Nullable LostdepthsAdvancementBuilder parent, ResourceLocation id, Supplier<? extends ItemLike> icon, FrameType frame) {
		this.parent = parent;
		this.id = id;
		this.icon = icon;
		this.frame = frame;
	}

	// Adds a named criterion. Call more than once for a multi-dependency advancement (vanilla's "Adventuring Time" shape).
	public LostdepthsAdvancementBuilder criterion(String name, Supplier<CriterionTriggerInstance> trigger) {
		criteria.put(name, trigger);
		return this;
	}

	// Any single criterion completes the advancement. Default is that all of them must be met.
	public LostdepthsAdvancementBuilder anyOf() {
		this.strategy = RequirementsStrategy.OR;
		return this;
	}

	public LostdepthsAdvancementBuilder background(String texture) {
		this.background = new ResourceLocation(id.getNamespace(), texture);
		return this;
	}

	public LostdepthsAdvancementBuilder experience(int experience) {
		this.experience = experience;
		return this;
	}

	public LostdepthsAdvancementBuilder hidden() {
		this.hidden = true;
		return this;
	}

	public LostdepthsAdvancementBuilder noToast() {
		this.showToast = false;
		return this;
	}

	public LostdepthsAdvancementBuilder noAnnounce() {
		this.announceChat = false;
		return this;
	}

	public ResourceLocation id() {
		return id;
	}

	@Nullable
	public LostdepthsAdvancementBuilder parent() {
		return parent;
	}

	public String titleKey() {
		return "advancements." + id.getPath() + ".title";
	}

	public String descriptionKey() {
		return "advancements." + id.getPath() + ".descr";
	}

	public Component translateTitle() {
		return Component.translatable(titleKey());
	}

	public Component translateDescription() {
		return Component.translatable(descriptionKey());
	}

	// Called from datagen only, once the parent has already been built.
	public Advancement build(Map<LostdepthsAdvancementBuilder, Advancement> built, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
		if (criteria.isEmpty())
			throw new IllegalStateException("Advancement " + id + " has no criteria");
		// recipeAdvancement() is the same builder minus sends_telemetry_event, which vanilla only reports for its own namespace.
		Advancement.Builder builder = Advancement.Builder.recipeAdvancement();
		if (parent != null) {
			Advancement parentAdvancement = built.get(parent);
			if (parentAdvancement == null)
				throw new IllegalStateException("Advancement " + id + " is declared before its parent " + parent.id());
			builder.parent(parentAdvancement);
		}
		builder.display(new ItemStack(icon.get().asItem()), translateTitle(), translateDescription(), background, frame, showToast, announceChat, hidden);
		criteria.forEach((name, trigger) -> builder.addCriterion(name, trigger.get()));
		builder.requirements(strategy);
		if (experience > 0)
			builder.rewards(AdvancementRewards.Builder.experience(experience));
		return builder.save(saver, id, existingFileHelper);
	}
}
