package com.teammoeg.frostedheart.trade;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teammoeg.frostedheart.util.SerializeUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ProductionData extends BaseData {
	ItemStack item;

	public ProductionData(String id, int maxstore, float recover, int price, ItemStack item) {
		super(id, maxstore, recover, price);
		this.item = item;

	}

	public ProductionData(JsonObject jo) {
		super(jo);
		item = SerializeUtil.fromJson(jo.get("produce"));
	}

	public ProductionData(PacketBuffer pb) {
		super(pb);
		item = pb.readItemStack();
	}

	@Override
	public void fetch(List<BuyData> buys, List<SellData> sell, Map<String, Float> data) {

		int num = (int) (float) data.getOrDefault(getId(), 0f);
		if (num > 0)
			sell.add(new SellData(item, num, getId()));
	}

	@Override
	public JsonElement serialize() {
		JsonObject jo = super.serialize().getAsJsonObject();
		jo.add("produce", SerializeUtil.toJson(item));
		return jo;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeBoolean(true);
		super.write(buffer);
		buffer.writeItemStack(item);
	}

	@Override
	public String getType() {
		return "s";
	}
}