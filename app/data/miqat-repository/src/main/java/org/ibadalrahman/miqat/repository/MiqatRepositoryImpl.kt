package org.ibadalrahman.miqat.repository

import org.ibadalrahman.miqat.eventsForGregorianYear
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatData
import org.ibadalrahman.miqat.repository.data.domain.MiqatEventOccurrence
import org.ibadalrahman.miqat.repository.data.local.MiqatLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MiqatRepositoryImpl @Inject constructor(
    private val localDataSource: MiqatLocalDataSource,
) : MiqatRepository {

    override fun getMiqatData(timestampSecs: Long): MiqatData =
        MiqatData.compute(timestampSecs = timestampSecs, method = getCalculationMethod())

    override fun previewMiqatData(timestampSecs: Long, method: MiqatCalculationMethod): MiqatData =
        MiqatData.compute(timestampSecs = timestampSecs, method = method)

    override fun getIslamicEvents(gregorianYear: Int): List<MiqatEventOccurrence> =
        eventsForGregorianYear(gregorianYear).map(MiqatEventOccurrence::from)

    override fun setCalculationMethod(method: MiqatCalculationMethod) {
        localDataSource.saveCalculationMethod(method)
        // Retain the astronomical config so it survives a switch to precomputed and back.
        method.asAstronomical?.let(localDataSource::saveRetainedAstronomicalConfig)
    }

    override fun getCalculationMethod(): MiqatCalculationMethod =
        localDataSource.getCalculationMethod()

    override fun getRetainedAstronomicalConfig(): AstronomicalConfig? =
        localDataSource.getRetainedAstronomicalConfig()
}
