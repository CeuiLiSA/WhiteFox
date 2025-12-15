package ceui.lisa.hermes.common

import java.util.UUID


fun uuidToLong(token: String): Long {
    val uuid: UUID = UUID.fromString(token)
    return uuid.mostSignificantBits xor uuid.leastSignificantBits
}
