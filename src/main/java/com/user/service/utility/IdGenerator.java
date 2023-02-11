package com.user.service.utility;

import java.math.BigInteger;
import java.util.UUID;

public class IdGenerator {
	public static String generateUniqueId() {
		String str = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 24));
		return str.substring(0, 9);
	}
}