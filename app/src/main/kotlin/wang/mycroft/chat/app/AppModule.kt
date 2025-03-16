package wang.mycroft.chat.app.wang.mycroft.chat.app

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import wang.mycroft.feature.chat.ChatModule
import wang.mycroft.feature.settings.SettingsModule

@ComponentScan
@Module(includes = [ChatModule::class, SettingsModule::class])
class AppModule
