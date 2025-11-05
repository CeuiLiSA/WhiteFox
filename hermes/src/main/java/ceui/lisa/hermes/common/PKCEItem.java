package ceui.lisa.hermes.common;

import timber.log.Timber;

public class PKCEItem {

    private final String verify;
    private final String challenge;

    public PKCEItem(String verify, String challenge) {
        this.verify = verify;
        this.challenge = challenge;
    }

    public static PKCEItem build() {
        PKCEItem pkceItem;
        try {
            final String verify = PkceUtil.generateCodeVerifier();
            final String challenge = PkceUtil.generateCodeChallenge(verify);
            pkceItem = new PKCEItem(verify, challenge);
        } catch (Exception e) {
            Timber.e(e);
            pkceItem = new PKCEItem(
                    "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI",
                    "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0");
        }
        return pkceItem;
    }

    public String getVerify() {
        return verify;
    }

    public String getChallenge() {
        return challenge;
    }
}
