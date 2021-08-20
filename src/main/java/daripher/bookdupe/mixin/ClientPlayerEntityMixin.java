package daripher.bookdupe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CEditBookPacket;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin
{
	private static final ItemStack DUPE_BOOK = new ItemStack(Items.WRITABLE_BOOK, 1);
	
	@Inject(at = @At("HEAD"), method = "chat", cancellable = true)
	public void injectChat(String message, CallbackInfo ci)
	{
		if (message.equals(".d"))
		{
			ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
			
			if (player.getMainHandItem().getItem() == Items.WRITABLE_BOOK)
			{
				IPacket<?> packet = new CEditBookPacket(DUPE_BOOK, true, player.inventory.selected);
				player.connection.send(packet);
			}
			
			ci.cancel();
		}
	}
	
	static
	{
		String s = "";
		
		for (int i = 0; i < 21845; i++)
		{
			s += (char) 2048;
		}
		
		ListNBT listTag = new ListNBT();
		listTag.addTag(0, StringNBT.valueOf(s));
		DUPE_BOOK.addTagElement("pages", listTag);
	}
}