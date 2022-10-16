/*
 * Copyright (c) 2022 TeamMoeg
 *
 * This file is part of Frosted Heart.
 *
 * Frosted Heart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Frosted Heart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Frosted Heart. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.teammoeg.frostedheart.mixin.minecraft;

import blusunrize.immersiveengineering.common.blocks.IETileProviderBlock;
import blusunrize.immersiveengineering.common.blocks.generic.MultiblockPartTileEntity;
import blusunrize.immersiveengineering.common.util.Utils;
import com.simibubi.create.content.contraptions.components.crafter.MechanicalCrafterBlock;
import com.teammoeg.frostedheart.util.IOwnerTile;
import dev.ftb.mods.ftbteams.FTBTeamsAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mixin({IETileProviderBlock.class, MechanicalCrafterBlock.class})
public class BlockMixin extends Block {


    public BlockMixin(Properties properties) {
        super(properties);
    }

    //@Inject(at=@At("HEAD"),method="onBlockPlacedBy(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V")
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (placer != null && placer instanceof ServerPlayerEntity)
            IOwnerTile.setOwner(Utils.getExistingTileEntity(worldIn, pos), FTBTeamsAPI.getPlayerTeam((ServerPlayerEntity) placer).getId());
    }

    @Inject(at = @At("HEAD"), method = "onBlockActivated(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/math/BlockRayTraceResult;)Lnet/minecraft/util/ActionResultType;")
    public void fh$on$onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> r) {
        if (!worldIn.isRemote) {
            TileEntity te = Utils.getExistingTileEntity(worldIn, pos);
            if (te instanceof MultiblockPartTileEntity) {
                te = ((MultiblockPartTileEntity) te).master();
            }
            IOwnerTile.trySetOwner(te, FTBTeamsAPI.getPlayerTeam((ServerPlayerEntity) player).getId());
        }
    }
}
