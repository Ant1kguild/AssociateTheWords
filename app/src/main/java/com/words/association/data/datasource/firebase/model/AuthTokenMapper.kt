package com.words.association.data.datasource.firebase.model

import com.google.firebase.auth.GetTokenResult
import com.words.association.core.BaseMapper

class AuthTokenMapper : BaseMapper<GetTokenResult, AuthToken> {
    override fun map(from: GetTokenResult): AuthToken {
        return AuthToken(
            from.token!!,
            from.expirationTimestamp
        )
    }
}