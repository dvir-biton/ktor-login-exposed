package com.fylora.security.token

import com.fylora.auth.security.token.TokenClaim

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}