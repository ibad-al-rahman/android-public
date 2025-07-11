object GradleConfigs {
    const val COMPILE_SDK = 35
    const val MIN_SDK = 26
    private const val BASE_NAMESPACE = "org.ibadalrahman"

    fun subNamespace(nsp: String) = "$BASE_NAMESPACE.$nsp"
    fun subNamespaces(vararg nsps: String) = "$BASE_NAMESPACE.${nsps.joinToString(".")}"
}
