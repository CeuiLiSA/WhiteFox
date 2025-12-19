package ceui.lisa.hermes.common

import android.util.Base64
import timber.log.Timber
import java.security.MessageDigest
import java.security.SecureRandom

object PKCEUtil {

    private const val CODE_VERIFIER_LENGTH = 32

    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(CODE_VERIFIER_LENGTH)
        SecureRandom().nextBytes(bytes)
        return bytes.toBase64Url()
    }

    private fun generateCodeChallenge(codeVerifier: String): String {
        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(codeVerifier.toByteArray(Charsets.US_ASCII))

        return digest.toBase64Url()
    }

    private fun ByteArray.toBase64Url(): String {
        return Base64.encodeToString(
            this,
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }

    val pkceItem by lazy { build() }

    private fun build(): PKCEItem {
        try {
            val v = generateCodeVerifier()
            return PKCEItem(
                v,
                generateCodeChallenge(v)
            )
        } catch (ex: Exception) {
            Timber.e(ex)
            return FALLBACK
        }
    }

}

data class PKCEItem(
    val verify: String,
    val challenge: String,
)


val FALLBACK = PKCEItem(
    "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI",
    "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0"
)