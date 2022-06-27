package com.empconn.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MailAttachmentUtil {

	public static ByteArrayOutputStream createInMemoryDocument(String documentBody) throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(documentBody.getBytes());
		return outputStream;
	}

}
