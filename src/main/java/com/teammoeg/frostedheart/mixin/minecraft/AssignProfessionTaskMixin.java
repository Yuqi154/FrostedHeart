package com.teammoeg.frostedheart.mixin.minecraft;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.AssignProfessionTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.server.ServerWorld;
@Mixin(AssignProfessionTask.class)
public abstract class AssignProfessionTaskMixin extends Task<VillagerEntity> {
	public AssignProfessionTaskMixin(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn) {
		super(requiredMemoryStateIn);
	}
	/**
	 * @author khjxiaogu
	 * @reason Disable vanilla profession
	 * */
	@Overwrite
	protected boolean shouldExecute(ServerWorld worldIn, VillagerEntity owner) {
		return false;
	}
}