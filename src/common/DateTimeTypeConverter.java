package common;

import java.lang.reflect.Type;

import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

//@author A0105860L
/*
 * Json serializer to properly serialize datetime objects
 */
public class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

	@Override
	public DateTime deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		assert(json!=null);
		return new DateTime(json.getAsString());

	}

	@Override
	public JsonElement serialize(DateTime datetime, Type type,
			JsonSerializationContext context) {
		return new JsonPrimitive(datetime.toString());
	}
}
