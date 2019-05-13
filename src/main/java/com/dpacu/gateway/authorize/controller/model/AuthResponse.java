package com.dpacu.gateway.authorize.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author ard333
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthResponse {
	private String username;
	
	private String accessToken;

	private String refreshToken;


}
