package com.kashdeya.pa.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {
	
	public static float ATTACK_DAMAGE;
	public static boolean CALL_FOR_HELP;
	public static boolean LONG_TERM_MEMORY;
	
	@SubscribeEvent
	public void onEntityJoinedWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityCreature && event.getEntity().isNonBoss() && !(event.getEntity() instanceof IMob)) {
			EntityCreature entity = (EntityCreature) event.getEntity();
			World world = entity.getEntityWorld();
			if (!world.isRemote)
				setUpAI(entity);
		}
	}
    
	public void setUpAI(EntityCreature entity) {
		 for (Object taskObject : entity.tasks.taskEntries.toArray()) {
			 EntityAIBase ai = ((EntityAITaskEntry) taskObject).action;
			 if (ai instanceof EntityAIPanic)
				 entity.tasks.removeTask(ai);
			 }
				entity.tasks.addTask(1, new AIPassiveMobAttack(entity, 1.25D, LONG_TERM_MEMORY));
				entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entity, CALL_FOR_HELP));
	}

	static class AIPassiveMobAttack extends EntityAIAttackMelee {
		public AIPassiveMobAttack(EntityCreature entity, double moveSpeed, boolean longMemory) {
			super(entity, moveSpeed, longMemory);
		}

		@Override
		protected void checkAndPerformAttack(EntityLivingBase entity, double distanceIn) {
			double reach = this.getAttackReachSqr(entity);
			if (distanceIn <= reach && this.attackTick <= 0) {
				attackTick = 20;
				attacker.swingArm(EnumHand.MAIN_HAND);

				// This overrides the vanilla method with out needing to fuck about with entities
					attackEntityAsMob(attacker, entity); // Use our damage value attack below
			}
		}

		public boolean attackEntityAsMob(EntityCreature entityAttacker, Entity entityIn) {
			return entityIn.attackEntityFrom(DamageSource.causeMobDamage(entityAttacker), ATTACK_DAMAGE);
		}
	}
}
