package me.phoenixra.atumodcore.json.minidev.json.reader;

import me.phoenixra.atumodcore.json.minidev.json.JSONStyle;
import me.phoenixra.atumodcore.json.minidev.json.JSONValue;

import java.io.IOException;

public class ArrayWriter implements JsonWriterI<Object> {
	public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
		compression.arrayStart(out);
		boolean needSep = false;
		for (Object o : ((Object[]) value)) {
			if (needSep)
				compression.objectNext(out);
			else
				needSep = true;
			JSONValue.writeJSONString(o, out, compression);
		}
		compression.arrayStop(out);
	}
}
