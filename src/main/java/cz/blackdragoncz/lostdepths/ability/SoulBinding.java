package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * NBT for a soul-bound item: one owner, plus everyone who signed a contract book the owner wrote.
 * Identity is by UUID throughout so renames cannot break a binding. Names are kept alongside for
 * display and as the fallback for books signed before UUIDs were recorded.
 */
public final class SoulBinding {

    private static final String OWNER_ID = "soul_owner";
    private static final String OWNER_NAME = "soul_owner_name";
    private static final String SIGNERS = "soul_signers";
    private static final String ENTRY_NAME = "name";
    private static final String ENTRY_ID = "id";

    public static final String BOOK_SIGNER = "contract_signer";
    public static final String BOOK_SIGNER_ID = "contract_signer_id";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_AUTHOR_ID = "author_id";

    private SoulBinding() {
    }

    public static boolean isBound(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.hasUUID(OWNER_ID);
    }

    @Nullable
    public static UUID owner(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.hasUUID(OWNER_ID) ? tag.getUUID(OWNER_ID) : null;
    }

    public static String ownerName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? "" : tag.getString(OWNER_NAME);
    }

    /** The carrier must be the bound owner - the item does nothing in anyone else's inventory. */
    public static boolean isOwner(ItemStack stack, Player player) {
        UUID owner = owner(stack);
        return owner != null && owner.equals(player.getUUID());
    }

    public static void bind(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(OWNER_ID, player.getUUID());
        tag.putString(OWNER_NAME, player.getGameProfile().getName());
    }

    /** Shift-right-click reset: drops the owner and every signature, so the item is blank again. */
    public static void reset(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null)
            return;
        tag.remove(OWNER_ID);
        tag.remove(OWNER_NAME);
        tag.remove(SIGNERS);
    }

    /** Display only. */
    public static List<String> signerNames(ItemStack stack) {
        ListTag list = signerList(stack);
        List<String> names = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++)
            names.add(list.getCompound(i).getString(ENTRY_NAME));
        return names;
    }

    /**
     * Whether this player signed the owner's contract. Matches on UUID; an entry copied from a
     * pre-UUID book has only a name, so it falls back to a name match and writes the UUID in on the
     * spot - the attacking player is the one moment we can resolve it without a blocking lookup.
     */
    public static boolean matchesSigner(ItemStack stack, Player player) {
        ListTag list = signerList(stack);
        String name = player.getGameProfile().getName();

        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            if (entry.hasUUID(ENTRY_ID)) {
                if (entry.getUUID(ENTRY_ID).equals(player.getUUID()))
                    return true;
            } else if (entry.getString(ENTRY_NAME).equalsIgnoreCase(name)) {
                entry.putUUID(ENTRY_ID, player.getUUID());
                stack.getOrCreateTag().put(SIGNERS, list);
                return true;
            }
        }
        return false;
    }

    /**
     * Copies the signature off a signed contract book. The book must have been written by this item's
     * owner, so a signature cannot be transplanted onto a third party's item, and a self-signed book is
     * refused - being immune to yourself is meaningless. The book itself is untouched.
     *
     * @return true if a new signature was stored
     */
    public static boolean addSigner(ItemStack stack, ItemStack book) {
        CompoundTag bookTag = book.getTag();
        UUID ownerId = owner(stack);
        if (bookTag == null || ownerId == null)
            return false;

        String signerName = bookTag.getString(BOOK_SIGNER);
        if (signerName.isEmpty())
            return false;
        UUID signerId = bookTag.hasUUID(BOOK_SIGNER_ID) ? bookTag.getUUID(BOOK_SIGNER_ID) : null;

        if (!isWrittenBy(bookTag, ownerId, ownerName(stack)))
            return false;
        // Self-signed: the author put their own name to it.
        if (signerId != null ? signerId.equals(ownerId) : signerName.equalsIgnoreCase(ownerName(stack)))
            return false;
        if (alreadyPresent(stack, signerId, signerName))
            return false;

        CompoundTag entry = new CompoundTag();
        entry.putString(ENTRY_NAME, signerName);
        if (signerId != null)
            entry.putUUID(ENTRY_ID, signerId);

        ListTag list = signerList(stack);
        list.add(entry);
        stack.getOrCreateTag().put(SIGNERS, list);
        return true;
    }

    /** Author check by UUID, falling back to the name for books written before author_id existed. */
    private static boolean isWrittenBy(CompoundTag bookTag, UUID ownerId, String ownerName) {
        if (bookTag.hasUUID(BOOK_AUTHOR_ID))
            return bookTag.getUUID(BOOK_AUTHOR_ID).equals(ownerId);
        String author = bookTag.getString(BOOK_AUTHOR);
        return !author.isEmpty() && author.equalsIgnoreCase(ownerName);
    }

    private static boolean alreadyPresent(ItemStack stack, @Nullable UUID signerId, String signerName) {
        ListTag list = signerList(stack);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            if (signerId != null && entry.hasUUID(ENTRY_ID) && entry.getUUID(ENTRY_ID).equals(signerId))
                return true;
            if (entry.getString(ENTRY_NAME).equalsIgnoreCase(signerName))
                return true;
        }
        return false;
    }

    private static ListTag signerList(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? new ListTag() : tag.getList(SIGNERS, Tag.TAG_COMPOUND);
    }

    /**
     * Fills in the UUID on a book signed before this version, if that player happens to be online.
     * Online players only - GameProfileCache.get(String) can block on a Mojang lookup, which must never
     * happen on the server thread.
     *
     * @return true if the book was changed
     */
    public static boolean backfillBookIds(ItemStack book, @Nullable MinecraftServer server) {
        CompoundTag tag = book.getTag();
        if (tag == null || server == null)
            return false;

        boolean changed = fillId(tag, server, BOOK_SIGNER, BOOK_SIGNER_ID);
        changed |= fillId(tag, server, BOOK_AUTHOR, BOOK_AUTHOR_ID);
        return changed;
    }

    private static boolean fillId(CompoundTag tag, MinecraftServer server, String nameKey, String idKey) {
        if (tag.hasUUID(idKey))
            return false;
        String name = tag.getString(nameKey);
        if (name.isEmpty())
            return false;
        Player player = server.getPlayerList().getPlayerByName(name);
        if (player == null)
            return false;
        tag.putUUID(idKey, player.getUUID());
        return true;
    }
}
