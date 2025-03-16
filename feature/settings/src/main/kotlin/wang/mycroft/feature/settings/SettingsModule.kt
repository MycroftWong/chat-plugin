package wang.mycroft.feature.settings

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import wang.mycroft.core.data.DataModule

@ComponentScan
@Module(includes = [DataModule::class])
class SettingsModule
