/*
 * Copyright (c) 2024 TeamMoeg
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

package com.teammoeg.frostedheart.scenario.runner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.teammoeg.frostedheart.scenario.runner.ScenarioConductor.ExecuteTarget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
/*
 * Paragraph data.
 * A Paragraph is like some sort of base block for code.
 * Declared local data like links and ui elements should be invalidate when paragraph is changed.
 * 
 * */
public class ParagraphData {
    CompoundNBT extraData;
    int paragraphNum;
    Map<String,ExecuteTarget> links=new HashMap<>();
    public ParagraphData(int paragraphNum) {
        super();
        this.paragraphNum = paragraphNum;
    }

    public boolean containsPath(String path) {
        String[] paths = path.split("\\.");
        CompoundNBT nbt = getExecutionData();
        for (int i = 0; i < paths.length - 1; i++) {
            if (!nbt.contains(paths[i], 10))
                return false;
            nbt = nbt.getCompound(paths[i]);
        }
        return nbt.contains(paths[paths.length - 1]);
    }

    public INBT evalPath(String path) {
        String[] paths = path.split("\\.");
        CompoundNBT nbt = getExecutionData();
        for (int i = 0; i < paths.length - 1; i++) {
            nbt = nbt.getCompound(paths[i]);
        }
        return nbt.get(paths[paths.length - 1]);
    }

    public Double evalPathDouble(String path) {
        String[] paths = path.split("\\.");
        CompoundNBT nbt = getExecutionData();
        for (int i = 0; i < paths.length - 1; i++) {
            nbt = nbt.getCompound(paths[i]);
        }
        return nbt.getDouble(paths[paths.length - 1]);
    }

    public String evalPathString(String path) {
        return evalPath(path).getString();
    }

    public CompoundNBT getExecutionData() {
    	if(extraData==null) {
    		extraData=new CompoundNBT();
    	}
        return extraData;
    }
    public void setPath(String path, INBT val) {
        String[] paths = path.split("\\.");
        CompoundNBT nbt = getExecutionData();
        for (int i = 0; i < paths.length - 1; i++) {
            if (nbt.contains(paths[i], 10)) {
                nbt = nbt.getCompound(paths[i]);
            } else if (!nbt.contains(paths[i])) {
                CompoundNBT cnbt = new CompoundNBT();
                nbt.put(paths[i], cnbt);
                nbt = cnbt;
            } else
                throw new IllegalArgumentException(String.join(".", Arrays.copyOfRange(paths, 0, i + 1)) + " is not an object");
        }
        nbt.put(paths[paths.length - 1], val);
    }

    public void setPathNumber(String path, Number val) {
        String[] paths = path.split("\\.");
        CompoundNBT nbt = getExecutionData();
        for (int i = 0; i < paths.length - 1; i++) {
            if (nbt.contains(paths[i], 10)) {
                nbt = nbt.getCompound(paths[i]);
            } else if (!nbt.contains(paths[i])) {
                CompoundNBT cnbt = new CompoundNBT();
                nbt.put(paths[i], cnbt);
                nbt = cnbt;
            } else
                throw new IllegalArgumentException(String.join(".", Arrays.copyOfRange(paths, 0, i + 1)) + " is not an object");
        }
        nbt.putDouble(paths[paths.length - 1], val.doubleValue());
    }

    public void setPathString(String path, String val) {
        String[] paths = path.split("\\.");
        CompoundNBT nbt = getExecutionData();
        for (int i = 0; i < paths.length - 1; i++) {
            if (nbt.contains(paths[i], 10)) {
                nbt = nbt.getCompound(paths[i]);
            } else if (!nbt.contains(paths[i])) {
                CompoundNBT cnbt = new CompoundNBT();
                nbt.put(paths[i], cnbt);
                nbt = cnbt;
            } else
                throw new IllegalArgumentException(String.join(".", Arrays.copyOfRange(paths, 0, i + 1)) + " is not an object");
        }
        nbt.putString(paths[paths.length - 1], val);
    }
}