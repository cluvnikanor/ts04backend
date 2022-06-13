package com.sl.mdb04.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {
	public Map<UUID, String> tokenMap = new HashMap<UUID, String>();

	public void addUser(UUID uuid, String email) {
		tokenMap.put(uuid, email);
	}

	public void removeUser(String token) {
		tokenMap.remove(UUID.fromString(token));
	}

	public boolean validateUser(String token) {
		return tokenMap.containsKey(UUID.fromString(token));
	}

	public String getUserEmail(String token) {
		return tokenMap.get(UUID.fromString(token));
	}

	public boolean adminValidation(String adminToken) {
		return tokenMap.get(UUID.fromString(adminToken)).equals(Constants.ADMINEMAIL);
	}
}
