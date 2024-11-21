object GradleConfigs {
    const val compileSdk = 34
    const val minSdk = 26
    const val baseNamespace = "org.ibadalrahman.publicsector"

    fun subNamespace(sub: String) = "$baseNamespace.$sub"
}
