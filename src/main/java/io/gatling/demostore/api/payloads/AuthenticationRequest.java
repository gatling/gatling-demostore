package io.gatling.demostore.api.payloads;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class AuthenticationRequest {

    @Schema(example = "admin", required = true)
    @NotNull
    private String username;

    @Schema(example = "admin", required = true)
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
