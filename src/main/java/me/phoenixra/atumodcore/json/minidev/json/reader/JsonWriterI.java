package me.phoenixra.atumodcore.json.minidev.json.reader;

import me.phoenixra.atumodcore.json.minidev.json.JSONStyle;

import java.io.IOException;

public interface JsonWriterI<T> {
	public <E extends T> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException;
}
