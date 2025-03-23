object GradleConfigs {
    const val compileSdk = 34
    const val minSdk = 26
    const val baseNamespace = "org.ibadalrahman"

    fun subNamespace(nsp: String) = "$baseNamespace.$nsp"
    fun subNamespaces(vararg nsps: String) = "$baseNamespace.${nsps.joinToString(".")}"
}
