package org.icatproject.exposed.parser;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.junit.Test;

public class TestJunk {

	@Test
	public void testJunk() throws Exception {

		try (JsonParser parser = Json.createParser(new ByteArrayInputStream("12".getBytes()))) {
			parser.next();
			fail("Exception expected as values are not accepted");
		} catch (JsonException e) {
			// Do nothing
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator gen = Json.createGenerator(baos);

		gen.writeStartArray();
		gen.writeStartObject();
		gen.writeStartObject("c");
		gen.write("a", "b");
		gen.writeEnd();
		gen.writeEnd();
		gen.writeEnd();
		gen.close();
		System.out.println(baos.toString());

		String j = "{\"sessionId\":\"fe79139a-11a6-4359-9456-8cd905b4f3f\",\"entity\":[{\"InvestigationType\":{\"facility\":{\"id\":24907},\"name\":\"ztype\"}}]}";
		JsonReader reader = Json.createReader(new ByteArrayInputStream(j.getBytes()));
		JsonObject model = (JsonObject) reader.read();
		System.out.println(model);

		model = Json.createObjectBuilder().add("sessionId", "aaa-bbb")
				.add("query", "Investigation").build();

		StringWriter stWriter = new StringWriter();
		try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
			jsonWriter.writeObject(model);
		}

		reader = Json.createReader(new ByteArrayInputStream(stWriter.toString().getBytes()));
		model = (JsonObject) reader.read();

		baos = new ByteArrayOutputStream();
		gen = Json.createGenerator(baos);
		gen.writeStartObject().write("sessionId", "ccc-ddd").write("query", "Job").writeEnd();
		gen.close();
		System.out.println(baos.toString());

		String t = baos.toString();

		String sessionId = null;
		String query = null;
		try (JsonParser parser = Json.createParser(new ByteArrayInputStream(t.getBytes()))) {
			String key = null;
			while (parser.hasNext()) {
				JsonParser.Event event = parser.next();
				if (event == Event.KEY_NAME) {
					key = parser.getString();
				} else if (event == Event.VALUE_STRING || event == Event.VALUE_NUMBER) {
					if (key.equals("sessionId")) {
						sessionId = parser.getString();
					} else if (key.equals("query")) {
						query = parser.getString();
					}
				}
			}
		}
		System.out.println(sessionId);
		System.out.println(query);

		baos = new ByteArrayOutputStream();
		gen = Json.createGenerator(baos);
		gen.writeStartObject().write("plugin", "db").writeStartArray("credentials")
				.writeStartObject().write("username", "root").writeEnd().writeStartObject()
				.write("password", "password").writeEnd().writeEnd().writeEnd();
		gen.close();
		System.out.println(baos.toString());

		try (JsonParser parser = Json.createParser(new ByteArrayInputStream(baos.toString()
				.getBytes()))) {
			String key = null;
			boolean inCredentials = false;
			String plugin = null;
			Map<String, String> credentials = new HashMap<>();
			while (parser.hasNext()) {
				JsonParser.Event event = parser.next();
				System.out.println(event);
				if (event == Event.KEY_NAME) {
					key = parser.getString();
					System.out.println(parser.getString());
				} else if (event == Event.VALUE_STRING) {
					if (inCredentials) {
						credentials.put(key, parser.getString());
					} else {
						if (key.equals("plugin")) {
							plugin = parser.getString();
						}
					}
				} else if (event == Event.START_ARRAY && key.equals("credentials")) {
					inCredentials = true;
				} else if (event == Event.END_ARRAY) {
					inCredentials = false;
				}
			}
			System.out.println(plugin);
			System.out.println(credentials);
		}

	}
}