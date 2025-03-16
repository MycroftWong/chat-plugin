package wang.mycroft.core.data

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import wang.mycroft.core.network.NetworkModule

@ComponentScan
@Module(includes = [NetworkModule::class])
class DataModule
