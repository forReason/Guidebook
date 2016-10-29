package gigaherz.guidebook.guidebook;

import gigaherz.common.ItemRegistered;
import gigaherz.guidebook.GuidebookMod;
import gigaherz.guidebook.guidebook.client.BookDocument;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemGuidebook extends ItemRegistered
{
    public ItemGuidebook(String name)
    {
        super(name);
        setMaxStackSize(1);
        setUnlocalizedName(GuidebookMod.MODID + ".guidebook");
        setCreativeTab(GuidebookMod.tabMagic);
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return showBook(worldIn, stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        EnumActionResult result = showBook(worldIn, itemStackIn);
        return ActionResult.newResult(result, itemStackIn);
    }

    private EnumActionResult showBook(World worldIn, ItemStack stack)
    {
        if (!worldIn.isRemote)
            return EnumActionResult.FAIL;

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null || !nbt.hasKey("Book", Constants.NBT.TAG_STRING))
            return EnumActionResult.FAIL;

        GuidebookMod.proxy.displayBook(nbt.getString("Book"));

        return EnumActionResult.SUCCESS;
    }

    public ItemStack of(ResourceLocation book)
    {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Book", book.toString());
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        BookDocument.REGISTRY.keySet().stream().map(this::of).forEach(subItems::add);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null)
        {
            String book = tag.getString("Book");
            if (book != null)
            {
                BookDocument bookDocument = BookDocument.get(new ResourceLocation(book));
                if (bookDocument != null)
                {
                    String name = bookDocument.getBookName();
                    if (name != null)
                        return name;
                }
                else
                {

                }
            }
        }

        return super.getItemStackDisplayName(stack);
    }
}
