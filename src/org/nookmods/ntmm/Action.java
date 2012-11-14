package org.nookmods.ntmm;

public final class Action {
	public final String id;
	public final String label;
	public final String command;
	public final int    iconId;

	public Action(String id, String label, int iconId, String command) {
		this.id = id;
		this.label = label;
		this.iconId = iconId;
		this.command = command;
	}
}
